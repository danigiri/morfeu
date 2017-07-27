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

import javax.inject.Named;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
public String produceContentOfAnnotation(Node annotationNode, @Named("tag") String tag) {
	
	String content = "";
	LinkedList<Node> pending = new LinkedList<Node>();
	pending.add(annotationNode);
	while (pending.size()>0 && content.length()==0) {
		
		Node currentNode = pending.pop();
		if (currentNode.getNodeName().equals(tag)) {
			content = currentNode.getTextContent();
		} else {
			if (currentNode.hasChildNodes()) {
				NodeList childNodes = currentNode.getChildNodes();
				for (int i=0;i<childNodes.getLength();i++) {
					pending.add(childNodes.item(i));
				}
			}
		}
		
	}
	
	// this will probably have lots of leading/trailing whitespace stuff, we'll leave that outside our scope
	return content;
	
}


}
