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

package cat.calidos.morfeu.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import cat.calidos.morfeu.model.di.ListeningExecutorServiceModule;
import cat.calidos.morfeu.model.di.RemoteResourcesModule;

/**
* @author daniel giribet
*////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class RemoteResourcesModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

@Mock CloseableHttpClient httpClient;
@Mock CloseableHttpResponse response;
private ListeningExecutorService executor;
private URI uri;

private RemoteResourcesModule remoteResources;

@Before
public void setup() throws Exception {
	
	executor = ListeningExecutorServiceModule.executor();
	uri = new URI("http://www.foo.com");
	remoteResources = new RemoteResourcesModule(executor, httpClient);

}


@After
public void teardown() {
	executor.shutdownNow();
}


@Test
public void testProduceHttpClient() {
	assertNotNull(RemoteResourcesModule.produceHttpClient());
}


@Test
public void testProduceRequest() {

	HttpGet request = remoteResources.produceRequest(uri);
	assertNotNull(request);
	assertEquals("GET", request.getMethod());

}


@Test
public void testGetchHttpData() throws Exception {

	HttpGet request = remoteResources.produceRequest(uri);
	when(httpClient.execute(request)).thenReturn(response);

	ListenableFuture<HttpResponse> dataFuture = remoteResources.fetchHttpData(request);
	HttpResponse httpResponse = dataFuture.get();
	
	assertEquals(response, httpResponse);
	verify(httpClient, times(1)).close();	

}


@Test
public void testFaultyGetchHttpData() throws Exception {

	HttpGet request = remoteResources.produceRequest(uri);
	when(httpClient.execute(request)).thenThrow(new ClientProtocolException("Bad request type"));
	
	ListenableFuture<HttpResponse> dataFuture = remoteResources.fetchHttpData(request);
	try {
		dataFuture.get();
		fail("Bad request should throw an execution exception");
	} catch (ExecutionException e) {
		Throwable cause = e.getCause();
		assertEquals(ClientProtocolException.class, cause.getClass());
		assertEquals("Bad request type", cause.getMessage());
	}

	// connection was still closed even after exception!
	verify(httpClient, times(1)).close();	

}


}
