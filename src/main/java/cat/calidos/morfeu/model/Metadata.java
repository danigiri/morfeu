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

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Metadata {

private static final String DEFAULT_DESC = "";
private static final String DEFAULT_THUMB = "DEFAULT";
public static String DEFAULT_PRESENTATION = "CELL";

private Optional<String> desc;
private Optional<String> presentation;
private Optional<String> thumb;



public Metadata() {
	this(Optional.empty(), Optional.empty(), Optional.empty());
}


public Metadata(String desc, String presentation, String thumb) {
	this(Optional.ofNullable(desc), Optional.ofNullable(presentation), Optional.ofNullable(thumb));
}


public Metadata(Optional<String> desc, Optional<String> presentation, Optional<String> thumb) {

	this.desc = desc;
	this.presentation = presentation;
	this.thumb = thumb;
	
}

public Metadata(Optional<String> desc, Optional<String> pres, Optional<String> thumb, Metadata fallback) {
	this(desc.orElse(fallback.getDesc()),pres.orElse(fallback.getPresentation()), thumb.orElse(fallback.getThumb()));
}

public String getDesc() {
	return desc.orElse(DEFAULT_DESC);
}


public String getPresentation() {
	return presentation.orElse(DEFAULT_PRESENTATION);
}


public String getThumb() {
	return thumb.orElse(DEFAULT_THUMB);
}

private static Optional<String> combine(Optional<String> a, Optional<String> b) {
	return Stream.of(a, b)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.findFirst();
}

}
