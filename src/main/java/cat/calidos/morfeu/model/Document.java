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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Document extends Cell implements Validable {

protected String kind;
protected URI modelURI;
protected URI contentURI;
protected Model model;


public Document(URI u) {
	super(u);
}

public Document(String name, String desc, String kind, URI uri, URI modelUri, URI docUri, Model m) throws URISyntaxException {
	
	super(name, uri, desc);

	this.kind = kind;
	this.uri = uri;
	setModelURI(modelUri);
	setContentURI(docUri);
	this.model = m;

}


@Override
public void validate() throws RuntimeException {

	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
* @see java.lang.Object#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {

	return "{"+this.name+", ["+this.kind+"], uri:"+this.uri+", model:"+this.modelURI+", doc:"+this.contentURI+"}";
}


public String getType() {

	return kind;
}


@JsonProperty("kind") 
public void setKind(String kind) {
	this.kind = kind;
}


public URI getModelURI() {
	return modelURI;
}


@JsonProperty("modelURI") 
public void setModelURI(URI modelURI) throws URISyntaxException {
	
	this.modelURI = makeAbsoluteURIIfNeeded(modelURI);
	
}

public URI getContentURI() {
	return contentURI;
}


@JsonProperty("contentURI") 
public void setContentURI(URI contentURI) throws URISyntaxException {
	this.contentURI = makeAbsoluteURIIfNeeded(contentURI);
}


public Model getModel() {
	return model;
}


public void setModel(Model model) {

	this.model = model;
}


private URI makeAbsoluteURIIfNeeded(URI relativeURI) throws URISyntaxException {

	String path = uri.getPath();
	String uriString = uri.toString();
	String uriHostPart = uriString.substring(0,uriString.length()-path.length());
	
	// there may be a better way to do this but tests will protect us
	if (uri!=null && !relativeURI.isAbsolute() && !uri.getScheme().equals("file"))  {
		return new URI(uriHostPart+relativeURI); 
	} else { 
		return relativeURI;
	}
	
	
}

 

}
