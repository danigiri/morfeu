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

import org.zeroturnaround.exec.ProcessExecutor;

import cat.calidos.morfeu.runtime.api.Task;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class ExecTask implements Task {

protected int type;
protected int status;
protected Process process;
protected ProcessExecutor executor;

private ExecOutputProcessor outputProcessorWrapper;
private ExecProblemProcessor problemProcessorWrapper;
protected ExecOutputProcessor logMatcher;
protected ExecProblemProcessor problemMatcher;

private boolean isOK = true;


public ExecTask(int type, int status, ProcessExecutor executor) {

	this.type = type;
	this.status = status;
	this.executor = executor;

}


public ExecTask(int type, 
				int status, 
				ProcessExecutor executor, 
				ExecOutputProcessor outputProcessorWrapper,
				ExecProblemProcessor problemProcessorWrapper,
				ExecOutputProcessor logMatcher, 
				ExecProblemProcessor problemMatcher) {
	
	this(type, status, executor);
	
	this.logMatcher = logMatcher;
	this.problemMatcher = problemMatcher;
	
	this.outputProcessorWrapper = outputProcessorWrapper;
	this.problemProcessorWrapper = problemProcessorWrapper;

}


public void startRedirectingOutput() {

	// we are using the indirection as the executor.redirect* methods are non-reentrant, namely, if they are called
	// from within a callback, they have no effect, so we use a level of indirection
	
	System.out.println("REDIRECTING (INDIRECTLY) IN "+this.translate(status));
	problemProcessorWrapper.setIndirectProcessor(problemMatcher);
	outputProcessorWrapper.setIndirectProcessor(logMatcher);
	executor.redirectError(problemProcessorWrapper);
	executor.redirectOutput(outputProcessorWrapper);

}


public void stopRedirectingOutput() {

	executor.redirectError(null);
	executor.redirectOutput(null);

}


public void setProcess(Process process) {
	this.process = process;
}


@Override
public int getStatus() {	// if the process has somehow died, we mark as finished
	return (process!=null && !process.isAlive()) ? Task.FINISHED : status;
}


@Override
public int getType() {
	return type;
}


public void setKO() {
	isOK = false;
}

@Override
public boolean isOK() {
	return isOK;
}


}
