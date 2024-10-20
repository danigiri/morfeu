package cat.calidos.morfeu.runtime;

import java.util.function.Function;

import cat.calidos.morfeu.runtime.api.MutableTask;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class StoppingOutputProcessor extends ExecOutputProcessor {

MutableTask task;

public StoppingOutputProcessor(Function<String, Integer> matcher) {
	super(matcher);
}


public void setTask(ExecStoppingTask execStoppingTask) { this.task = execStoppingTask; }


@Override
protected void processLine(String line) {

	System.err.println("[STOPPING]>>" + line);
	task.appendToOutput(line);
	int percent = process(line);
	task.setRemaining(percent);
	System.err.println("Marked remaining percentage");
	if (task.isDone()) {
		System.err.println("STOPPING --> FINISHED, marking as finished");
		task.markAsFinished();
	}

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
