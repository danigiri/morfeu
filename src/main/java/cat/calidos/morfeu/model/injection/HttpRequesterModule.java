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

package cat.calidos.morfeu.model.injection;

import java.io.InputStream;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class HttpRequesterModule {

protected ListeningExecutorService executorService;
protected CloseableHttpClient client;
protected HttpGet request;

@Inject
public HttpRequesterModule(ListeningExecutorService executorService, CloseableHttpClient client, HttpGet request) {
	this.executorService = executorService;
	this.client = client;
	this.request = request;
}


@Produces
public ListenableFuture<InputStream> fetchHttpData() {
	
	return executorService.submit(new Callable<InputStream>() {
		@Override
		public InputStream call() throws Exception {
			try {
				 return client.execute(request)
						 .getEntity()
						 .getContent();
			} finally {
				if (client!=null) {
						client.close();
				}
			}
		}});
	
}

}
