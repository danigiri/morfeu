
// SAVER MODULE . JAVA

package cat.calidos.morfeu.utils.injection;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import dagger.producers.Producer;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.utils.LocalSaver;
import cat.calidos.morfeu.utils.POSTSaver;
import cat.calidos.morfeu.utils.Saver;

/** (We include the http client so the http post saver can be created)
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule(includes=HttpClientModule.class)
public class SaverModule {

protected final static Logger log = LoggerFactory.getLogger(SaverModule.class);


@Produces
public static Saver saver(@Named("DestinationContentURI") URI u, 
							Producer<LocalSaver> fileSaverProducer,
							Producer<POSTSaver> postSaverProducer) throws SavingException {

	String scheme = u.getScheme();
	log.debug("Trying to save to uri {} with scheme {}", u, scheme);

	try {
		return scheme.startsWith("file") ? fileSaverProducer.get().get() : postSaverProducer.get().get();
	} catch (Exception e) {
		throw new SavingException("Could not provide an appropriate saver for '"+u+"'", e);
	}

}


@Produces
public static LocalSaver fileSaver(@Named("DestinationPath") String path, @Named("EffectiveContent") String content) {
	return new LocalSaver(path, content);
}


@Produces @Named("DestinationPath")
public static String destinationPathFrom(@Named("DestinationContentURI") URI u) {

	String uriString = u.toString();
	String path = uriString.substring("file://".length(), uriString.length());
	log.info("Saving content to destination path '{}'", path);

	return path;

}


@Produces
public static POSTSaver postSaver(@Named("DestinationContentURI") URI u,
									Map<String, String> contentMap,
									CloseableHttpClient client) {
	return new POSTSaver(client, u, contentMap);
}


@Produces
public static Map<String, String> contentMap(@Named("DestinationContentURI") URI u,
												@Named("EffectiveContent") String content) {
	// by convention, we send two variables, though that can be easily changed

	Map<String, String> contentMap = new HashMap<String, String>(2);
	contentMap.put("uri", u.toString());
	contentMap.put("content", content);

	return contentMap;

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
