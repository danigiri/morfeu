// DATA POSTER MODULE . JAVA

package cat.calidos.morfeu.utils.injection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.PostingException;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class DataPosterModule {

protected final static Logger log = LoggerFactory.getLogger(DataPosterModule.class);

@Produces
InputStream postHttpData(	CloseableHttpClient client,
							HttpPost request)
		throws PostingException {

	log
			.trace(
					"Posting http data [{} bytes] to {}",
					request.getEntity().getContentLength(),
					request.getURI());

	try {
		// we want to close right now so we fetch all the content and close the input stream
		InputStream content = client.execute(request).getEntity().getContent();

		return IOUtils.toBufferedInputStream(content);

	} catch (Exception e) {
		throw new PostingException("Problem posting http data", e);
	} finally {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				throw new PostingException("Problem closing client when posting http data", e);
			}
		}
	}

}


@Produces
HttpPost request(	URI uri,
					HttpEntity entity) {

	HttpPost request = new HttpPost(uri);
	request.setEntity(entity);

	return request;

}


@Produces
HttpEntity entity(List<NameValuePair> nameValueParis) {
	return EntityBuilder.create().setParameters(nameValueParis).build();
}


@Produces
List<NameValuePair> nameValueParis(Map<String, String> data) {

	List<NameValuePair> nvps = new ArrayList<NameValuePair>(data.size());
	data.entrySet().forEach(e -> nvps.add(new BasicNameValuePair(e.getKey(), e.getValue())));

	return nvps;

}

}

/*
 * Copyright 2019 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
