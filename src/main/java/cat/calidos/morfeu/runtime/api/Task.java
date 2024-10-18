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

package cat.calidos.morfeu.runtime.api;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public interface Task extends Showable {

public static int	READY		= 1001;
public static int	STARTING	= 1002;
public static int	STARTED		= 1003;
public static int	RUNNING		= 1004;
public static int	STOPPED		= 1005;
public static int	STOPPING	= 1006;
public static int	FINISHED	= 1007;

public static int	NEXT	= 0;
public static int	MAX		= 100;

public static int	ONE_TIME		= 1000;
public static int	LONG_RUNNING	= 1100;

/** @return */
public int getStatus();

/** @return */
public int getType();


default public boolean isOneTime() { return getType() == Task.ONE_TIME; }


default public boolean isLongRunning() { return getType() == Task.LONG_RUNNING; }


public void setRemaining(int percent);

public int getRemaining();

public boolean isOK();


default public boolean isDone() { return getRemaining() <= Task.NEXT || getStatus() == FINISHED; }


/** Used in testing, block until status is reached @throws InterruptedException */
default void spinUntil(int status) throws InterruptedException {

	while (getStatus() != status && isOK()) {
		Thread.sleep(10); // this seems more effective than spinwait
	}
	if (!isOK()) {
		throw new InterruptedException("While waiting, the task failed");
	}

}


default void spinUntil(	int status,
						int timeoutMillis)
		throws InterruptedException {
	long t = System.currentTimeMillis();
	while (getStatus() != status && isOK() && System.currentTimeMillis() - t < timeoutMillis) {
		Thread.sleep(10); // this seems more effective than spinwait
	}
	if (!isOK()) {
		throw new InterruptedException("While waiting, the task failed");
	}

}


default String translate(int status) {

	switch (status) {
		case READY:
			return "READY";
		case STARTING:
			return "STARTING";
		case STARTED:
			return "STARTED";
		case RUNNING:
			return "RUNNING";
		case STOPPED:
			return "STOPPED";
		case STOPPING:
			return "STOPPING";
		case FINISHED:
			return "FINISHED";
	}
	throw new IllegalStateException("Attempting to translate an unkown state");

}

}
