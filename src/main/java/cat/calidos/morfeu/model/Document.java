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

package cat.calidos.morfeu.model;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Document extends Cell {

@Inject @JsonProperty("type") public String type;
@Inject @JsonProperty("modelURI") public URI modelURI;
@Inject @JsonProperty("docURI") public URI docURI;
@Inject public Model model;


public Document(URI u) {
	super(u);
}

public Document(String name, String desc, String type, URI uri, URI modelUri, URI docUri, Model m) {
	
	super(name, uri, desc);

	this.type = type;
	this.uri = uri;
	this.modelURI = modelUri;
	this.docURI = docUri;
	this.model = m;

}


/* (non-Javadoc)
* @see java.lang.Object#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {

	return "{"+this.name+", ["+this.type+"], uri:"+this.uri+", model:"+this.modelURI+", doc:"+this.docURI+"}";
}



}
