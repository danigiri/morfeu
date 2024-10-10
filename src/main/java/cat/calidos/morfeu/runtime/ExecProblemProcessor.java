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

package cat.calidos.morfeu.runtime;

import java.util.function.Predicate;

import org.zeroturnaround.exec.stream.LogOutputStream;

import cat.calidos.morfeu.runtime.api.MutableTask;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecProblemProcessor extends LogOutputStream {

private MutableTask				task;
private Predicate<String>		matcher;
private ExecProblemProcessor	indirectProcessor;

public ExecProblemProcessor() {
}


public ExecProblemProcessor(Predicate<String> matcher) {
	this.matcher = matcher;
}


public ExecProblemProcessor(ExecProblemProcessor indirectProcessor) {
	this.indirectProcessor = indirectProcessor;
}


public void setTask(MutableTask task) {
	this.task = task;
}


public void setIndirectProcessor(ExecProblemProcessor indirectProcessor) {
	this.indirectProcessor = indirectProcessor;
}


public boolean matchesProblem(String line) {
	return matcher.test(line);
}


@Override
protected void processLine(String line) {

	if (indirectProcessor != null) {
		indirectProcessor.processLine(line);
	} else {
		defaultProcessLine(line);
	}

}


private void defaultProcessLine(String line) {

	System.err.println(">>" + line);
	if (matcher.test(line)) {
		System.err.println("MARKED AS PROBLERMATIC");
		task.markAsFailed();
	}

}

}
