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
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ValidationException;
import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={ModelModule.class, ParserModule.class, ListeningExecutorServiceModule.class})
public interface ModelComponent {

ListenableFuture<Model> model() throws ValidationException, ExecutionException;

@ProductionComponent.Builder
interface Builder {
	@BindsInstance Builder identifiedBy(@Named("ModelURI") URI u);
	@BindsInstance Builder fromFetchable(@Named("FetchableModelURI") URI u);
	ModelComponent build();
}

}
