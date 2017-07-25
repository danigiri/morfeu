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

import static org.junit.Assert.*;

import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;

import com.sun.xml.xsom.parser.XSOMParser;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ParserModuleTest {

@Test
public void testProduceSAXParserFactory() throws Exception {
	assertNotNull(ParserModule.produceSAXParserFactory());
}


@Test
public void testProduceSchemaParser() throws Exception {
	
	SAXParserFactory factory = ParserModule.produceSAXParserFactory();
	XSOMParser parser = ParserModule.produceSchemaParser(factory);
	assertNotNull(parser);
	assertNotNull(parser.getErrorHandler());
	
}


@Test
public void testProduceJSONObjectMapper() throws Exception {
	assertNotNull(ParserModule.produceJSONObjectMapper());
}



}
