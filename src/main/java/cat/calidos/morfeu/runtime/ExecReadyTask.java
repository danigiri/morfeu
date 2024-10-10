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

import java.io.IOException;
import java.util.Optional;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.StartingTask;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecReadyTask extends ExecTask implements ReadyTask {

private ProcessExecutor		executor;
private StartedProcess		startedProcess;
private ExecStartingTask	startingTask;
private ExecRunningTask		runningTask;
private ExecStoppingTask	stoppingTask;
private ExecFinishedTask	finishedTask;

public ExecReadyTask(	int type,
						ProcessExecutor executor,
						ExecStartingTask startingTask,
						ExecRunningTask runningTask,
						ExecStoppingTask stoppingTask,
						ExecFinishedTask finishedTask) {

	super(type, READY, executor); // no matchers for ready tasks

	this.executor = executor;
	this.startingTask = startingTask;
	this.runningTask = runningTask;
	this.stoppingTask = stoppingTask;
	this.finishedTask = finishedTask;

	startingTask.setRunningTask(runningTask); // enforce the same instance

}


@Override
public StartingTask start(Optional<String> stdin) throws MorfeuRuntimeException {

	// race condition here, stdout and stderr are to all intents and purposes being examined
	// simultaneously, this
	// means that in the case of an error, we can have the following cases:
	// a)
	// starting:
	// STDIN (match)
	// STDERR (match)
	// markasFinished(starting)
	// markAsFailed(starting)
	// b)
	// starting:
	// STDIN (match)
	// markasFinished(starting)
	// running:
	// STDERR (match)
	// markAsFailed(running)
	// c)
	// starting:
	// STDERR (match)
	// STDIN (match)
	// markasFinished(starting)
	// running:
	// markAsFailed(running)
	// and a few other cases...
	// GUARD 1:
	// We synchronize the setRemaning method, plus it cannot go up on value, so we ensure that we
	// always set
	// startedTask to NEXT eventually. (Either the problem sets it to NEXT or it's marked complete
	// by runningtask)
	// GUARD 2:
	// Moreover, status is untouched/undefined when erroring, so if we have:
	// a)
	// status=STARTED
	// isOK=FALSE
	// b)
	// isOK=false
	// (status is not changed and therefore undefined)
	// c)
	// isOK=FALSE
	// status=STARTED
	// we still maintain the invariant.
	// GUARD 3:
	// only setKO implemented, isOK is private, so isOK is never set to true, we cannot override
	// with OK
	// GUARD 4:
	// TODO: FIXME: isOK() checks previous tasks so if any is not OK, we're not OK, as the most
	// conservative
	//
	// In summary: a) remaining is sync and always goes down, cannot be overriden and go up
	// b) ok variable cannot be set to true, cannot be overriden and go true
	// c) if any of the tasks fails, we are marked as failed

	status = STARTING;
	if (stdin.isPresent()) {
		startingTask.redirectInput(stdin.get());
	}
	startingTask.startRedirectingOutput();

	try {
		startedProcess = executor.start();
		// FIXME: race condition here, we may be finished already if the process starts and finishes
		// fast
		process = startedProcess.getProcess();
		startingTask.setProcess(process);
		runningTask.setProcess(process);
		stoppingTask.setProcess(process);
		finishedTask.setProcess(process);
	} catch (IOException e) {
		throw new MorfeuRuntimeException("Had a problem launching " + executor.getCommand(), e);
	}

	startedProcess.getProcess().onExit().thenAccept(a -> runningTask.markAsFinished());

	return startingTask;

}


@Override
public void setRemaining(int percent) {
}


@Override
public int getRemaining() {
	return MAX;
}


@Override
public String show() {
	return "READY TASK [" + executor.getCommand() + "]";
}


@Override
public String toString() {
	return show();
}

}
