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

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;
import cat.calidos.morfeu.model.Validable;
import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={ContentParserModule.class, ModelModule.class, ListeningExecutorServiceModule.class})
public interface ContentParserComponent {

ListenableFuture<Validable> validator() throws FetchingException, ConfigurationException, ParsingException;

@ProductionComponent.Builder
interface Builder {
	@BindsInstance Builder forContent(@Named("FetchableContentURI") URI u);
	@BindsInstance Builder withModel(@Named("ModelURI") URI u);
	@BindsInstance Builder modelFetchableFrom(@Named("FetchableModelURI") URI u);
	ContentParserComponent build();
}

}
