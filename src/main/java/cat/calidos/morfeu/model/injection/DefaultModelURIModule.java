/*
 * Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.model.injection;

import java.net.URI;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.Document;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class DefaultModelURIModule {

protected final static Logger log = LoggerFactory.getLogger(DefaultModelURIModule.class);

// this is the basic model uri, usually relative and easy to read and understand, cannot necesarily
// be fetched from
// the runtime context
@Produces @Named("ModelURI")
public static URI modelURI(@Named("ParsedDocument") Document doc) {

	URI modelURI = doc.getModelURI();
	log.trace("Using provided model URI " + modelURI);

	return modelURI;

}

}
