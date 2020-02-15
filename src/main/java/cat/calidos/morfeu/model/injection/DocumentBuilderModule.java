package cat.calidos.morfeu.model.injection;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cat.calidos.morfeu.problems.ConfigurationException;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class DocumentBuilderModule {


@Produces
public static DocumentBuilderFactory produceDocumentBuilderFactory() {

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setNamespaceAware(true);

	return dbf;

}


@Produces
public static DocumentBuilder produceDocumentBuilder(DocumentBuilderFactory dbf) throws ConfigurationException {

	//dbf.setSchema(s);
	DocumentBuilder db;
	try {
		db = dbf.newDocumentBuilder();
	} catch (ParserConfigurationException e) {
		throw new ConfigurationException("Problem when configuring the xml parsing system", e);
	}

	return db;

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

