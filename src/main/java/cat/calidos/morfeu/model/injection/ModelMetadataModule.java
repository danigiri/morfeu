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

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.xsom.XSAnnotation;

import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;

/** Model Metadata helper module to enrich the model definitions with useful information
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ModelMetadataModule {

//reverse breadth-first search, as the dom annotation parser adds all sibling nodes in reverse order
@Provides
public String provideContentOfAnnotation(LinkedList<Node> annotationNodes, @Named("tag") String tag) {
	
	String content = null;

	while (annotationNodes.size()>0 && content==null) {
		
		Node currentNode = annotationNodes.pop();
		if (currentNode.getNodeName().equals(tag)) {
			content = currentNode.getTextContent();
		} else {
			if (currentNode.hasChildNodes()) {
				NodeList childNodes = currentNode.getChildNodes();
				for (int i=0;i<childNodes.getLength();i++) {
					annotationNodes.add(childNodes.item(i));
				}
			}
		}
		
	}
	
	// this will probably have lots of leading/trailing whitespace stuff, we'll leave that outside our scope
	return (content==null) ? ModelMetadataComponent.UNDEFINED_VALUE: content;
	
}


@Provides
LinkedList<Node> annotationNode(@Nullable XSAnnotation annotation) {
	
	LinkedList<Node> annotationNodes = new LinkedList<Node>();
	if (annotation!=null) {
		Node annotationRootNode = (Node)annotation.getAnnotation(); // as we are using the DomAnnotationParserFactory from XSOM
		annotationNodes.add(annotationRootNode);
	}
	
	return annotationNodes;
	
}


}
