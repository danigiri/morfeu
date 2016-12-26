/*
 *    Copyright 2016 Daniel Giribet
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

package cat.calidos.morfeu.model.di;

import cat.calidos.morfeu.model.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sun.xml.xsom.parser.XSOMParser;

import dagger.Module;
import dagger.Provides;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import dagger.producers.Production;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class DocumentModule {

protected XSOMParser parser;

@Provides
public SAXParserFactory provideSAXParserFactory() {
	//TODO: double-check which parser to use that implements security like we want
	return new SAXParserFactoryImpl();
}

@Provides
public XSOMParser provideSchemaParser(SAXParserFactory factory) {
	
	factory.setNamespaceAware(true);
    try {
    	// TODO: checkout how to ensure we can load includes but only from the same origin and stuff
    	factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	} catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   return new XSOMParser(factory);
    
}


@Provides
public Document produceDocumentFromURL() {

return null;
	
}



}

