package cat.calidos.morfeu.model.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.apache.http.HttpEntity;
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

public class HttpRequesterModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

@Mock CloseableHttpClient httpClient;
@Mock CloseableHttpResponse response;
@Mock HttpEntity entity;
@Mock InputStream stream;

private String uri = "http://www.foo.com";

@Test
public void testProduceRequest() throws Exception {

	HttpGet request = produceRequest();
	assertNotNull(request);
	assertEquals("GET", request.getMethod());

}


@Test
public void testGetchHttpData() throws Exception {

	//FIXME: this is ridiculously exposing implementation details, is there no other way?
	HttpGet request = produceRequest();
	when(httpClient.execute(request)).thenReturn(response);
	when(response.getEntity()).thenReturn(entity);
	when(entity.getContent()).thenReturn(stream);
	
	HttpRequesterModule httpRequester = new HttpRequesterModule(uri);
	InputStream streamResponse =  httpRequester.fetchHttpData(httpClient, request);
	
	assertEquals(stream, streamResponse);
	verify(httpClient, times(1)).close();	

}


@Test
public void testFaultyGetchHttpData() throws Exception {

	HttpGet request = produceRequest();
	when(httpClient.execute(request)).thenThrow(new ClientProtocolException("Bad request type"));

	HttpRequesterModule httpRequester = new HttpRequesterModule(uri);
	try {
		httpRequester.fetchHttpData(httpClient, request);
		fail("Bad request should throw an execution exception");
	} catch (ClientProtocolException e) {
		assertEquals("Bad request type", e.getMessage());
	}

	// connection was still closed even after exception!
	verify(httpClient, times(1)).close();	

}

// kept as reference for working vanilla injection (no submodules or fancy stuff)
//@Test
//public void testInjection() throws Exception {
//	
//	HttpRequesterComponent component = DaggerHttpRequesterComponent.builder()
//		.httpClientModule(new HttpClientModule())
//		.httpRequesterModule(new HttpRequesterModule(uri))
//		.build();
//	InputStream stream = component.fetchHttpData().get();
//
//}


private HttpGet produceRequest() throws URISyntaxException {

	HttpRequesterModule module = new HttpRequesterModule(uri);
	HttpGet request = module.produceRequest();
	return request;
}

}