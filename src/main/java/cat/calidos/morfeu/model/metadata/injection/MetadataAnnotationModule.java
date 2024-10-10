// METADATA ANNOTATION MODULE . JAVA

package cat.calidos.morfeu.model.metadata.injection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.xsom.XSAnnotation;

import dagger.Module;
import dagger.Provides;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class MetadataAnnotationModule {

protected final static Logger log = LoggerFactory.getLogger(MetadataAnnotationModule.class);

@Provides
List<Node> provideNodesTagged(	LinkedList<Node> annotationNodes,
								String tagExpr) {

	// the expression can be of two types (<nodename> or <nodename>@<attributename>)
	// in the case of node it's straightforward, in the case of looking for an attribute, we look
	// for the node name
	// but add the attribute value as output node
	log.trace("MetadataAnnotationModule::provideNodesTagged(tagExpr:'{}'", tagExpr);
	int attributeIndex = tagExpr.lastIndexOf("@");
	String tag = attributeIndex != -1 ? tagExpr.substring(0, attributeIndex) : tagExpr;

	// reverse breadth-first search, as the dom annotation parser adds all sibling nodes in reverse
	// order
	List<Node> content = new ArrayList<Node>();

	while (annotationNodes.size() > 0) {

		Node currentNode = annotationNodes.pop();
		if (currentNode == null) {
			// FIXME: is this a problem or is intrinsic from the annotations (like empty value XML
			// nodes)?
			log.trace(
					"************** pop spits out a nullpointer value when retrieving metadata for '{}'",
					tagExpr);
			continue;
		}
		String nodeName = currentNode.getNodeName();
		if (nodeName.equals(tag)) {
			if (attributeIndex == -1) { // looking for plain node and its value
				content.add(currentNode);
			} else { // looking for node but setting attribute
				Node value = currentNode.getAttributes()
						.getNamedItem(tagExpr.substring(attributeIndex + 1));
				if (value != null) { // if attribute does not actually exist in the metadata we
										// don't add
					content.add(value);
				}
			}
		} else {
			if (currentNode.hasChildNodes()) {
				try {
					NodeList childNodes = currentNode.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); i++) {
						annotationNodes.add(childNodes.item(i));
					}
				} catch (NullPointerException e) {
					// FIXME: this seems to happen randomly at application startup, and then it goes
					// away
					log.error(
							"getChildNodes kicks out a nullpointer when retrieving metadata for '{}'",
							tagExpr);
				}
			}
		}

	}

	return content;

}


@Provides
LinkedList<Node> annotationNode(@Nullable XSAnnotation annotation) {

	LinkedList<Node> annotationNodes = new LinkedList<Node>();
	if (annotation != null) {
		Node annotationRootNode = (Node) annotation.getAnnotation(); // as we are using the
																		// DomAnnotationParserFactory
																		// from XSOM
		annotationNodes.add(annotationRootNode);
	}

	return annotationNodes;

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
