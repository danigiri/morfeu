package cat.calidos.morfeu.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.utils.injection.DaggerJSONParserComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class JSONGETControl extends Control {

private final static Logger log = LoggerFactory.getLogger(JSONGETControl.class);

public JSONGETControl(String operation, String template, String problemTemplate) {
	super(operation, template, problemTemplate);
}


@Override
public String processRequest() {

	String json = super.processRequest();
	try {

		return DaggerJSONParserComponent.builder().from(json).build().pretty().get();

	} catch (Exception e) {
		log.warn("Could not pretty print json, returning without prettfying");
	}

	return json;

}


}

/*
 *    Copyright 2024 Daniel Giribet
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

