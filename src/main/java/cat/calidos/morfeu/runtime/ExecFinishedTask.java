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

import cat.calidos.morfeu.runtime.api.FinishedTask;


/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecFinishedTask extends ExecTask implements FinishedTask {


public ExecFinishedTask(int type, ProcessExecutor executor) {

	super(type, FINISHED, executor);

	this.setRemaining(NEXT);	// by definition we are done

}


@Override
public boolean isOK() {
	try {
		return result()==0;	// we override this method and look at the exit code, if interrupted, we assume KO
	} catch (InterruptedException e) {
		return false;
	}
}


@Override
public void waitFor() throws InterruptedException {
	while (process==null) {
		Thread.sleep(10);
	}
	process.waitFor();
}


@Override
public int result() throws InterruptedException {

	while (process==null) {
		Thread.sleep(10);
	}
	
	return process.exitValue();

}


@Override
public void setRemaining(int percent) {

	// TODO Auto-generated method stub
	
}


@Override
public int getRemaining() {
	return NEXT;	// by definition we do not have anything left to do
}


@Override
public String show() {

	// TODO Auto-generated method stub
	return null;
}

}
