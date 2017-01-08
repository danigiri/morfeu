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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import dagger.producers.ProductionComponent.Builder;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class HttpRequesterModule {

protected String uriString;

public HttpRequesterModule(String uri) {
	this.uriString = uri;
}

@Produces 
public HttpGet produceRequest() throws URISyntaxException {
	URI uri = new URI(uriString);
	return new HttpGet(uri);
}


@Produces
public InputStream fetchHttpData(CloseableHttpClient client, HttpGet request) throws UnsupportedOperationException, ClientProtocolException, IOException {
	
			try {
				 return client.execute(request)
						 .getEntity()
						 .getContent();
			} finally {
				if (client!=null) {
						client.close();
				}
			}
	
}

}
