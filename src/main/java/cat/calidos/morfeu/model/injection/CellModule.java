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

import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
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
public static Cell provideCell(Element elem, 
							   @Named("SimpleInstance") Provider<Cell> providerCell, 
							   @Named("ComplexInstance") Provider<ComplexCell> providerComplexCell) {

	if (elem.hasAttributes() || elem.hasChildNodes()) {

		return providerComplexCell.get();
	
	} else { 
	
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
											   Composite<Cell> children){ 
											   //Attributes<Cell> attributes) {
	return new ComplexCell(u, name, desc, value, cm, children, null);
}


@Provides @Named("name") 
public static String nameFrom(Element elem) {
	return elem.getNodeName();
}


@Provides @Named("desc") 
public static String desc() {
	return DEFAULT_DESC;	
}


@Provides @Named("value") 
public static String valueFrom(Element elem) {
	
	String value = elem.getNodeValue();
	
	return (value!=null)? value : DEFAULT_VALUE;	//TODO: how to handle empty values properly
	
}


// bear in mind we can do element() instead of node if we need it, as it holds type information
@Provides
public static Composite<Cell> childrenFrom(Element elem, URI uri, CellModel cellModel) {
	
 	if (!elem.hasChildNodes()) {
	
		return new OrderedMap<Cell>(0);	// base case, save some memory

	}
// it may be that we have attributes and no children
//	if (cellModel.isSimple()) {
//		log.error("Complex node '{}' and simple model mismatch on validated content!?!?", node.getNodeName());
//		throw new RuntimeException("Complex node and simple model mismatch", new NullPointerException());
//	}

	// recursive case
	Composite<Cell> children = new OrderedMap<Cell>(elem.getChildNodes().getLength());
	//NodeList nodeList = elem.getChildNodes();
	NodeList nodeList = elem.getElementsByTagName("*");	//TODO: use node and detect text as values as this gives you all descendants
	for (int i=0; i<nodeList.getLength();i++){
		Element childElem = (Element) nodeList.item(i);	// we know it's element but this is not guaranteed not to change
		if (childElem.getParentNode()!=elem) {			// only handle the current element children
			break; 
		}
		String childName = childElem.getNodeName();
		Optional<CellModel> matchedCellModel = ComplexCellModel.from(cellModel)
																.children()
																.asList()
																.stream()
																.filter(cm -> cm.getName().equals(childName))
																.findFirst();
		if (!matchedCellModel.isPresent()) {
			log.error("Node '{}' could not match any children of '{}'", childName, cellModel.getName());
			throw new RuntimeException("Node and model mismatch", new NullPointerException());
		}
		CellModel childCellModel = matchedCellModel.get();
		URI childURI;
		try {
			childURI = new URI(uri+"/"+childName);
		} catch (URISyntaxException e) {
			log.error("Node '{}' could not build uri of '{}'", childName, cellModel.getName());
			throw new RuntimeException("Wrong uri for child node", e);
		}
		Cell childCell = DaggerCellComponent.builder()
												.withURI(childURI)
												.fromElement(childElem)
												.withCellModel(childCellModel)
												.builder()
												.createCell();
		children.addChild(childName+"["+i+"]", childCell);
	
	}

	return children;
	
}

}
