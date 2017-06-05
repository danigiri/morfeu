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

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ValidationException;

/** TODO: check if document extends cell or not
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Document extends Cell implements Validable {

protected String kind;
protected URI prefix;
protected URI modelURI;
protected URI contentURI;
protected Model model;
protected Validable validator;
protected boolean isValid = false;


public Document(URI u) {
	super(u);
}

public Document(URI u, String name) {
	super(u, name);
}


public Document(String name, String desc, String kind, URI prefix, URI uri, URI modelUri, URI docUri, Model m) 
		throws URISyntaxException {
	
	super(uri, name);

	this.desc = desc;
	this.prefix = prefix;	// this may be moved to the supperclass
	this.kind = kind;
	this.uri = uri;
	setModelURI(modelUri);
	setContentURI(docUri);
	this.model = m;

}



@Override
public void validate() throws ValidationException, FetchingException {
	if (validator==null) {
		throw new ValidationException("Document does not have a validation mechanism", new NullPointerException());
	}
	validator.validate();
	// if there is no exception
	isValid = true;
}
 

@Override
public boolean isValid() {
	return isValid;
}


/* (non-Javadoc)
* @see java.lang.Object#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {

	return "{"+this.name+", ["+this.kind+"], uri:"+this.uri+", prefix:"+prefix+", model:"+this.modelURI+", doc:"+this.contentURI+"}";
}


public String getKind() {

	return kind;
}


@JsonProperty("kind") 
public void setKind(String kind) {
	this.kind = kind;
}


@JsonProperty("prefix") 
public void setPrefix(URI prefix) {
	this.prefix = prefix;
}


public URI getPrefix() {
	return prefix;
}


public URI getModelURI() {
	return modelURI;
}


@JsonProperty("modelURI") 
public void setModelURI(URI modelURI) {
	
	this.modelURI = modelURI;
	
}


public URI getContentURI() {
	return contentURI;
}


@JsonProperty("contentURI") 
public void setContentURI(URI contentURI) {
	this.contentURI = contentURI;
}


public Model getModel() {
	return model;
}


public void setModel(Model model) {
	this.model = model;
}


public void setValidator(Validable validator) {
	this.validator = validator;
} 

}
