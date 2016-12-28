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
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import dagger.producers.Production;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class RemoteResourcesModule {

//TODO: this is stateful and non-reentrant
@Produces
public static CloseableHttpClient produceHttpClient() {
	return HttpClients.createDefault();	
}


@Produces
public static HttpGet produceRequest(URI uri) {
	return new HttpGet(uri);
}


@Produces
public static ListenableFuture<HttpResponse> fetchHttpData(	HttpGet request, 
															ListeningExecutorService s, 
															CloseableHttpClient client) {
	
	return s.submit(new Callable<HttpResponse>() {
		@Override
		public HttpResponse call() throws Exception {
			try {
//				HttpGet request = new HttpGet(uri);
				return client.execute(request);
			} finally {
				if (client!=null) {
						client.close();
				}
			}
		}});
	
}

}


@Module
final class ListeningExecutorServiceModule {
	
	@Provides
	@Production
	public static ListeningExecutorService executor() {
		// Following the Dagger 2 official docs, this private executor pool should be efficient for just the HTTP stuff
		return MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
	}
	
}
