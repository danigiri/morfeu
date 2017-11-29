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

package cat.calidos.morfeu.model.injection;

import java.io.IOException;
import java.net.URI;

import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ParseStringModule {


//notice this is a DOM Document and not a morfeu document
@Produces
public static org.w3c.dom.Document produceDomDocument(DocumentBuilder db, @Named("ContentString") String content) 
		throws ParsingException, FetchingException {
	
	// TODO: we can probably parse with something faster than building into dom
	try {
		return db.parse(IOUtils.toInputStream(content));
	} catch (SAXException e) {
		throw new ParsingException("Problem when parsing '"+content.substring(0, 20)+"'", e);
	} catch (IOException e) {
		throw new FetchingException("IO problem when parsing '"+content.substring(0, 20)+"'", e);
	}


}


}
