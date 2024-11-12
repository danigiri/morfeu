package cat.calidos.morfeu.webapp.control.problems;

import java.util.Optional;

import cat.calidos.morfeu.problems.MorfeuException;
import cat.calidos.morfeu.problems.MorfeuRuntimeException;


/**
 * We can use this layer to translate internal exceptions to web app exceptions to return the right
 * code
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class WebappRuntimeException extends MorfeuRuntimeException {

public WebappRuntimeException() {
	super();
}


public WebappRuntimeException(String message) {
	super(message);
}


public WebappRuntimeException(MorfeuException e) {
	super(e);
	Optional<String> incomingPayload = e.getPayload();
	if (incomingPayload.isPresent()) {
		this.setPayload(incomingPayload.get());
	}
}


public WebappRuntimeException(String m, String payload, Exception e) {
	super(m, e);
	setPayload(payload);
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
