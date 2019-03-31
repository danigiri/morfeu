/*
 *    Copyright 2018 Daniel Giribet
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
import java.util.HashSet;
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

private static final HashSet<String> EMPTY_SET = new HashSet<String>(0);

private static final String DEFAULT_DESC = "";
private static final String DEFAULT_THUMB = "DEFAULT";
public static String DEFAULT_PRESENTATION = "CELL";
private static final String DEFAULT_CELL_PRESENTATION = "DEFAULT";
private static final String DEFAULT_CELL_PRESENTATION_TYPE = "IMG";
public static final String DEFAULT_VALUE_PREFIX = "@";
public static final String ATTRIBUTES_ONLY = "ATTRIBUTES-ONLY";	// directive to signal this cm serialises only attribs
public static final String LISTS_NO_PLURAL = "LISTS-NO-PLURAL";	// directive that a list is not pluralised (default)
public static final String KEY_VALUE = "KEY-VALUE";				// directive that specifyies that it's a key-value

private URI uri;	// pre-calculated default
private String desc;
private String presentation;
private String cellPresentation;
private String cellPresentationType;
private String thumb;
private Optional<String> identifier;
private Map<String, String> defaultValues;
private Map<String, Set<String>> directives;
private Map<String, Set<String>> attributes;


public Metadata(URI uri,
				String desc,
				String presentation,
				String cellPresentation,
				String cellPresentationType,
				String thumb,
				String identifier,
				Map<String, String> defaultValues,
				Map<String, Set<String>> directives,
				Map<String, Set<String>> attributes
			) {
	this(uri,
			Optional.ofNullable(desc),
			Optional.ofNullable(presentation),
			Optional.ofNullable(cellPresentation),
			Optional.ofNullable(cellPresentationType),
			Optional.ofNullable(thumb),
			Optional.ofNullable(identifier),
			defaultValues,
			directives,
			attributes);
}


public Metadata(URI uri,
				Optional<String> desc,
				Optional<String> presentation,
				Optional<String> cellPresentation,
				Optional<String> cellPresentationType,
				Optional<String> thumb,
				Optional<String> identifier,
				Map<String, String> defaultValues,
				Map<String, Set<String>> directives,
				Map<String, Set<String>> attributes) {

	this.uri = uri;
	this.desc = desc.orElse(DEFAULT_DESC);
	this.presentation = presentation.orElse(DEFAULT_PRESENTATION);
	this.cellPresentation = cellPresentation.orElse(DEFAULT_CELL_PRESENTATION);
	this.cellPresentationType = cellPresentationType.orElse(DEFAULT_CELL_PRESENTATION_TYPE);
	this.thumb = thumb.orElse(DEFAULT_THUMB);
	this.identifier = identifier;
	this.defaultValues = defaultValues;
	this.directives = directives;
	this.attributes = attributes;

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


public String getCellPresentationType() {
	return cellPresentationType;
}


public String getThumb() {
	return thumb;
}


public Optional<String> getIdentifier() {
	return identifier;
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


public Set<String> getDirectivesFor(String case_) {
	return directives.containsKey(case_) ? directives.get(case_) : EMPTY_SET;
}


public Map<String, Set<String>> getDirectives() {
	return directives;
}


public Set<String> getAttributesFor(String case_) {
	return attributes.containsKey(case_) ? attributes.get(case_) : EMPTY_SET;
}


public Map<String, Set<String>> getAttributes() {
	return attributes;
}


@Override
public String toString() {

	return "Metadata:"+
			"{uri:"+uri+
			", desc:'"+desc+
			"', thumb:'"+thumb+
			"', presentation:'"+presentation+
			"' defaults("+defaultValues.size()+")}";
}


/** Merge the more priority metadata instance and the less priority one, priority values have precedence, defaults will
* 	overriden by the less priority.
*	@param u new uri
*	@param morePriority higher priority metadata instance
*	@param lessPriority less priority metadata
*	@return new instance with the merge
*/
public static Metadata merge(URI u, Metadata morePriority, Metadata lessPriority) {
	
	String desc = morePriority.getDesc();
	desc = desc.equals(DEFAULT_DESC) ? lessPriority.getDesc() : desc;
	String presentation = morePriority.getPresentation();
	presentation = presentation.equals(DEFAULT_PRESENTATION) ? lessPriority.getPresentation() : presentation;
	String cellPresentation = morePriority.getCellPresentation();
	cellPresentation = cellPresentation.equals(DEFAULT_CELL_PRESENTATION) 
						? lessPriority.getCellPresentation() : cellPresentation;
	String cellPresentationType = morePriority.getCellPresentationType();
	cellPresentationType = cellPresentationType.equals(DEFAULT_CELL_PRESENTATION_TYPE) 
							? lessPriority.getCellPresentationType() : cellPresentationType;
	String thumb = morePriority.getThumb();
	thumb = thumb.equals(DEFAULT_THUMB) ? lessPriority.getThumb() : thumb;
	
	String identifier = morePriority.getIdentifier().isPresent() ? morePriority.getIdentifier().get() : lessPriority.getIdentifier().orElse(null);
	
	Map<String,String> newDefaultValues = new HashMap<String, String>();
	newDefaultValues.putAll(lessPriority.getDefaultValues());
	newDefaultValues.putAll(morePriority.getDefaultValues());	// this will overwrite
	
	Map<String, Set<String>> directives = mergeMapSet(morePriority.getDirectives(), lessPriority.getDirectives());
	Map<String, Set<String>> attributes = mergeMapSet(morePriority.getAttributes(), lessPriority.getAttributes());
	
	return new Metadata(u, 
						desc, 
						presentation, 
						cellPresentation, 
						cellPresentationType, 
						thumb, 
						identifier, 
						newDefaultValues, 
						directives, 
						attributes);
	
}


private static Map<String, Set<String>> mergeMapSet(Map<String, Set<String>> more, Map<String, Set<String>> less) {

	Map<String, Set<String>> merged = new HashMap<String, Set<String>>();
	merged.putAll(less);
	more.keySet().forEach(d -> {
		if (merged.containsKey(d)) {
			merged.get(d).addAll(more.get(d));
		} else {
			merged.put(d, more.get(d));
		}
	});

	return merged;

}

}
