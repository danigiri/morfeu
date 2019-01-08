
// SAVER MODULE . JAVA

package cat.calidos.morfeu.utils.injection;

import java.net.URI;

import javax.inject.Named;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.utils.LocalSaver;
import cat.calidos.morfeu.utils.POSTSaver;
import cat.calidos.morfeu.utils.Saver;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class SaverModule {

protected final static Logger log = LoggerFactory.getLogger(SaverModule.class);


@Produces
public static Saver saver(@Named("DestinationContentURI") URI u, 
							Provider<LocalSaver> fileSaverProvider,
							Provider<POSTSaver> postSaverProvider) {
	return u.getScheme().startsWith("file://") ? fileSaverProvider.get() : postSaverProvider.get();
}


@Produces
public static LocalSaver fileSaver(@Named("DestinationPath") String path, @Named("EffectiveContent") String content) {
	return new LocalSaver(path, content);
}


@Produces @Named("DestinationPath")
public static String destinationPathFrom(@Named("DestinationContentURI") URI u) throws SavingException {
	

	String uriString = u.toString();
	String path = uriString.substring("file://".length(), uriString.length());
	log.info("Saving content to destination path '{}'", path);

	return path;

}


@Produces
public static POSTSaver postSaver(@Named("DestinationContentURI") URI u, @Named("EffectiveContent") String content) {
	return new POSTSaver(u, content);
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
