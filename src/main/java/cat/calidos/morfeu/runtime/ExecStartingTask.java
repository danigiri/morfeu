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

import java.io.InputStream;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.zeroturnaround.exec.ProcessExecutor;

import cat.calidos.morfeu.runtime.api.RunningTask;
import cat.calidos.morfeu.runtime.api.StartingTask;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecStartingTask extends ExecMutableTask implements StartingTask {

private BiConsumer<ExecStartingTask, ExecRunningTask> startedCallback;
private ExecRunningTask runningTask;


public ExecStartingTask(int type, 
						ProcessExecutor executor,
						Optional<InputStream> stdin,
						ExecOutputProcessor outputProcessorWrapper,
						ExecProblemProcessor problemProcessorWrapper,
						StartingOutputProcessor outputProcessor, 
						ExecProblemProcessor problemProcessor,
						BiConsumer<ExecStartingTask, ExecRunningTask> startedCallback,
						ExecRunningTask runningTask,
						ExecStoppingTask stoppingTask,
						ExecFinishedTask finishedTask) {

	super(type, 
			STARTING,
			executor,
			stdin,
			outputProcessorWrapper, 
			problemProcessorWrapper, 
			outputProcessor, 
			problemProcessor, 
			stoppingTask, 
			finishedTask);

	this.startedCallback = startedCallback;
	this.runningTask = runningTask;

	outputProcessor.setTask(this); 	// the processors need to mark us as NEXT or failed, so they need a ref to this
	problemProcessor.setTask(this);	// specific instance

}


public void setRunningTask(ExecRunningTask runningTask) {
	this.runningTask = runningTask;	//HACK: this is a hack as we are not creating singletons of running task in dagger
}


@Override
public RunningTask runningTask() throws IllegalStateException {
	return runningTask;
}


@Override
public RunningTask markAsStarted() {

	System.out.println("STARTING: Mark as started, about to redirect to running task");
	runningTask.startRedirectingOutput();
	status = STARTED;
	setRemaining(NEXT);

	startedCallback.accept(this, runningTask);

	return runningTask;

}


}
