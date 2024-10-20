package cat.calidos.morfeu.runtime;

import java.util.function.Function;

import org.zeroturnaround.exec.stream.LogOutputStream;


public class ExecOutputProcessor extends LogOutputStream {

private Function<String, Integer>	matcher;
private ExecOutputProcessor			indirectProcessor;

public ExecOutputProcessor() {
}


public ExecOutputProcessor(Function<String, Integer> matcher) {
	this.matcher = matcher;
}


public ExecOutputProcessor(ExecOutputProcessor indirectProcessor) {
	this.indirectProcessor = indirectProcessor;
}


public void setIndirectProcessor(ExecOutputProcessor indirectProcessor) {
	this.indirectProcessor = indirectProcessor;
}


/**
 * @return given a log line, it returns how much is remaining in the current state, or negative if
 *         there is an error or some kind of problem. The remaining time is undefined for long
 *         running tasks. Values are from 100 to 1 for percentage, 0 for stage completed and
 *         negative for errors
 */
public int process(String line) {
	return (indirectProcessor == null) ? matcher.apply(line) : indirectProcessor.process(line);
}


@Override
protected void processLine(String line) {
	if (indirectProcessor == null) {
		process(line);
	} else {
		indirectProcessor.processLine(line);
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
