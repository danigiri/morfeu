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

package cat.calidos.morfeu.runtime.api;

import java.util.Optional;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;


/**
 * Generic task holder, where all configuration is stored/created
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public interface ReadyTask extends Task {

/** Start this task and @return a starting task, no stdin */
default public StartingTask start() throws MorfeuRuntimeException {
	return start(Optional.empty());
}


/** Start this task and @return a starting task, stdin */
default public StartingTask start(String stdin) throws MorfeuRuntimeException {
	return start(Optional.of(stdin));
}


/** Start this task and @return a starting task, optional stdin */
public StartingTask start(Optional<String> stdin) throws MorfeuRuntimeException;

}
