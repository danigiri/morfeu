/*
 * Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;


/**
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////
public class URIDeserializer extends StdDeserializer<URI> {

public URIDeserializer() {
	this(null);
}


public URIDeserializer(Class<?> t) {
	super(t);
}


@Override
public URI deserialize(	JsonParser p,
						DeserializationContext ctxt)
		throws IOException, JsonProcessingException {
	try {
		return new URI(p.getText());
	} catch (URISyntaxException e) {
		return null; // return new JSonPro
	}
}

}
