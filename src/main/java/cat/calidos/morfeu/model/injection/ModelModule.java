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

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.xml.sax.SAXException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.ComplexCell;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import dagger.producers.Produced;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule 
public class ModelModule extends RemoteModule {


@Produces
public static Model produceModel(@Named("ModelURI") URI u, 
								 @Named("desc") String desc, 
								 XSSchemaSet schemaSet, 
								 List<CellModel> rootTypes) {
	return new Model(u, desc, schemaSet, rootTypes);
}


@Produces
public static XSSchemaSet parseModel(@Named("ModelURI") URI u, Produced<XSOMParser> parserProducer) throws ParsingException, ExecutionException, FetchingException {
	
	XSOMParser parser = parserProducer.get();
	XSSchemaSet schemaSet = null;
	String uri = u.toString();
	try {
		parser.parse(uri);
		schemaSet = parser.getResult();
	} catch (SAXException e) {
		// either it's a broken or invalid model or the model is just not found
		if (e.getCause() instanceof FileNotFoundException) {
			throw new FetchingException("Problem fetching model '"+uri+"'", e);
		} else {
			throw new ParsingException("Problem parsing model '"+uri+"'", e);
		}
	}
	
	return schemaSet;
	
}


@Produces
public static List<CellModel> buildRootCellModels(XSSchemaSet schemaSet) {

	ArrayList<CellModel> rootTypes = new ArrayList<CellModel>();
	Iterator<XSElementDecl> iterator = schemaSet.iterateElementDecls();
	iterator.forEachRemaining(elem -> rootTypes.add(buildCellModel(elem)));

	return rootTypes;

}


@Produces @Named("desc")
public static String descriptionFromSchemaAnnotation(XSSchemaSet schemaSet) {
	
	XSSchema schema = schemaSet.getSchema(Model.MODEL_NAMESPACE);
	XSAnnotation annotation = schema.getAnnotation();
	Node annotationDOM = (Node)annotation.getAnnotation();	// as we are using the DomAnnotationParserFactory from XSOM
	String desc = DaggerModelMetadataComponent.builder().from(annotationDOM).named("mf:desc").build().value();
	
	return desc;

}


private static CellModel buildCellModel(XSElementDecl elem) {
	return DaggerCellModelComponent.builder().withElement(elem).build().cellModel();
}

}
