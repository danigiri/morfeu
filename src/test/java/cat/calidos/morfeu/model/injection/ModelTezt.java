/*
 *    Copyright 2017 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package cat.calidos.morfeu.model.injection;

import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.BasicCellModel;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelTezt {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();


protected Document produceDocumentFromPath(String path)
throws InterruptedException, ExecutionException, ParsingException, FetchingException, ValidationException {
	
	String doc1Path = testAwareFullPathFrom(path);
	URI uri = DaggerURIComponent.builder().from(doc1Path).builder().uri().get();

	DocumentComponent docComponent = DaggerDocumentComponent.builder()
										.from(uri)
										.withPrefix("")
										.build();
	
	return docComponent.produceDocument().get();

}

protected Model parseModelFrom(URI u) throws ConfigurationException, 
											 InterruptedException, 
											 ExecutionException, ParsingException, FetchingException {
		
	XSSchemaSet schemaSet = parseSchemaFrom(u);
		
	List<CellModel> rootCellModels = ModelModule.buildRootCellModels(schemaSet, u, null);
	XSAnnotation annotation = schemaSet.getSchema(Model.MODEL_NAMESPACE).getAnnotation();
	String desc = ModelModule.descriptionFromSchemaAnnotation(annotation);
	
	return ModelModule.produceModel(u, desc, u, schemaSet, rootCellModels);
		
}

protected CellModel cellModelFrom(URI u, String name) throws Exception {

	XSSchemaSet schemaSet = parseSchemaFrom(u);
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, name);
	Map<String,CellModel> globals = new HashMap<String, CellModel>();
	
	//FIXME: this particle creation is probably wrong but it seems to work, at least for complex types
	return DaggerCellModelComponent.builder()
									.fromElem(elem)
									.fromParticle(elem.getType().asComplexType().getContentType().asParticle())
									.withParentURI(u)
									.andExistingGlobals(globals)
									.build()
									.cellModel();

}


protected ComplexCellModel complexCellModelFrom(URI u, String name) throws Exception {
	
	CellModel cellModel = cellModelFrom(u, name);
	
	return ComplexCellModel.from(cellModel);	// from simple to complex
	
}


protected XSSchemaSet parseSchemaFrom(URI uri)
		throws InterruptedException, ExecutionException, ConfigurationException, ParsingException, FetchingException {
 
	XSOMParser parser = DaggerModelParserComponent.builder().build().produceXSOMParser().get();

	return ModelModule.parseModel(uri, parser);

}


protected Type provideElementType(XSElementDecl elem) {

	return DaggerTypeComponent.builder()	//awfully convenient to inject the dependencies, ok on integration tests
			.withDefaultName("default-type-name")
			.withXSType(elem.getType())
			.build()
			.type();
	
}


protected String testAwareFullPathFrom(String path) {
	return this.getClass().getClassLoader().getResource(path).toString();
}


}
