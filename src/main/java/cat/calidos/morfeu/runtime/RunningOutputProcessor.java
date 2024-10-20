package cat.calidos.morfeu.runtime;

import java.util.function.Function;

import cat.calidos.morfeu.runtime.api.MutableTask;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class RunningOutputProcessor extends ExecOutputProcessor {

MutableTask task;

public RunningOutputProcessor(Function<String, Integer> matcher) {
	super(matcher);
}


public void setTask(ExecRunningTask task) { this.task = task; }


@Override
protected void processLine(String line) {

	System.err.println("[RUNNING]>>" + line);
	task.appendToOutput(line);
	if (task.isOneTime() && !task.isDone()) {
		int percent = process(line);
		task.setRemaining(percent);
		// we do not take further action as we expect the process to finish regardless of percent
		// remaining
		// TODO: could add warnings here if we have further output and we have marked as zero
		// percent
	} else {

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
