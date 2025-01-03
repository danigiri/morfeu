// CONTENT PARSER SUBCOMPONENT . JAVA

package cat.calidos.morfeu.model.injection;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.producers.ProductionSubcomponent;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionSubcomponent(modules = { ContentParserModule.class, URIToFetchedContentModule.class,
		FetchedToParsedModule.class, ModelModule.class, CellModelsFilterModule.class,
		ValidatorModule.class, XMLDocumentBuilderModule.class,
		ListeningExecutorServiceModule.class })
public interface ContentParserSubcomponent {

ListenableFuture<Validable> validator()
		throws FetchingException, ConfigurationException, ParsingException;

ListenableFuture<Composite<Cell>> content() throws ParsingException;

//@formatter:off
@ProductionSubcomponent.Builder
interface Builder {

ContentParserSubcomponent builder();
}
//@formatter:on

}

/*
 * Copyright 2024 Daniel Giribet
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
