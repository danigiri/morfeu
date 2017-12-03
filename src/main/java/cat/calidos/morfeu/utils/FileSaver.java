/*
 *    Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.SavingException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FileSaver {

protected final static Logger log = LoggerFactory.getLogger(FileSaver.class);

private String destination;
private String content;


public FileSaver(String destination, String content) {

	this.destination = destination;
	this.content = content;

}


public void save() throws SavingException {

	File destinationFile = new File(destination);
	if (destinationFile.isDirectory()) {
		log.error("Cannot save to '{}' as it is a folder and not a file", destination);
		throw new SavingException("Could not save to '"+destination+"' as it is a folder and not a file");		
	}
	
	
	try {
		FileUtils.touch(destinationFile);
	} catch (IOException e) {
		log.error("Cannot save to '{}' as we cannot even touch it", destination);
		throw new SavingException("Could not save to '"+destination+"' as we cannot write to it", e);		
	}
	if (!destinationFile.canWrite()) {
		log.error("Cannot save to '{}' as we cannot write to it", destination);
		throw new SavingException("Could not save to '"+destination+"' as we cannot write to it");		
	}
	if (destinationFile.exists()) {
		log.error("Removing old '{}' to replace it with new content", destination);
		destinationFile.delete();
	}

	try {
		FileUtils.writeStringToFile(destinationFile, content, Config.DEFAULT_CHARSET);
	} catch (IOException e) {
		log.error("Removing old '{}' to replace it with new content", destination);
		throw new SavingException("Could not save to '"+destination+"' due to IO problems", e);
	}
	
}


}
