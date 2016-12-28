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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

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
															ListeningExecutorService executorService, 
															CloseableHttpClient client) {
	
	return executorService.submit(new Callable<HttpResponse>() {
		@Override
		public HttpResponse call() throws Exception {
			try {
				return client.execute(request);
			} finally {
				if (client!=null) {
						client.close();
				}
			}
		}});
	
}

}
