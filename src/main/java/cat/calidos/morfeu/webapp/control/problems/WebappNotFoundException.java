package cat.calidos.morfeu.webapp.control.problems;

import cat.calidos.morfeu.problems.MorfeuException;

public class WebappNotFoundException extends WebappRuntimeException {

public WebappNotFoundException() {
	super();
}


public WebappNotFoundException(MorfeuException e) {
	super(e);
}

public WebappNotFoundException(String message) {
	super(message);
}


public WebappNotFoundException(String message, String payload, Exception e) {
	super(message, payload, e);
}


public WebappNotFoundException(String message, Exception e) {
	this(message, message, e);
}



}

/*
 * Copyright 2024 Daniel Giribet
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
