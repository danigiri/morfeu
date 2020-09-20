// SNIPPET COMPONENT . JAVA

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

/** Component to get cell data from a snippet (can be optionally filtered), needs the cellmodel (within th emodel
*	the model hierarchy). Very similar to the regular content parser component 
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={ContentParserModule.class, URIToParsedModule.class, MapperModule.class, 
								ModelModule.class, SnippetCellModelModule.class, CellModelsFilterModule.class, 
								XMLDocumentBuilderModule.class, ValidatorModule.class, 
								ListeningExecutorServiceModule.class})
public interface SnippetParserComponent {

ListenableFuture<Validable> validator() throws FetchingException, ConfigurationException, ParsingException;
ListenableFuture<Composite<Cell>> content() throws FetchingException, ParsingException, TransformException;

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder content(@Named("ContentURI") URI u);
	@BindsInstance Builder fetchedContentFrom(@Named("FetchableContentURI") URI u);
	@BindsInstance Builder filters(@Nullable @Named("Filters") String filters);
	@BindsInstance Builder modelFiltered(@Named("FilteredModelURI") URI u);
	@BindsInstance Builder withModelFetchedFrom(@Named("FetchableModelURI") URI u);

	SnippetParserComponent build();

}

}

/*
 *    Copyright 2019 Daniel Giribet
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in  compliance with the License.
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
