package cat.calidos.morfeu.utils;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.PostingException;
import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.utils.injection.DaggerDataPosterComponent;
import cat.calidos.morfeu.utils.injection.DataPosterComponent;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class POSTSaver implements Saver {

protected final static Logger log = LoggerFactory.getLogger(POSTSaver.class);

private URI destination;
private DataPosterComponent poster;

private String response;


public POSTSaver(CloseableHttpClient client, URI destination, Map<String, String> content) {

	this.destination = destination;
	this.poster = DaggerDataPosterComponent.builder()
											.forURI(destination)
											.withClient(client)
											.andData(content)
											.build();
}


@Override
public void save() throws SavingException {

	try {
		log.info("Saving to ", destination);
		InputStream responseInputStream = poster.postData().get();
		response = IOUtils.toString(responseInputStream, Config.DEFAULT_CHARSET);
	} catch (Exception e) {
		throw new SavingException("Problem when posting save data to "+destination, e);
	}

}


public Optional<String> getResponse() {
	return Optional.ofNullable(response);
}

}

/*
 *    Copyright 2019 Daniel Giribet
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

