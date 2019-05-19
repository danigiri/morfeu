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

package cat.calidos.morfeu.runtime;

import java.util.function.Function;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class StartingOutputProcessor extends ExecOutputProcessor {

ExecStartingTask startingTask;


public StartingOutputProcessor(Function<String, Integer> matcher) {
	super(matcher);
}


/**	this is needed as we will be creating the processor with this task */
public void setTask(ExecStartingTask startingTask) {
	this.startingTask = startingTask;
}


@Override
protected void processLine(String line) {

	System.out.println("[STARTING]>>"+line);
	startingTask.appendToOutput(line);
	int percent = process(line);
	startingTask.setRemaining(percent);
	if (startingTask.isDone()) {
		System.out.println("STARTING --> STARTED, marking and running callback");
		startingTask.markAsStarted();
	}

}


}
