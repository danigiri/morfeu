// URICOMPONENT . JAVA

package cat.calidos.morfeu.utils.injection;

import java.net.URI;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.problems.FetchingException;


/**
 * @author daniel giribet Note we're using a producer as we can declare explicit exceptions
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = { URIModule.class, ListeningExecutorServiceModule.class })
public interface URIComponent {

ListenableFuture<URI> uri() throws FetchingException;

//@formatter:off
@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder from(String uri);

	URIComponent build();

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
