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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import dagger.Lazy;
import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import dagger.producers.ProductionComponent.Builder;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class DataFetcherModule {


@Produces
public HttpGet produceRequest(URI uri) {
	return new HttpGet(uri);
}


@Produces
public ListenableFuture<InputStream> fetchData(URI uri,  
							 @Named("httpData") Producer<InputStream> httpData, 
							 @Named("fileData") Producer<InputStream> fileData ) 
									 throws UnsupportedOperationException, ClientProtocolException, IOException {
	if (uri.getScheme().equals("file")) {
		return fileData.get();
	} else {
		return httpData.get();
	}
}


@Produces @Named("httpData")
public InputStream fetchHttpData(CloseableHttpClient client, HttpGet request)
		throws IOException, UnsupportedOperationException, ClientProtocolException {

	try {
		 
		// we want to close right now so we fetch all the content and close the input stream
		InputStream content = client.execute(request)
				 .getEntity()
				 .getContent();
		InputStream fetchedData = IOUtils.toBufferedInputStream(content);
		 
		return fetchedData;
		 
	} finally {
		if (client!=null) {
				client.close();
		}
	}
	
}


@Produces @Named("fileData")
public InputStream fetchFileData(URI uri) throws IOException {
	
	return FileUtils.openInputStream(FileUtils.toFile(uri.toURL()));
	
	//return uri.toURL().openStream();
}


}
