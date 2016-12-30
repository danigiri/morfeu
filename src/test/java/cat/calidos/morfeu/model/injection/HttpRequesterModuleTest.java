package cat.calidos.morfeu.model.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.net.URISyntaxException;
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

public class HttpRequesterModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

@Mock CloseableHttpClient httpClient;
@Mock CloseableHttpResponse response;
private URI uri;
private HttpGet request;
private ListeningExecutorService executor;

@Before
public void setup() throws Exception {

	uri = new URI("http://www.cisco.com");
	request = HttpClientModule.produceRequest(uri);
	executor = ListeningExecutorServiceModule.executor();
}


@After
public void teardown() {
	executor.shutdownNow();
}


@Test
public void testGetchHttpData() throws Exception {

	when(httpClient.execute(request)).thenReturn(response);
	
	HttpRequesterModule httpRequester = new HttpRequesterModule(executor, httpClient);
	ListenableFuture<HttpResponse> dataFuture = httpRequester.fetchHttpData(request);
	HttpResponse httpResponse = dataFuture.get();
	
	assertEquals(response, httpResponse);
	verify(httpClient, times(1)).close();	

}


@Test
public void testFaultyGetchHttpData() throws Exception {

	HttpGet request = HttpClientModule.produceRequest(uri);
	when(httpClient.execute(request)).thenThrow(new ClientProtocolException("Bad request type"));

	HttpRequesterModule httpRequester = new HttpRequesterModule(executor, httpClient);
	ListenableFuture<HttpResponse> dataFuture = httpRequester.fetchHttpData(request);
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