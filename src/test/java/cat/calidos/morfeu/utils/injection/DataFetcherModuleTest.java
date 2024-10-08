package cat.calidos.morfeu.utils.injection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.problems.FetchingException;


public class DataFetcherModuleTest {

CloseableHttpClient		httpClient	= mock(CloseableHttpClient.class);
CloseableHttpResponse	response	= mock(CloseableHttpResponse.class);
HttpEntity				entity		= mock(HttpEntity.class);
InputStream				stream		= mock(InputStream.class);

private String uri = "http://www.foo.com";

@Test
public void testProduceRequest() throws Exception {

	HttpGet request = produceRequest();
	assertNotNull(request);
	assertEquals("GET", request.getMethod());

}


// @Test FIXME: this test hangs
public void testGetchHttpData() throws Exception {

	// FIXME: this is ridiculously exposing implementation details, is there no other way? move to
	// helper and reuse
	HttpGet request = produceRequest();
	// FIXME: create a fully fledged response and then the rest should be OK
	when(httpClient.execute(request)).thenReturn(response);
	when(response.getEntity()).thenReturn(entity);
	when(entity.getContent()).thenReturn(stream);

	DataFetcherModule httpRequester = new DataFetcherModule();
	InputStream streamResponse = httpRequester.fetchHttpData(httpClient, request);

	assertEquals(stream, streamResponse);
	verify(httpClient, times(1)).close();

}


@Test
public void testFaultyGetchHttpData() throws Exception {

	HttpGet request = produceRequest();
	when(httpClient.execute(request)).thenThrow(new ClientProtocolException("Bad request type"));

	DataFetcherModule httpRequester = new DataFetcherModule();
	try {
		httpRequester.fetchHttpData(httpClient, request);
		fail("Bad request should throw an execution exception");
	} catch (FetchingException e) {
		assertTrue(e.getMessage().contains("Bad request type"));
	}

	// connection should still closed even after exception
	verify(httpClient, times(1)).close();

}


private HttpGet produceRequest() throws URISyntaxException {

	DataFetcherModule module = new DataFetcherModule();
	return module.produceRequest(new URI(uri));

}

}
