package cat.calidos.morfeu.runtime;

import java.io.IOException;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.process.Processes;
import org.zeroturnaround.process.SystemProcess;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import cat.calidos.morfeu.runtime.api.StoppingTask;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecStoppingTask extends ExecMutableTask implements StoppingTask {

public ExecStoppingTask(int type, ProcessExecutor executor,
						ExecOutputProcessor outputProcessorWrapper,
						ExecProblemProcessor problemProcessorWrapper,
						StoppingOutputProcessor outputProcessor,
						ExecProblemProcessor problemProcessor, ExecFinishedTask finishedTask) {

	super(type, STOPPING, executor, outputProcessorWrapper, problemProcessorWrapper,
			outputProcessor, problemProcessor, null, finishedTask);

	outputProcessor.setTask(this);
	problemProcessor.setTask(this);
}


@Override
public StoppingTask stop() throws MorfeuRuntimeException {

	int pid = (int) process.pid();
	SystemProcess systemprocess = Processes.newPidProcess(pid); // race condition, pid may not exist
																// from the call
	try { // to the destroy :)
		systemprocess.destroyForcefully();
	} catch (IOException | InterruptedException e) {
		throw new MorfeuRuntimeException("Problem killing stopping process", e);
	}

	return this;

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
