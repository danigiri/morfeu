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

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.process.Processes;
import org.zeroturnaround.process.SystemProcess;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import cat.calidos.morfeu.runtime.api.FinishedTask;
import cat.calidos.morfeu.runtime.api.MutableTask;
import cat.calidos.morfeu.runtime.api.StoppingTask;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecMutableTask extends ExecTask implements MutableTask {

private static final String LF = "\n";
private int remaining = MAX;	// by default we have everything left to do

private StringBuilder output = new StringBuilder();
protected ExecStoppingTask stoppingTask;
protected ExecFinishedTask finishedTask;


public ExecMutableTask(int type, 
						int status, 
						ProcessExecutor executor,
						ExecOutputProcessor outputProcessorWrapper,
						ExecProblemProcessor problemProcessorWrapper,
						ExecOutputProcessor logProcessor, 
						ExecProblemProcessor problemProcessor,
						ExecStoppingTask stoppingTask,
						ExecFinishedTask finishedTask) {

	super(type, 
			status,
			executor,
			outputProcessorWrapper,
			problemProcessorWrapper,
			logProcessor,
			problemProcessor);

	this.stoppingTask = stoppingTask;
	this.finishedTask = finishedTask;

}


@Override
public void appendToOutput(String content) {
	output.append(content).append(LF);
}


@Override
public void setRemaining(int percent) {						// we have the STDOUT logger and STDEER logger both
	remaining = Math.min(Math.min(MAX, percent), remaining); 	// changing this so we never go up
}


@Override
public StoppingTask stop() throws MorfeuRuntimeException {

	System.out.println(" --> STOP[");
	try {
		int pid = (int) process.pid();
		SystemProcess systemProcess = Processes.newPidProcess(pid);
		//stopRedirectingOutput();
		stoppingTask.startRedirectingOutput();	// redirect just before killing, to prevent race condition
		status = STOPPED;
		setRemaining(NEXT);
		systemProcess.destroyGracefully();
	} catch (IOException | InterruptedException e) {
		throw new MorfeuRuntimeException("Problem gracefully destroying process", e);
	}
	System.out.println("STOP]");

	return stoppingTask;
	
}


@Override
public FinishedTask markAsFailed() {

	setKO();
	setRemaining(NEXT);

	return finishedTask();

}


@Override
public FinishedTask markAsFinished() {

	status = FINISHED;

	return finishedTask();

}


@Override
public int getRemaining() {
	return remaining;
}


@Override
public String show() {
	return output.toString();
}


public FinishedTask finishedTask() {
//	if (status!=FINISHED) {
//		throw new IllegalStateException("Accessing finished task when we are not finished ("+translate(status)+")");
//	}
	return finishedTask;
}

}
