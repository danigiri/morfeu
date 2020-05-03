// DOCUMENT . JAVA

package cat.calidos.morfeu.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ValidationException;

/** 
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Document extends ComplexCell implements Validable {

public static String ROOT_NAME = "";

protected String kind;
protected URI prefix;
protected URI modelURI;
protected URI fetchableModelURI;
protected URI contentURI;
protected URI fetchableContentURI;
protected Model model;			// it's the same as the cellmodel of the root empty node
protected Validable validator;
protected boolean isValid = false;
protected boolean skipValidation = false;


public Document(URI u) {
	super(u, "Empty doc", "No desc", Optional.empty(), null, null, null, null);
}


public Document(URI u, String name, String desc) {
	super(u, name, desc, Optional.empty(), null, null, null, null);
}


public Document(String name, String desc, String kind, URI prefix, URI uri, URI modelUri, URI docUri, Model model) 
		throws URISyntaxException {

	super(uri, name, desc, Optional.empty(), model, null, null, null);	// no children, attributes when created

	this.prefix = prefix;	// this may be moved to the supperclass
	this.kind = kind;
	this.uri = uri;
	setModelURI(modelUri);
	setContentURI(docUri);
	this.model = model;

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


/** this is the user-friendly, relative URI used to easily identify this document model */
@JsonProperty("modelURI") 
public void setModelURI(URI uri) {
//	System.err.println("***************** MODELURI="+uri);
	this.modelURI = uri;
	
}


/** This is the url that will be used to fetch the raw model data */
public URI getFetchableModelURI() {
	return fetchableModelURI;
}


@JsonProperty("fetchableModelURI") 
public void setFetchableModelURI(URI uri) {
	this.fetchableModelURI = uri;
}


/** this is the user-friendly, relative URI used to easily identify this document content */
public URI getContentURI() {
	return contentURI;
}


@JsonProperty("contentURI") 
public void setContentURI(URI contentURI) {
	this.contentURI = contentURI;
}


/** This is the url that will be used to fetch the raw model content */
public URI getFetchableContentURI() {
	return fetchableContentURI;
}


@JsonProperty("fetchableContentURI") 
public void setFetchableContentURI(URI uri) {
	this.fetchableContentURI = uri;
}


public boolean skipValidation() {
	return skipValidation;
}


@JsonProperty("skipValidation") 
public void skipValidation(boolean skip) {
	this.skipValidation = skip;
}


public Model getModel() {
	return model;
}


public void setModel(Model model) {
	this.model = model;
}


public void setContent(Composite<Cell> content) {

	this.children = content;

}


public void setValidator(Validable validator) {
	this.validator = validator;
} 

}

/*
 *    Copyright 2019 Daniel Giribet
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


