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

package cat.calidos.morfeu.utils.injection;

import java.net.URI;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.utils.FileSaver;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class FileSaverModule {

protected final static Logger log = LoggerFactory.getLogger(FileSaverModule.class);
		
		
@Produces
public static FileSaver produceFileSaver(@Named("DestinationPath") String path, @Named("Content") String content) {
	return new FileSaver(path, content);
}


@Produces @Named("DestinationPath")
public static String destinationPathFrom(@Named("DestinationContentURI") URI u) throws SavingException {
	
	String scheme = u.getScheme();
	if (!scheme.equals("file")) {
		throw new SavingException("Scheme '"+scheme+"' is not supported");
	}
	String uriString = u.toString();
	String path = uriString.substring("file://".length(), uriString.length());
	log.info("Saving content to destination path '{}'", path);

	return path;

}

}
