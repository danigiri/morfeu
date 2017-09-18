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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.impl.util.SchemaTreeTraverser;
import com.sun.xml.xsom.impl.util.SchemaTreeTraverser.SchemaTreeModel;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import dagger.Provides;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(includes=ModelParserModule.class)
public class ModelModule extends RemoteModule {

private static final String METADATA_DESC_FIELD = "mf:desc";


// here we're using the model uri as it will be used to populate internal uri of cell models and it's a neat 
// representation
@Produces
public static Model produceModel(@Named("ModelURI") URI u, 
								 @Named("desc") String desc, 
								 @Named("FetchableModelURI") URI fetchableURI,
								 XSSchemaSet schemaSet, 
								 List<CellModel> rootTypes) {
	return new Model(u, desc, fetchableURI, schemaSet, rootTypes);
}


// notice here we are using the fetchable uri to get the schema and parse it, as this one is guaranteed to be fetchable
@Produces
public static XSSchemaSet parseModel(@Named("FetchableModelURI") URI u, XSOMParser parser) 
		throws ParsingException, ExecutionException, FetchingException {
	
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
public static List<CellModel> buildRootCellModels(XSSchemaSet schemaSet, @Named("ModelURI") URI u) {

	SchemaTreeTraverser traverser = new SchemaTreeTraverser();
	traverser.visit(schemaSet);
	//SchemaTreeModel model = traverser.getModel();
	
	ArrayList<CellModel> rootTypes = new ArrayList<CellModel>();
	Set<Type> processedTypes = new HashSet<Type>();
	Map<String, CellModel> globals = new HashMap<String, CellModel>();
	
	Iterator<XSElementDecl> iterator = schemaSet.iterateElementDecls();
	iterator.forEachRemaining(elem -> {
									   XSParticle part = elem.getType().asComplexType().getContentType().asParticle();
									   rootTypes.add(buildCellModel(elem, part, u, processedTypes, globals));
	});
	
	return rootTypes;

}


@Produces @Named("desc")
public static String descriptionFromSchemaAnnotation(XSSchemaSet schemaSet) {

	XSSchema schema = schemaSet.getSchema(Model.MODEL_NAMESPACE);
	Metadata meta = DaggerModelMetadataComponent.builder()
										.from(schema.getAnnotation())
										.build()
										.value();
	return meta.getDesc();
}



// notice we keep the processed types as we build the root cell models as global types can appear in different
// root cell models
private static CellModel buildCellModel(XSElementDecl elem, 
										XSParticle particle, 
										URI u, 
										Set<Type> types, 
										Map<String, CellModel> globals) {
	return DaggerCellModelComponent.builder()
									.fromElem(elem)
									.fromParticle(particle)
									.withParentURI(u)
									.andExistingGlobals(globals)
									.build()
									.cellModel();
}

}
