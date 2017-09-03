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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

private static final String DEFAULT_DESC = "";
protected final static Logger log = LoggerFactory.getLogger(CellModule.class);
private static final String DEFAULT_VALUE = "";
	

@Provides
public static Cell provideCell(Node node, 
							   @Named("SimpleInstance") Provider<Cell> providerCell, 
							   @Named("ComplexInstance") Provider<ComplexCell> providerComplexCell) {
	if (node instanceof Element) {
		System.err.println("Node:"+node.getNodeName()+"[complex]");

		return providerComplexCell.get();
	
	} else { 
		System.err.println("Node:"+node.getNodeName()+"[simple]");
	
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
											   Attributes<Cell> attributes) {
	return new ComplexCell(u, name, desc, value, cm, children, attributes);
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
	
	String value = node.getNodeValue();
	
	return (value!=null)? value : DEFAULT_VALUE;	//TODO: how to handle empty values properly
	
}


// bear in mind we can do element() instead of node if we need it, as it holds type information
@Provides
public static Composite<Cell> childrenFrom(Node node, URI uri, CellModel cellModel) {
	
 	if (!node.hasChildNodes()) {	// base case, save some memory on the list
	
		return new OrderedMap<Cell>(0);	

	}

	// recursive case
	Composite<Cell> children = new OrderedMap<Cell>(node.getChildNodes().getLength());
	int nodeIndex = 0;
	// this works for now, but some day it will come back with a vengeance :)
	NodeList nodeList = ((Element)node).getElementsByTagName("*");	//TODO: use node and detect text as values as this gives you all descendants
	for (int i=0; i<nodeList.getLength();i++) {						// or use normalise (SLOW CODE!)
		
		Element childElem = (Element) nodeList.item(i);	// we know it's element but this is not guaranteed not to change
		if (childElem.getParentNode()==node) {			// only handle the current element children!!! (SLOW!)
			String childName = childElem.getTagName();	
			CellModel childCellModel = findChildWithName(cellModel, childName);
			URI childURI = cellURI(uri, cellModel, childName);
			Cell childCell = DaggerCellComponent.builder()
													.withURI(childURI)
													.fromNode(childElem)
													.withCellModel(childCellModel)
													.builder()
													.createCell();
			
			children.addChild(childName+"["+nodeIndex+"]", childCell);
			nodeIndex++;
		
		}
		
	}

	return children;
	
}


@Provides
public static Attributes<Cell> attributesFrom(Node node, URI uri, CellModel cellModel) {
	
	if (!node.hasAttributes()) {	// base case, save some memory on the list returning a zero-sized one
		
		return new OrderedMap<Cell>(0);
	}
	
	if (cellModel.isSimple()) {
		log.error("CellModel '{}' does not allow attributes but the elem does", cellModel.getName());
		throw new RuntimeException("Element and model attribute mismatch", new IllegalArgumentException());
	}
	
	// recursive case
	OrderedMap<Cell> attributes = new OrderedMap<Cell>(node.getAttributes().getLength());
	NamedNodeMap elemAttributes = node.getAttributes();
	for (int i=0; i<elemAttributes.getLength(); i++) {
		
		Node attribute = elemAttributes.item(i);
		String attributeName = attribute.getNodeName();	// the root element may have xml namespace stuff
		if (!(attributeName.startsWith("xmlns:") || attributeName.startsWith("xsi:") )) {
			CellModel attributeCellModel = findAttributeWithName(cellModel, attributeName);
			URI childURI = cellURI(uri, cellModel, attributeName);
	
			Cell attributeCell = DaggerCellComponent.builder()
														.withURI(childURI)
														.fromNode(attribute)
														.withCellModel(attributeCellModel)
														.builder()
														.createCell();
			attributes.addChild(attributeName, attributeCell);
		}
	}
	
	return attributes;
}


private static CellModel findChildWithName(CellModel cellModel, String childName) {
	
	ComplexCellModel effectiveCellModel = (cellModel.isReference()) ? 
											cellModel.asReference().reference().asComplex() 
											: cellModel.asComplex();
	
	Optional<CellModel> matchedChild = effectiveCellModel.children()
															.asList()
															.stream()
															.filter(cm -> cm.getName().equals(childName))
															.findFirst();
	if (!matchedChild.isPresent()) {
		log.error("Elem '{}' could not match any children of '{}'", childName, cellModel.getName());
		throw new RuntimeException("Node and model mismatch", new IllegalArgumentException());
	}
	
	return matchedChild.get();
	
}


private static URI cellURI(URI uri, CellModel cellModel, String childName) throws RuntimeException {

	try {
		return new URI(uri+"/"+childName);
	} catch (URISyntaxException e) {
		log.error("Child '{}' could not build uri of '{}'", childName, cellModel.getName());
		throw new RuntimeException("Wrong uri for cell", e);
	}
	
}


private static CellModel findAttributeWithName(CellModel cellModel, String attributeName) {

	ComplexCellModel effectiveCellModel = (cellModel.isReference()) ? 
			cellModel.asReference().reference().asComplex() 
			: cellModel.asComplex();
	
	Optional<CellModel> matchedAttribute = effectiveCellModel.attributes()
																.asList()
																.stream()
																.filter(cm -> cm.getName().equals(attributeName))
																.findFirst();
	if (!matchedAttribute.isPresent()) {
		log.error("Elem '{}' could not match any attribute of '{}'", attributeName, cellModel.getName());
		throw new RuntimeException("Node and model attribute mismatch", new IllegalArgumentException());
	}
	
	return matchedAttribute.get();

}


}
