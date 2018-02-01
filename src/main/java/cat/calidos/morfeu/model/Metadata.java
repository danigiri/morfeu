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

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Metadata implements Locatable {

protected final static Logger log = LoggerFactory.getLogger(Metadata.class);
		
private static final String DEFAULT_DESC = "";
private static final String DEFAULT_THUMB = "DEFAULT";
public static String DEFAULT_PRESENTATION = "CELL";
private static final String DEFAULT_CELL_PRESENTATION = "DEFAULT";

private URI uri;	// pre-calculated default
private String desc;
private String presentation;
private String cellPresentation;
private String thumb;
private Map<String, String> defaultValues;

private Map<String, Set<String>> directives;

private Map<String, Set<String>> attributes;

public static final String DEFAULT_VALUE_PREFIX = "@";
//private static String UNDEFINED = "";

public Metadata(URI uri, 
				String desc, 
				String presentation, 
				String cellPresentation, 
				String thumb, 
				Map<String, String> defaultValues,
				Map<String, Set<String>> directives,
				Map<String, Set<String>> attributes
			) {
	this(uri, 
			Optional.ofNullable(desc), 
			Optional.ofNullable(presentation), 
			Optional.ofNullable(cellPresentation), 
			Optional.ofNullable(thumb),
			defaultValues,
			directives,
			attributes);
}


public Metadata(URI uri, 
				Optional<String> desc, 
				Optional<String> presentation, 
				Optional<String> cellPresentation,
				Optional<String> thumb,
				Map<String, String> defaultValues,
				Map<String, Set<String>> directives,
				Map<String, Set<String>> attributes) {

	this.uri = uri;
	this.desc = desc.orElse(DEFAULT_DESC);
	this.presentation = presentation.orElse(DEFAULT_PRESENTATION);
	this.cellPresentation = cellPresentation.orElse(DEFAULT_CELL_PRESENTATION);
	this.thumb = thumb.orElse(DEFAULT_THUMB);
	this.defaultValues = defaultValues;
	this.directives = directives;
	this.attributes = attributes;

}


public Metadata(URI uri, 
				Optional<String> desc, 
				Optional<String> pres, 
			    Optional<String> cellPresentation,
				Optional<String> thumb,
				Map<String, String> defaultValues,
				Metadata fallback,
				Map<String, Set<String>> directives,
				Map<String, Set<String>> attributes) {
	this(uri,
		 desc.orElse(fallback.getDesc()),
		 pres.orElse(fallback.getPresentation()),
		 cellPresentation.orElse(fallback.getCellPresentation()),
		 thumb.orElse(fallback.getThumb()),
		 defaultValues,
		 directives,
		 attributes);
}


public static Metadata merge(URI u, Metadata morePriority, Metadata lessPriority) {
	
	String desc = morePriority.getDesc();
	desc = desc.equals(DEFAULT_DESC) ? lessPriority.getDesc() : desc;
	String presentation = morePriority.getPresentation();
	presentation = presentation.equals(DEFAULT_PRESENTATION) ? lessPriority.getPresentation() : presentation;
	String cellPresentation = morePriority.getCellPresentation();
	cellPresentation = cellPresentation.equals(DEFAULT_CELL_PRESENTATION) 
						? lessPriority.getCellPresentation() : cellPresentation;
	String thumb = morePriority.getThumb();
	thumb = thumb.equals(DEFAULT_THUMB) ? lessPriority.getThumb() : thumb;
	
	Map<String,String> newDefaultValues = new HashMap<String, String>();
	newDefaultValues.putAll(lessPriority.getDefaultValues());
	newDefaultValues.putAll(morePriority.getDefaultValues());	// this will overwrite
	
	Map<String, Set<String>> directives = new HashMap<String, Set<String>>();
	
	Map<String, Set<String>> attributes = new HashMap<String, Set<String>>();
	
	return new Metadata(u, desc, presentation, cellPresentation, thumb, newDefaultValues, directives, attributes);
	
}


public String getDesc() {
	return desc;
}


public String getPresentation() {
	return presentation;
}


public String getCellPresentation() {
	return cellPresentation;
}


public String getThumb() {
	return thumb;
}


public Map<String, String> getDefaultValues() {
	return defaultValues;
}


@Override
public URI getURI() {
	return uri;
}


@Override
public String getName() {

	// TODO Auto-generated method stub
	return null;
}


Set<String> getDirectivesFor(String case_) {
	
} 

Set<String> getAttributessFor(String case_) {
	
} 



/* (non-Javadoc)
* @see java.lang.Object#toString()
*//////////////////////////////////////////////////////////////////////////////
@Override
public String toString() {

	return "Metadata:"+
			"{uri:"+uri+
			", desc:'"+desc+
			"', thumb:'"+thumb+
			"', presentation:'"+presentation+
			"' defaults("+defaultValues.size()+")}";
}



}
