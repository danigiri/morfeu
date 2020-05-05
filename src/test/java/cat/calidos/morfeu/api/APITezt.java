/*
 *    Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.Tezt;
import cat.calidos.morfeu.webapp.GenericHttpServlet;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class APITezt extends Tezt {

public static final String WEBAPP_BASE_URL = "webapp-prefix";
protected static String DEFAULT_WEBAPP_BASE_URL = DEFAULT_URL+"/dyn/";
protected CloseableHttpClient client;
protected String webappPrefix;
protected String pathPrefix = "target/test-classes/test-resources/";


@Before
public void setup() {

	client = HttpClients.createDefault();
	webappPrefix = getConfigurationVariable(WEBAPP_BASE_URL, DEFAULT_WEBAPP_BASE_URL);

}


@After
public void teardown() throws IOException {

	if (client!=null) {
		client.close();
	}

}


public InputStream fetchRemoteInputStreamFrom(String location)	throws Exception {

	String uri = webappPrefix+location;
	System.err.println("Fetching remote input stream from '"+uri+"'");
	URI u = new URI(uri);
	HttpGet request = new HttpGet(u);

	return client.execute(request).getEntity().getContent();

}


public InputStream postToRemote(String location, String content) throws Exception {

	String uri = webappPrefix+location;
	System.err.println("Posting to remote '"+uri+"'");
	URI u = new URI(uri);
	HttpPost request = new HttpPost(u);
	StringEntity params = new StringEntity(content, Config.DEFAULT_CHARSET);
	params.setContentType(GenericHttpServlet.URLENCODED);
	request.setEntity(params);

	return client.execute(request).getEntity().getContent();

}


}
