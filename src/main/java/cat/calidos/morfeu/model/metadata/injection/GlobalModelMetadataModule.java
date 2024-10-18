// GLOBAL MODEL METADATA MODULE

package cat.calidos.morfeu.model.metadata.injection;

import java.net.URI;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.w3c.dom.Node;
import org.xml.sax.Locator;

import com.google.common.base.Functions;
import com.sun.xml.xsom.XSAnnotation;

import cat.calidos.morfeu.model.Metadata;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;


/**
 * Extra metadata that cannot be embedded in the cell model definitions due to being a reference or
 * other problems
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(includes = MetadataAnnotationModule.class)
public class GlobalModelMetadataModule {

@Produces
public static Map<URI, Metadata> provideGlobalModelMetadata(XSAnnotation annotation,
															@Named("ModelURI") URI uri) {

	List<Node> extraMetadataNodes = DaggerMetadataAnnotationComponent
			.builder()
			.from(annotation)
			.andTag("mf:metadata")
			.build()
			.values();

	// an XSAnnotation is just a wrapper for any kind of object, in this case a Node, so we give it
	// that
	return extraMetadataNodes
			.stream()
			.map(m -> DaggerModelMetadataComponent.builder().from(new XSAnnotation() {

			@Override
			public Object setAnnotation(Object o) {
				return null;
			}


			@Override
			public Locator getLocator() { return null; }


			@Override
			public Object getAnnotation() { return m; }
			}).withParentURI(uri).build().value())
			.collect(
					Collectors
							.toMap(
									Metadata::getURI,
									Functions.identity(),
									(	p1,
										p2) -> p1));
	// FIXME: sometimes we get a duplicate here, we should investigate at some point

}

}

/*
 * Copyright 2018 Daniel Giribet
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
