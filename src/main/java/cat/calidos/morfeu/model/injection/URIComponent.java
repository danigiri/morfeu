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
import java.net.URISyntaxException;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.problems.FetchingException;
import dagger.BindsInstance;
import dagger.Component;
import dagger.producers.ProductionComponent;

/**
* @author daniel giribet
* Note we're using a producer as we can declare explicit exceptions
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={URIModule.class, ListeningExecutorServiceModule.class})
public interface URIComponent {

ListenableFuture<URI> uri() throws FetchingException;

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder from(String uri);
	URIComponent builder();
	
}


}
