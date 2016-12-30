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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.model.injection.ListeningExecutorServiceModule;
import cat.calidos.morfeu.model.injection.HttpClientModule;

/**
* @author daniel giribet
*////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class HttpClientModuleTest {


@Test
public void testProduceHttpClient() {
	assertNotNull(HttpClientModule.produceHttpClient());
}

@Test
public void testProduceRequest() throws Exception {

	URI uri = new URI("http://www.foo.com");
	HttpGet request = HttpClientModule.produceRequest(uri);
	assertNotNull(request);
	assertEquals("GET", request.getMethod());

}

}
