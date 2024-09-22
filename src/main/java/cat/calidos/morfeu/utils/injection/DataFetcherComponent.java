// DATA FETCHER COMPONENT . JAVA

package cat.calidos.morfeu.utils.injection;

import java.io.InputStream;
import java.net.URI;

import javax.annotation.Nullable;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.problems.FetchingException;
import dagger.BindsInstance;
import dagger.producers.ProductionComponent;


@ProductionComponent(modules = {DataFetcherModule.class, ListeningExecutorServiceModule.class})
public interface DataFetcherComponent {

ListenableFuture<InputStream> fetchData() throws FetchingException;

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder forURI(URI u);
	@BindsInstance Builder withClient(@Nullable CloseableHttpClient c);

	DataFetcherComponent build();

}

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

