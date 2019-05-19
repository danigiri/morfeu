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

package cat.calidos.morfeu.runtime.api;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public interface MutableTask extends Task {

public void appendToOutput(String append);

public void setRemaining(int percent);

public StoppingTask stop() throws MorfeuRuntimeException;

public FinishedTask markAsFailed();

public FinishedTask markAsFinished();

public FinishedTask finishedTask();

}
