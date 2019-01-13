/*
 *    Copyright 2018 Daniel Giribet
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

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.model.transform.injection.StringFormatModule;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.utils.LocalSaver;
import cat.calidos.morfeu.utils.Saver;
import cat.calidos.morfeu.utils.injection.SaverModule;
import cat.calidos.morfeu.utils.injection.HttpClientModule;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;
import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={ContentParserModule.class, StringToParsedModule.class, StringFormatModule.class,
								SaverModule.class, ModelModule.class, ValidatorModule.class,
								CellModelsFilterModule.class, ListeningExecutorServiceModule.class})
public interface ContentSaverParserComponent { //FIXME: this is probably a subcomponent of the content saver?

ListenableFuture<Validable> validator() throws FetchingException, ConfigurationException, ParsingException;
ListenableFuture<Composite<Cell>> content() throws ParsingException, TransformException;
ListenableFuture<Saver> saver() throws SavingException;

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder from(@Named("Content") String content);
	@BindsInstance Builder to(@Named("DestinationContentURI") URI u);
	@BindsInstance Builder having(@Named("ContentURI") URI u);
	@BindsInstance Builder model(@Named("ModelURI") URI u);
	@BindsInstance Builder withModelFetchedFrom(@Named("FetchableModelURI") URI u);

	ContentSaverParserComponent build();

}

}
