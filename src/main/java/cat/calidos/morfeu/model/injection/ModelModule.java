// MODEL MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.metadata.injection.DaggerModelMetadataComponent;
import cat.calidos.morfeu.model.metadata.injection.GlobalModelMetadataModule;
import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.OrderedMap;
import cat.calidos.morfeu.utils.injection.RemoteModule;


/** A model is just a specialised cellmodel at the root, may have additional metadata
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(includes={ModelParserModule.class, GlobalModelMetadataModule.class})
public class ModelModule extends RemoteModule {

public static final String ROOT_NAME = "";

// here we're using the model uri as it will be used to populate internal uri of cell models and it's a neat 
// representation
@Produces
public static Model model(@Named("ModelURI") URI u,
							@Named("desc") String desc,
							Type type,
							Metadata metadata,
							Attributes<CellModel> attributes,
							XSSchemaSet schemaSet,
							@Named("RootCellModels") Composite<CellModel> rootCellModels) {
	return new Model(u,
						ROOT_NAME,
						desc,
						type,
						1,
						1,
						Optional.empty(),
						metadata.getCategory(),
						metadata,
						attributes,
						schemaSet,
						rootCellModels);
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


@Produces @Named("RootCellModels")
public static Composite<CellModel> rootCellModels(XSSchemaSet schemaSet,
													@Named("ModelURI") URI u,
													Map<URI, Metadata> globalMetadata) {

	Composite<CellModel> rootCellModels = new OrderedMap<CellModel>(schemaSet.getSchemaSize());
	Set<Type> processedTypes = new HashSet<Type>();
	Map<String, CellModel> globals = new HashMap<String, CellModel>();

	Iterator<XSElementDecl> elems = schemaSet.iterateElementDecls();
	elems.forEachRemaining(elem -> {
									XSParticle part = elem.getType().asComplexType().getContentType().asParticle();
									CellModel cellModel = buildCellModel(elem,
																			part,
																			u,
																			processedTypes,
																			globals,
																			globalMetadata);
									rootCellModels.addChild(cellModel.getName(), cellModel);
									}
	);

	return rootCellModels;

}


@Produces @Named("desc")
public static String description(Metadata metadata) {
	return metadata.getDesc();
}


@Produces
public static Type type(@Named("ModelURI") URI u) {	// empty type for the model as it's only virtual
	return DaggerTypeComponent.builder().withDefaultName(ROOT_NAME).andURI(u).build().emptyType();
}


@Produces
public static Metadata metadata(@Named("ModelURI") URI u, XSAnnotation annotation) {
	return DaggerModelMetadataComponent.builder()
										.from(annotation)
										.withParentURI(u)
										.build()
										.value();
}


@Produces
public static XSAnnotation annotationFrom(XSSchemaSet schemaSet) {
	return schemaSet.getSchema(Model.MODEL_NAMESPACE).getAnnotation();
}


@Produces
public static Attributes<CellModel> attributes() {
	return new OrderedMap<CellModel>(0);
}


// notice we keep the processed types as we build the root cell models as global types can appear in different
// root cell models
private static CellModel buildCellModel(XSElementDecl elem,
										XSParticle particle,
										URI u,
										Set<Type> types,
										Map<String, CellModel> globals,
										Map<URI, Metadata> globalMetadata) {
	return DaggerCellModelComponent.builder()
									.fromElem(elem)
									.fromParticle(particle)
									.withParentURI(u)
									.withGlobalMetadata(globalMetadata)
									.andExistingGlobals(globals)
									.build()
									.cellModel();
}


}

/*
 *    Copyright 2019 Daniel Giribet
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

