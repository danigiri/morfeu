// CONTENT PARSER COMPONENT . JAVA

package cat.calidos.morfeu.model.injection;

import java.net.URI;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.utils.injection.MapperModule;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;


/**
 * With the URIs, fetchable URIs (usually absolute) and an optional set of filter swe get the
 * content and validator
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = { ContentParserModule.class, URIToFetchedContentModule.class,
		FetchedToParsedModule.class, MapperModule.class, ModelModule.class,
		CellModelsFilterModule.class, ValidatorModule.class, XMLDocumentBuilderModule.class,
		ListeningExecutorServiceModule.class })
public interface ContentParserComponent {

ListenableFuture<Validable> validator()
		throws FetchingException, ConfigurationException, ParsingException;

ListenableFuture<Composite<Cell>> content()
		throws FetchingException, ParsingException, TransformException;

//@formatter:off
@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder content(@Named("ContentURI") URI u);
	@BindsInstance Builder fetchedContentFrom(@Named("FetchableContentURI") URI u);
	@BindsInstance Builder filters(@Nullable @Named("Filters") String filters);
	@BindsInstance Builder model(@Named("ModelURI") URI u);
	@BindsInstance Builder withModelFetchedFrom(@Named("FetchableModelURI") URI u);

	ContentParserComponent build();

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
