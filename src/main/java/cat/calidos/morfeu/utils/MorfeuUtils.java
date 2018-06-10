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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import cat.calidos.morfeu.problems.ParsingException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuUtils {


public static Throwable findRootCauseFrom(ExecutionException e) {

	Throwable cause = e;
	while (cause!=null && cause instanceof ExecutionException) {
		cause = cause.getCause();
	}
	
	return cause;

}

// if the uri is absolute we don't modify it, if prefix is a file://, we don't modify it either, otherwise we prepend the prefix
public static URI makeAbsoluteURIIfNeeded(URI prefix, URI uri) throws ParsingException {
	
	URI finalURI = null;
	if (!uri.isAbsolute() && prefix.getScheme()!=null && !prefix.getScheme().equals("file")) {
		try {
			finalURI = new URI(prefix+uri.toString());
		} catch (URISyntaxException e) {
			throw new ParsingException("Problem composing absolute urls with prefix:'"+prefix+"', and:'"+uri+"'",e);
		}
	} else {
		finalURI = uri;
	}
	
	return finalURI;

}



}
