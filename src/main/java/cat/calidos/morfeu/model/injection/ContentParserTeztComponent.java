// CONTENT PARSER TEZT COMPONENT . JAVA
package cat.calidos.morfeu.model.injection;

import java.net.URI;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.utils.injection.MapperModule;
import cat.calidos.morfeu.utils.injection.HttpClientModule;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;


/**
 * Content parser component used in testing
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = { ContentParserModule.class, URIToFetchedContentModule.class,
		FetchedToParsedModule.class, HttpClientModule.class, MapperModule.class, ModelModule.class,
		CellModelsFilterModule.class, ValidatorModule.class, XMLDocumentBuilderModule.class,
		ListeningExecutorServiceModule.class })
public interface ContentParserTeztComponent {

ListenableFuture<org.w3c.dom.Document> parsedXMLDocument();

// apologies for the reckless interface duplication, options:
// a) adding a dom public method to the content parser component, exposing internals
// b) heavily use internals of the content parser to build the dom in the cell module test
// c) have an interface extend contentparsercomponent (not supported by dagger 2)
// d) duplicate the builder interface here - chosen option as it duplicates interface, not
// implementation *)
//@formatter:off
@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder content(@Named("ContentURI") URI u);
	@BindsInstance Builder fetchedContentFrom(@Named("FetchableContentURI") URI u);
	@BindsInstance Builder filters(@Nullable @Named("Filters") String filters);
	@BindsInstance Builder model(@Named("ModelURI") URI u);
	@BindsInstance Builder withModelFetchedFrom(@Named("FetchableModelURI") URI u);

	ContentParserTeztComponent build();

}
//@formatter.on

}
/*
 *    Copyright 2024 Daniel Giribet
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
