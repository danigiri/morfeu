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

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XmlString;

import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.BasicCellModel;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.metadata.injection.DaggerModelMetadataComponent;
import cat.calidos.morfeu.utils.OrderedMap;
import dagger.Module;
import dagger.Provides;


/** Module to create cell models
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class CellModelModule {

protected final static Logger log = LoggerFactory.getLogger(CellModelModule.class);

//private static final String DEFAULT_DESC = "";
private static final String NODE_SEPARATOR = "/";
private static final int ATTRIBUTE_MIN = 0;
private static final int ATTRIBUTE_REQUIRED = 1;
private static final int ATTRIBUTE_MAX = 1;
private static final String ATTRIBUTE_SEPARATOR = "@";
private static final String DEFAULT_TYPE_POSTFIX = "-type";


// creates the cell model, decides if lazily invoking ghe complex or simple cell dependency graph
@Provides
public static CellModel provideCellModel(Type t,
									    Provider<BasicCellModel> providerCell,
									    Provider<ComplexCellModel> providerComplexCell) {
	return (t.isSimple()) ? providerCell.get() : providerComplexCell.get();
}


// we find out if we are a reference, checking this type against the global cell model pool
@Provides @Named("isReference")
public static boolean isReference(Type t, Map<String, CellModel> globals) {
	return globals!=null && globals.containsKey(t.getName());
}


// get the reference cell model from the global pool (will be lazily called)
@Provides @Named("reference")
public static CellModel reference(Type t, Map<String, CellModel> globals) {
	return globals.get(t.getName());
}


// create a basic cell model instance, either a reference or a full one, with the provided data
@Provides
public static BasicCellModel buildCellModelFrom(URI u,
												@Named("name") String name, 
												@Named("desc") String desc,
												@Named("MinOccurs") int minOccurs,
												@Named("MaxOccurs") int maxOccurs,
												Optional<String> defaultValue,
												Type t, 
												Metadata metadata,
												@Named("isReference") boolean isReference,
												@Named("reference") Provider<CellModel> referenceProvider,
												Map<String, CellModel> globals) {

	// TODO: add cell description from metadata
	BasicCellModel newCellModel;
	if (isReference) {
		// we are a cell model reference, so we get the reference cell model and use it to build ourselves
		// bear in mind that references can have different metadata (defaults, etc.)
		CellModel reference = referenceProvider.get();
		newCellModel = new BasicCellModel(u, name, desc, t, minOccurs, maxOccurs, false, defaultValue, metadata, reference);
	} else {
		// we are a new cell model, we create a new instance and add it to globals so future cells can reference it
		newCellModel = new BasicCellModel(u, name, desc, t, minOccurs, maxOccurs, false, defaultValue, metadata);
		updateGlobalsWith(globals, t, newCellModel);
	}

	return newCellModel;

}


// create a complex cell model instance, either a reference or a full one
@Provides
public static ComplexCellModel buildComplexCellModelFrom(URI u,
															@Named("name") String name,
															@Named("desc") String desc, 
															@Named("MinOccurs") int minOccurs,
															@Named("MaxOccurs") int maxOccurs,
															Optional<String> defaultValue,
															Type t,
															Metadata metadata,
															Provider<Attributes<CellModel>> attributesProvider,
															Provider<Composite<CellModel>> childrenProvider,
															@Named("isReference") boolean isReference,
															@Named("reference")Provider<CellModel> referenceProvider,
															Map<String, CellModel> globals) {

	ComplexCellModel newComplexCellModel;

	String typeName = t.getName();
	if (isReference) {

		// we are a cell model reference, so we get the reference cell model and use it to build ourselves
		CellModel reference = referenceProvider.get();
		if (reference.isSimple()) {
			// this should not happen, the model is inconsistent =/
			throw new RuntimeException("Found a complex refence to a simple type ("+typeName+")");
		}

		// Attributes are the same as the reference but may have different metatata, so we use our own provided attribs
		newComplexCellModel = new ComplexCellModel(u, 
													name, 
													desc, 
													t, 
													minOccurs, 
													maxOccurs, 
													metadata,
													defaultValue,
													attributesProvider.get(),	
													reference.asComplex());

	} else {

		// New cell model:
		// We create the cell model first, find out if it's global (add it globals if so) and then generate the
		// attributes and children. This means that if a child references an already defined CellModel (which could
		// include this very one), there will be no infinite loops and the child will be created as a reference to this
		// one we've just created.
		// This means that the cell model instance is 'mutable', but we mutate it within this small scope

		newComplexCellModel = new ComplexCellModel(u, 
				name, 
				desc, 
				t, 
				minOccurs, 
				maxOccurs, 
				metadata,
				defaultValue,
				new OrderedMap<CellModel>(0),	// empty attribs
				new OrderedMap<CellModel>(0));	// empty children

		updateGlobalsWith(globals, t, newComplexCellModel);

		newComplexCellModel.setAttributes(attributesProvider.get());
		newComplexCellModel.setChildren(childrenProvider.get());

	}

	return newComplexCellModel;

}


// description of the cell model
@Provides @Named("desc")
public String desc(Metadata meta, Type t) {
	return meta.getDesc();
}


// minimum number of times cells following this model need to appear in its context
@Provides @Named("MinOccurs")
public int minOccurs(XSParticle particle) {
	return particle.getMinOccurs().intValueExact();
}


// maxium number of times cells following thi smodel can appear in a given context, -1 if unlimited
@Provides @Named("MaxOccurs")
public int maxOccurs(XSParticle particle) {

	BigInteger maxOccurs = particle.getMaxOccurs();
	
	return maxOccurs.equals(BigInteger.valueOf(XSParticle.UNBOUNDED)) ? CellModel.UNBOUNDED : maxOccurs.intValueExact();

}


// default value for the cell following this model, if any
@Provides
public Optional<String> defaultValue(Metadata metadata) {
	return Optional.ofNullable(metadata.getDefaultValues().get(null)); // null is the key for the cell default value
}


// given the XSD type, we get our own domain type instance
@Provides
public static Type getTypeFrom(XSType type, @Named("TypeDefaultName") String defaultName) {
	return DaggerTypeComponent.builder()
								.withDefaultName(defaultName)
								.withXSType(type)
								.build()
								.type();
}

// attributes of the current cell model
@Provides
public static Attributes<CellModel> attributesOf(XSElementDecl elem,
													Type t,
													URI u,
													Metadata metadata,
													@Nullable Map<String, CellModel> globals) {

	if (t.isSimple()) {
		return new OrderedMap<CellModel>(0);
	}
	
	XSComplexType complexType = elem.getType().asComplexType();
	Collection<? extends XSAttributeUse> rawAttributes = complexType.getAttributeUses();

	Attributes<CellModel> attributes = new OrderedMap<CellModel>(rawAttributes.size());

	rawAttributes.forEach(a -> {
								XSAttributeDecl attributeDecl = a.getDecl();
								boolean isRequired = a.isRequired();
								CellModel cellModel = attributeCellModelFor(attributeDecl, 
																			isRequired, 
																			u, 
																			metadata, 
																			globals);
								attributes.addAttribute(attributeDecl.getName(), cellModel);
	});

	return attributes;
	
}


// recursive magic happens here, we generate the cell model children of the current cell model
@Provides
public static Composite<CellModel> childrenOf(XSElementDecl elem,
												Type t,
												URI u,
												Map<String, CellModel> globals,
												Map<URI, Metadata> globalMetadata) {
	
	// Magic happens here: 
	// BASE CASES:
	//	if we are a simple type we are at a leaf
	//	if we are an empty type we do not have children
	// RECURSIVE CASE:
	//	we go through all the children and we add them
	if (t.isSimple()) {
		return new OrderedMap<CellModel>(0);							// * base case, simple type sanity check
	}

	Composite<CellModel> children = new OrderedMap<CellModel>();

	XSComplexType complexType = elem.getType().asComplexType();
	XSContentType contentType = complexType.getContentType();
	if (contentType.asEmpty()!=null) {
		return new OrderedMap<CellModel>(0);							// * base case, no children, we return
	}

	XSParticle particle = contentType.asParticle();						// * recursive case, go through all children
	LinkedList<XSParticle> termTypes = new LinkedList<XSParticle>();	// list of all the particles left to process
	termTypes.add(particle);
	while (!termTypes.isEmpty()) {
		particle = termTypes.removeFirst();

		if (particle.getTerm().isModelGroup()) {

			// FIXME: we need to see what to do when we have more complex groups like unions and stuff 
			XSModelGroup typeModelGroup = particle.getTerm().asModelGroup();
			typeModelGroup.iterator().forEachRemaining(m -> termTypes.add(m.asParticle()));

		} else {

			XSElementDecl childElem = particle.getTerm().asElementDecl();
			CellModel childCellModel = DaggerCellModelComponent.builder()
																.fromElem(childElem)
																.fromParticle(particle)
																.withParentURI(u)
																.withGlobalMetadata(globalMetadata)
																.andExistingGlobals(globals)
																.build()
																.cellModel();
			children.addChild(childCellModel.getName(), childCellModel);

		}
	}

	return children;

}


// probably not used
@Provides
public Locator locatorFrom(XSElementDecl elem) {
	return elem.getLocator();
}


// name of the cell model
@Provides @Named("name")
public String nameFrom(XSElementDecl elem) {
	return elem.getName();
}


// build the URI of the current cell, built relative to the parent one
@Provides @Named("URIString")
public String getURIString(@Named("ParentURI") URI parentURI, @Named("name") String name) {
	return parentURI+NODE_SEPARATOR+name;
}


// get the URI instance of the current cell
@Provides
public static URI getURIFrom(@Named("URIString") String uri, @Named("name") String name) throws RuntimeException {

	try {

		return new URI(uri);

	} catch (URISyntaxException e) {
		log.error("What the heck, URI '{}' of element '{}' is not valid ", uri, name);
		throw new RuntimeException("Somehow we failed to create URI of element "+name, e);
	}

}


// if we do not have a specified cell model type anme, we will create a default one, from the cell model name
@Provides @Named("TypeDefaultName")
public static String getDefaultTypeName(XSElementDecl elem) {
	return elem.getName()+DEFAULT_TYPE_POSTFIX;
}


@Provides
public static XSType type(XSElementDecl elem) {
	return elem.getType();
}


@Provides
public static Metadata metadata(XSElementDecl elem, 
								URI uri, 
								Type t, 
								Map<URI, Metadata> globalMetadata,
								@Named("isReference") boolean isReference,
								@Named("reference") Provider<CellModel> referenceProvider) {

	// we get the metadata from the current cell model, this will have the highest priority
	Metadata meta = DaggerModelMetadataComponent.builder()
													.from(elem.getAnnotation())
													.withParentURI(uri)
													.build()
													.value();

	//fallback from merging global(if available) and type
	Metadata typeMetadata = t.getMetadata();
	Metadata fallback = (globalMetadata!=null  && globalMetadata.containsKey(uri)) ?
							Metadata.merge(uri, globalMetadata.get(uri), typeMetadata) : typeMetadata;
							
	// then we merge the current cell model metadata with the fallback 
	meta = Metadata.merge(uri, meta, fallback);
	
	// then if we are a reference we will merge the reference metadata as well :)
	// * but we do it with the type metadata, as the reference cellModel will have merged the global stuff
	// * and the global stuff is referenced with an explicit URI that is not the current cell model, therefore it is
	// * not intuitive to merge our meta with the reference type + the reference cell model (global) meta
	// --> which means we go for the reference type only
	if (isReference) {
		// We use the given metadata (just the type, not global) and we merge it with the reference meta to cover any 
		// gaps (Notice our own metadata has more priority)
		meta = Metadata.merge(uri, meta, referenceProvider.get().getType().getMetadata());
	}
	
	return meta;
	
}


private static CellModel attributeCellModelFor(XSAttributeDecl xsAttributeDecl, 
											 boolean required,
											 URI nodeURI, 
											 Metadata metadata,	// remember this is the cell metadata
											 @Nullable Map<String, CellModel> globals) {

	String name = xsAttributeDecl.getName();
	URI attributeURI = getURIFrom(nodeURI.toString()+ATTRIBUTE_SEPARATOR+name, name);
	Type type = DaggerTypeComponent.builder()
									.withDefaultName(name)
									.withXSType(xsAttributeDecl.getType())
									.build()
									.type();
	int minOccurs = required ? ATTRIBUTE_REQUIRED : ATTRIBUTE_MIN;

	
			
	Metadata attributeMetadata = DaggerModelMetadataComponent.builder()
																.from(xsAttributeDecl.getAnnotation())
																.withParentURI(attributeURI)
																.build()
																.value();
	
	attributeMetadata = Metadata.merge(attributeURI, attributeMetadata, type.getMetadata());
	
	// default value priorities
	// 1) the Cell metadata, with '<mf:default-value name="@attributename">foo</mf:default-value>'
	// 2) XML schema default="foo" (on optional attributes)
	// 3) the type default value
	Optional<String> defaultValue = Optional.ofNullable(
			metadata.getDefaultValues().get(Metadata.DEFAULT_VALUE_PREFIX+name));
	if (!defaultValue.isPresent()) {
		XmlString defaultValueXMLString = xsAttributeDecl.getDefaultValue();
		defaultValue = (defaultValueXMLString!=null) ? Optional.of(defaultValueXMLString.value) : defaultValue;
	}
	String defaultValueFromType = attributeMetadata.getDefaultValues().get(name);
	defaultValue = (!defaultValue.isPresent()) ? Optional.ofNullable(defaultValueFromType) : defaultValue;
	
	// no references for attributes at the moment
//	if (globals.containsKey(type.getName())) {		// if it's an attribute we keep the local uri
//		cellModel = new BasicCellModelReference(attributeURI, name, globals.get(type.getName()));
//	} else {
		
	// attributes have the presentation of the corresponding type
	return new BasicCellModel(attributeURI,
								name, 
								attributeMetadata.getDesc(), 
								type, 
								minOccurs,
								ATTRIBUTE_MAX,
								true,					// is an attribute
								defaultValue,
								attributeMetadata);
//	}

	
}


private static void updateGlobalsWith(@Nullable Map<String, CellModel> globals, Type t, BasicCellModel newCellModel) {

	if (t.isGlobal() && globals!=null) {
		globals.put(t.getName(), newCellModel);
	}
	
}


////for each weak reference, we look for the referenced cell model and turn it into a proper reference
//@Provides
//public static CellModel normaliseWeakReferences(CellModel cellModel, Map<String, CellModel> globalCellModels) {
//
//	if (cellModel.isReference() && cellModel.asReference().isWeak()) {
//		
//		
//		
//	}
//	
//	// at this point, if it's a reference it means it has been normalised
//	if (cellModel.isSimple() || cellModel.isReference()) {
//		return cellModel;
//	}
//	
//	ComplexCellModel complexCellModel = cellModel.asComplex();
//	complexCellModel.attributes().asList().forEach(a -> normaliseWeakReferences(root, a));
//	complexCellModel.children().asList().forEach(c -> normaliseWeakReferences(root, c));
//	
//	return cellModel;
//}


}
