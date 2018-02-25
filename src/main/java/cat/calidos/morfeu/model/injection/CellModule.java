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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Predicate;

import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dagger.Module;
import dagger.Provides;
import cat.calidos.morfeu.model.Attributes;
import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCell;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.utils.OrderedMap;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class CellModule {

private static final String ATTRIBUTE_PREFIX = "@";
private static final String DEFAULT_DESC = "";
protected final static Logger log = LoggerFactory.getLogger(CellModule.class);
private static final String DEFAULT_VALUE = "";
	

@Provides
public static Cell provideCell(Node node,
								CellModel model,
								@Named("SimpleInstance") Provider<Cell> providerCell, 
								@Named("ComplexInstance") Provider<ComplexCell> providerComplexCell) {

	// we are only complex if we have a complex cell model and we're an element
	if (model.isComplex() && node instanceof Element) {

		return providerComplexCell.get();

	} else {	// this can happen when we have a simple cell model or we're an (internal) attribute

		return providerCell.get();

	}

}


@Provides @Named("SimpleInstance")
public static Cell buildCellFrom(URI u, 
								 @Named("name") String name, 
								 @Named("desc") String desc, 
								 @Named("value") String value, 
								 CellModel cm) {
	return new Cell(u, name, desc, value, cm);
}

@Provides @Named("ComplexInstance")
public static ComplexCell buildComplexCellFrom(URI u, 
										       @Named("name") String name, 
											   @Named("desc") String desc, 
											   @Named("value") String value, 
											   CellModel cm,
											   Composite<Cell> children,
											   Attributes<Cell> attributes,
											   @Named("InternalAttributes") Attributes<Cell> internalAttributes
											   ) {
	return new ComplexCell(u, name, desc, value, cm, children, attributes, internalAttributes);
}


@Provides @Named("name") 
public static String nameFrom(Node node) {
	return node.getNodeName();
}


@Provides @Named("desc") 
public static String desc() {
	return DEFAULT_DESC;	
}


@Provides @Named("value") 
public static String valueFrom(Node node) {

	String value = node.getTextContent();

	return (value!=null)? value : DEFAULT_VALUE;	//TODO: how to handle empty values properly

}


@Provides
protected static ComplexCellModel effectiveCellModel(CellModel cellModel) {
	return cellModel.asComplex();
}


// bear in mind we can do element() instead of node if we need it, as it holds type information
@Provides
public static Composite<Cell> childrenFrom(Node node, URI uri, ComplexCellModel cellModel) {
	
 	if (!node.hasChildNodes()) {	// base case, save some memory on the list
	
		return new OrderedMap<Cell>(0);	

	}

	// recursive case
	Composite<Cell> children = new OrderedMap<Cell>(node.getChildNodes().getLength());
	int nodeIndex = 0;
	// this works for now, but some day it will come back with a vengeance :)
	NodeList nodeList = ((Element)node).getElementsByTagName("*");	//TODO: use node and detect text as values as this gives you all descendants
	for (int i=0; i<nodeList.getLength();i++) {						// or use normalise (SLOW CODE!)
		
		Element childElem = (Element) nodeList.item(i);	// We know it's element but this is not guaranteed not to change
		if (childElem.getParentNode()==node) {			// Only handle the current element children!!! (SLOW!)
			String childName = childElem.getTagName();
			CellModel childCellModel = findChildWithName(cellModel, childName);
			String childIndexedName = childName+"("+nodeIndex+")";	// to name children differently and for the URI
			URI childURI = cellURI(uri, cellModel, "/"+childIndexedName);	// foo(1)/bar(0)
			Cell childCell = DaggerCellComponent.builder()
													.withURI(childURI)
													.fromNode(childElem)
													.withCellModel(childCellModel)
													.builder()
													.createCell();

			children.addChild(childIndexedName, childCell);
			nodeIndex++;

		}

	}

	return children;

}



@Provides
public static Attributes<Cell> publicAttributesFrom(Node node, URI uri, ComplexCellModel cellModel) {
	// a bit slow as we go through the attributes twice, in the future we can do streams and group by to optimise
	return attributesFrom(node, uri, cellModel, attributeName->!(attributeName.startsWith("xmlns:") || attributeName.startsWith("xsi:")));
}


@Provides @Named("InternalAttributes")
public static Attributes<Cell> internalAttributesFrom(Node node, URI uri, ComplexCellModel cellModel) {
	// a bit slow as we go through the attributes twice, in the future we can do streams and group by to optimise
	return attributesFrom(node, uri, cellModel,  attributeName->attributeName.startsWith("xmlns:") || attributeName.startsWith("xsi:"));
}



private static  Attributes<Cell> attributesFrom(Node node, URI uri, ComplexCellModel cellModel, Predicate<String> attributeFilter) {
	
	if (!node.hasAttributes()) {	//	** base case **
		
		return new OrderedMap<Cell>(0);
	}
	
	if (cellModel.isSimple()) {
		log.error("CellModel '{}' does not allow attributes but the elem does", cellModel.getName());
		throw new RuntimeException("Element and model attribute mismatch", new IllegalArgumentException());
	}
	
	// 								** recursive case **
	OrderedMap<Cell> attributes = new OrderedMap<Cell>(node.getAttributes().getLength());
	NamedNodeMap elemAttributes = node.getAttributes();
	for (int i=0; i<elemAttributes.getLength(); i++) {
		
		Node attribute = elemAttributes.item(i);
		String attributeName = attribute.getNodeName();
		URI childURI = cellURI(uri, cellModel, ATTRIBUTE_PREFIX+attributeName);
		// if we are looking for public attributes, they need to match with the attributes model, otherwise we just
		// point to the node cell model (IDEA: maybe in the future point to the internal model)
		CellModel attributeCellModel = findAttributeWithName(cellModel, attributeName);
		if (attributeFilter.test(attributeName)) {
	
			Cell attributeCell = DaggerCellComponent.builder()
														.withURI(childURI)
														.fromNode(attribute)
														.withCellModel(attributeCellModel)
														.builder()
														.createCell();
			attributes.addChild(attributeName, attributeCell);
			
		} else  {

		}
	}

	return attributes;

}


private static CellModel findChildWithName(ComplexCellModel cellModel, String childName) {

	Optional<CellModel> matchedChild = cellModel.children()
												.asList()
												.stream()
												.filter(cm -> cm.getName().equals(childName))
												.findFirst();
	if (!matchedChild.isPresent()) {
		log.warn("Elem '{}' could not match any children of '{}'", childName, cellModel.getName());
		throw new RuntimeException("Node and model mismatch ("+childName+")", new IllegalArgumentException());
	}

	return matchedChild.get();

}


private static URI cellURI(URI uri, CellModel cellModel, String childName) throws RuntimeException {

	try {
		return new URI(uri+childName);
	} catch (URISyntaxException e) {
		log.error("Child '{}' could not build uri of '{}'", childName, cellModel.getName());
		throw new RuntimeException("Wrong uri for cell", e);
	}
	
}


private static CellModel findAttributeWithName(ComplexCellModel cellModel, String attributeName) {

	Optional<CellModel> matchedAttribute = cellModel.attributes()
													.asList()
													.stream()
													.filter(cm -> cm.getName().equals(attributeName))
													.findFirst();
	if (!matchedAttribute.isPresent()) {
		// FIXME: this is a hack, we should handle this more gracefully
		if (!(attributeName.startsWith("xmlns:") || attributeName.startsWith("xsi:"))) {

			log.error("Elem '{}' could not match any attribute of '{}'", attributeName, cellModel.getName());
			throw new RuntimeException("Node and model attribute mismatch", new IllegalArgumentException());
		}
	}

	return matchedAttribute.orElse(cellModel);
	
}


}
