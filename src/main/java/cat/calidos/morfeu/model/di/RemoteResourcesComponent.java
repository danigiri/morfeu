/*
 *    Copyright 2016 Daniel Giribet
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

package cat.calidos.morfeu.model.di;

import java.net.URI;

import org.apache.http.HttpResponse;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.producers.ProductionComponent;


/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules ={RemoteResourcesModule.class, ListeningExecutorServiceModule.class})
public interface RemoteResourcesComponent {

ListenableFuture<HttpResponse> fetchHttpData();

@ProductionComponent.Builder
interface Builder {
	Builder uri(URI uri);
	RemoteResourcesComponent build();
}

}
