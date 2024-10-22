// MODEL TEZT . JAVA
package cat.calidos.morfeu.model.injection;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.metadata.injection.GlobalModelMetadataModule;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Tezt;
import cat.calidos.morfeu.utils.injection.DaggerURIComponent;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelTezt extends Tezt {

public static final int EXPECTED_COL_CHILDREN_COUNT = 13;

protected Document produceDocumentFromPath(String path) throws InterruptedException,
		ExecutionException, ParsingException, FetchingException, ValidationException {

	String docPath = testAwareFullPathFrom(path);
	URI uri = DaggerURIComponent.builder().from(docPath).build().uri().get();

	return DaggerDocumentComponent.builder().from(uri).withPrefix("").build().document().get();

}


protected Model parseModelFrom(URI u) throws ConfigurationException, InterruptedException,
		ExecutionException, ParsingException, FetchingException {

	XSSchemaSet schemaSet = parseSchemaFrom(u);
	XSAnnotation annotation = schemaSet.getSchema(Model.MODEL_NAMESPACE).getAnnotation();
	Metadata metadata = ModelModule.metadata(u, annotation);
	String desc = ModelModule.description(metadata);
	Type type = DaggerTypeComponent
			.builder()
			.withDefaultName(ModelModule.ROOT_NAME)
			.andURI(u)
			.build()
			.emptyType();
	Attributes<CellModel> attributes = ModelModule.attributes();
	Map<URI, Metadata> globalModelMetadata = GlobalModelMetadataModule
			.provideGlobalModelMetadata(annotation, u);
	Composite<CellModel> rootCellModels = ModelModule
			.rootCellModels(schemaSet, u, globalModelMetadata);

	return ModelModule.model(u, desc, type, metadata, attributes, schemaSet, rootCellModels);

}


protected CellModel cellModelFrom(	URI u,
									String name)
		throws Exception {

	XSSchemaSet schemaSet = parseSchemaFrom(u);
	XSElementDecl elem = schemaSet.getElementDecl(Model.MODEL_NAMESPACE, name);
	var globals = new HashMap<String, CellModel>();
	XSAnnotation annotation = schemaSet.getSchema(Model.MODEL_NAMESPACE).getAnnotation();
	Map<URI, Metadata> globalMetadata = GlobalModelMetadataModule
			.provideGlobalModelMetadata(annotation, u);

	// FIXME: this particle creation is probably wrong but it seems to work, at least for complex
	// types
	return DaggerCellModelComponent
			.builder()
			.fromElem(elem)
			.fromParticle(elem.getType().asComplexType().getContentType().asParticle())
			.withParentURI(u)
			.withGlobalMetadata(globalMetadata)
			.andExistingGlobals(globals)
			.build()
			.cellModel();

}


protected ComplexCellModel complexCellModelFrom(URI u,
												String name)
		throws Exception {

	CellModel cellModel = cellModelFrom(u, name);

	return ComplexCellModel.from(cellModel); // from simple to complex

}


protected XSSchemaSet parseSchemaFrom(URI uri) throws InterruptedException, ExecutionException,
		ConfigurationException, ParsingException, FetchingException {

	XSOMParser parser = DaggerModelParserComponent.builder().build().produceXSOMParser().get();

	return ModelModule.parseModel(uri, parser);

}


protected Type provideElementType(XSElementDecl elem) {

	return DaggerTypeComponent
			.builder() // awfully convenient to inject the dependencies, ok on
						// integration tests
			.withDefaultName("default-type-name")
			.withXSType(elem.getType())
			.build()
			.type();

}


protected JsonNode readYAMLFrom(String path) throws Exception {

	var mapper = new YAMLMapper();
	var inputFile = new File(path);
	String content = FileUtils.readFileToString(inputFile, Config.DEFAULT_CHARSET);
	JsonNode yaml = mapper.readTree(content);

	return yaml;

}


protected Map<String, Object> valueMapFrom(Document doc) {

	var values = new HashMap<String, Object>(2);
	values.put("cells", doc.children()); // skip document, do not skip root node and it's more
											// compatible
	values.put("model", doc.getModel());

	return values;

}


protected String temporaryOutputFilePathIn(String pref) {
	return pref + "/filesaver-test-" + System.currentTimeMillis() + ".txt";
}

}

/*
 * Copyright 2019 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
