// URI TO FETCHED MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.injection.DaggerDataFetcherComponent;


/**
 * Given an URI, fetch the content
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class URIToFetchedContentModule {

protected final static Logger log = LoggerFactory.getLogger(URIToFetchedContentModule.class);

@Produces @Named("FetchedRawContent")
public static InputStream fetchedRawContent(@Named("FetchableContentURI") URI uri,
											@Named("AbsoluteFileRawContent") Producer<InputStream> absoluteFileInputStream,
											@Named("RelativeFileRawContent") Producer<InputStream> relativeFileInputStream,
											@Named("HttpRawContent") Producer<InputStream> httpRequestInputStream)
		throws FetchingException {
	// this method selects which way is the right one to fetch the content and invokes the right
	// producer accordingly.
	// If uri is absolute we retrieve it, otherwise we assume it's a local relative file

	try {
		if (uri.isAbsolute()) {
			String scheme = uri.getScheme();
			if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
				return httpRequestInputStream.get().get();
			} else {
				return absoluteFileInputStream.get().get();
			}
		} else {
			return relativeFileInputStream.get().get();
		}
	} catch (InterruptedException | ExecutionException e) {
		log.error("Could not fetch '{}' ({})", uri, e.getMessage());
		throw new FetchingException("Problem when fetching '" + uri + "'", e);
	}

}


@Produces @Named("AbsoluteFileRawContent")
public static InputStream absoluteFileInputStream(@Named("FetchableContentURI") URI uri)
		throws FetchingException {
	try {
		log.info("Fetching absolute content uri '{}' to parse", uri);
		return IOUtils
				.toInputStream(
						IOUtils.toString(uri, Config.DEFAULT_CHARSET),
						Config.DEFAULT_CHARSET);
	} catch (IOException e) {
		log.error("Could not fetch absolute url '{}' ({})", uri, e.getMessage());
		throw new FetchingException("Problem when fetching '" + uri + "'", e);
	}
}


@Produces @Named("RelativeFileRawContent")
public static InputStream relativeFileInputStream(@Named("FetchableContentURI") URI uri)
		throws FetchingException {
	log.info("Fetching relative content uri '{}' to parse, assuming file", uri);
	try {
		return FileUtils.openInputStream(new File(uri.toString()));
	} catch (IOException e) {
		log.error("Could not fetch relative uri '{}' ({})", uri, e.getMessage());
		throw new FetchingException("Problem when fetching '" + uri + "'", e);
	}
}


@Produces @Named("HttpRawContent")
public static InputStream httpRequestInputStream(	@Named("FetchableContentURI") URI uri,
													CloseableHttpClient client)
		throws FetchingException {

	try {
		return DaggerDataFetcherComponent
				.builder()
				.forURI(uri)
				.withClient(client)
				.build()
				.fetchData()
				.get();
	} catch (InterruptedException | ExecutionException | FetchingException e) {
		log.error("Could not fetch url '{}' ({})", uri, e.getMessage());
		throw new FetchingException("Problem when fetching '" + uri + "'", e);
	}
}

}

/*
 * Copyright 2024 Daniel Giribet
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
