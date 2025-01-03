// STRING TO PARSED MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import java.io.IOException;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.Config;


/**
 * Given a raw content string containing XML and a dom document builder, produce a dom tree
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class StringToParsedModule {

// notice this is a DOM Document and not a morfeu document
@Produces
public static org.w3c.dom.Document produceDomDocument(	DocumentBuilder db,
														@Named("Content") String content)
		throws ParsingException, FetchingException {

	// TODO: we can probably parse with something faster than building into dom
	try {
		return db.parse(IOUtils.toInputStream(content, Config.DEFAULT_CHARSET));
	} catch (SAXException e) {
		throw new ParsingException("Problem when parsing '" + content.substring(0, 20) + "'", e);
	} catch (IOException e) {
		throw new FetchingException("IO problem when parsing '" + content.substring(0, 20) + "'",
				e);
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
