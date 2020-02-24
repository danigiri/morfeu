package cat.calidos.morfeu.utils.injection;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class XMLNodeToStringModule {

protected final static Logger log = LoggerFactory.getLogger(XMLNodeToStringModule.class);


@Produces
String xml(Transformer transformer, DOMSource source, StreamResult result, Writer writer) throws ParsingException {

	try {
		transformer.transform(source, result);
	} catch (TransformerException e) {
		String msg = "Could not generate string for node "+source.getNode().getNodeName();
		log.error(msg);
		throw new ParsingException(msg, e);
	}

	return writer.toString();

}


@Produces
Transformer transformer(Node node) throws ConfigurationException {

	try {

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		return transformer;

	} catch (TransformerConfigurationException e) {
		String msg = "Could not configure parser for node "+node.getNodeName();
		log.error(msg);
		throw new ConfigurationException(msg, e);
	}

}


@Produces
DOMSource source(Node node) {
	return new DOMSource(node);
}


@Produces
StreamResult result(Writer writer) {
	return new StreamResult(writer);
}


@Produces
Writer writer() {
	return new StringWriter();
}


}

/*
 *    Copyright 2020 Daniel Giribet
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

