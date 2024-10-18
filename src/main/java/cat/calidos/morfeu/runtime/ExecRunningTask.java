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

import java.util.function.BiConsumer;

import org.zeroturnaround.exec.ProcessExecutor;

import cat.calidos.morfeu.runtime.api.FinishedTask;
import cat.calidos.morfeu.runtime.api.RunningTask;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecRunningTask extends ExecMutableTask implements RunningTask {

private ExecStoppingTask								stoppingTask;
private ExecFinishedTask								finishedTask;
private BiConsumer<ExecRunningTask, ExecFinishedTask>	finishedCallBack;

public ExecRunningTask(	int type, ProcessExecutor executor,
						ExecOutputProcessor outputProcessorWrapper,
						ExecProblemProcessor problemProcessorWrapper,
						RunningOutputProcessor outputProcessor,
						ExecProblemProcessor problemProcessor, ExecStoppingTask stoppingTask,
						ExecFinishedTask finishedTask,
						BiConsumer<ExecRunningTask, ExecFinishedTask> finishedCallBack) {

	super(type, RUNNING, executor, outputProcessorWrapper, problemProcessorWrapper, outputProcessor,
			problemProcessor, stoppingTask, finishedTask);

	this.finishedCallBack = finishedCallBack;

	outputProcessor.setTask(this);
	problemProcessor.setTask(this);

}

//
// @Override
// public StoppingTask stop() {
//
// status = STOPPED;
//
// return stoppingTask;
//
// }


@Override
public FinishedTask markAsFinished() {

	System.out.println("RUNNING: [Mark as finished]");
	status = FINISHED;
	setRemaining(NEXT);
	finishedCallBack.accept(this, finishedTask);

	return finishedTask();

}

}
