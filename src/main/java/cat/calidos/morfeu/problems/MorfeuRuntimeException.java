/*
 * Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.problems;

import java.util.Optional;

import cat.calidos.morfeu.webapp.control.problems.WebappRuntimeException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MorfeuRuntimeException extends RuntimeException implements ExceptionWithPayload {

protected Optional<String> payload = Optional.empty();

public MorfeuRuntimeException() {
	super();
}


public MorfeuRuntimeException(MorfeuException e) {
	super(e);
}


public MorfeuRuntimeException(String message) {
	super(message);
}


public MorfeuRuntimeException(String message, Throwable e) {
	super(message + "(" + e.getMessage() + ")", e);
}


public void setPayload(String payload) { this.payload = Optional.of(payload); }


public Optional<String> getPayload() { return payload; }

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
