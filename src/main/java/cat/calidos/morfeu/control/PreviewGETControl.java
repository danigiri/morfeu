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

package cat.calidos.morfeu.control;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.SavingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.view.injection.DaggerSVGViewComponent;


/** Controller to generate cell preview, generates SVG dynamically for the moment
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class PreviewGETControl extends Control {

protected final static Logger log = LoggerFactory.getLogger(PreviewGETControl.class);

private String prefix;				// not used, only for logging
private String path;				// not used, only for logging
private Optional<String> header;	// header of the SVG preview image
private boolean truncate;			// trucante the text within the SVG
private Map<String, String> params;	// params to concateneate onto the SVG for info

public PreviewGETControl(String prefix, String path, Optional<String> header, Map<String, String> params) {

		super("preview", "", "");

		this.prefix = prefix;
		this.path = path;
		this.params = params;
		this.header = header;
		this.truncate = false;
}


@Override
protected Object process() throws InterruptedException, ExecutionException, ValidationException, ParsingException,
		FetchingException, ConfigurationException, SavingException, TransformException {

	String text = params.values().stream().collect(Collectors.joining(","));

	return DaggerSVGViewComponent.builder().from(text).withHeader(header).truncate(truncate).build().render();

}


@Override
protected String render(String template, Object value, String problem) {
	return value.toString();	// no need to use templates, we return the string
}


@Override
protected void beforeProcess() {
	log.trace("Previewing('{}', '{}')", prefix, path);
}


@Override
protected void afterProblem(String problem) {
	log.warn("Problem previewing('{}', '{}'): {}", prefix, path, problem);

}


@Override
protected Object problemInformation() {
	return params;	// we attach the parameters map which will be useful for error reporting
}

}
