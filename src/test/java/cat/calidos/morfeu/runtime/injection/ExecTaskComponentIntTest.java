// EXEC TASK COMPONENT INT TEST . JAVA

package cat.calidos.morfeu.runtime.injection;

import static org.junit.Assert.*;

import org.junit.Test;

import cat.calidos.morfeu.runtime.api.FinishedTask;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.RunningTask;
import cat.calidos.morfeu.runtime.api.StartingTask;
import cat.calidos.morfeu.runtime.api.StoppingTask;
import cat.calidos.morfeu.runtime.api.Task;
import cat.calidos.morfeu.runtime.injection.DaggerExecTaskComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ExecTaskComponentIntTest {

private static final int FIVESECS = 5000;
private boolean startedCallbackCalled = false;
private boolean finishedCallbackCalled = false;


@Test 
public void testOneTimeExecSimpleTask() throws Exception {

	System.out.println("TEST: START");
	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'hello world' && sleep 1")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("hello world") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> true)	// if anything shows on STDERR
												.build()
												.readyTask();
	assertFalse("Task not started should not be 'done'", task.isDone());
	assertEquals("Task not started should be ready", Task.READY, task.getStatus());
	System.out.println("TEST: about to call start");
	StartingTask starting = task.start();
//	assertEquals("ready tasks should have transitioned to started", Task.STARTED, task.getStatus());
	starting.spinUntil(Task.STARTED);
	assertEquals("hello world\n", starting.show());

	RunningTask running = starting.runningTask();
	running.spinUntil(Task.FINISHED);

	FinishedTask finishedTask = running.finishedTask();
	assertTrue("Task finished should be 'done'", finishedTask.isDone());
	assertTrue(finishedTask.isOK());
	assertEquals(0, finishedTask.result());

}


@Test
public void testOneTimeExecComplexTask() throws Exception {
	
	String command = "echo 'started' && sleep 1 && echo 100 && sleep 1 && echo 50 && echo 0";
	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec("/bin/bash", "-c", command)
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.startedCallback((s, r) -> {startedCallbackCalled = true;})
												.runningMatcher(s -> Integer.parseInt(s))
												.finishedCallback((r, f) -> {finishedCallbackCalled = true;})
												.problemMatcher(s -> true)
												.build()
												.readyTask();
	
	assertFalse(task.isDone());
	assertEquals(Task.READY, task.getStatus());
	
	StartingTask start = task.start();
	start.spinUntil(Task.STARTED, FIVESECS);
	assertEquals("started\n", start.show());
	
	RunningTask running = start.runningTask();
	running.spinUntil(Task.FINISHED);
	assertEquals("100\n50\n0\n", running.show());
	assertTrue("Start callback was not called at all", startedCallbackCalled);
	assertTrue("Finished callback was not called at all", finishedCallbackCalled);

	FinishedTask finished = running.finishedTask();
	assertTrue(finished.isOK());
	assertEquals(0, finished.result());

}


@Test
public void testOneTimeExecProblematicTask() throws Exception {

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", "echo 'started' && sleep 2 && notfound")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> s.contains("command not found"))
												.build()
												.readyTask();
	assertFalse(task.isDone());
	assertEquals(Task.READY, task.getStatus());

	StartingTask start = task.start();
	start.spinUntil(Task.STARTED);
	assertTrue("Started task should be completed", start.isDone());
	assertTrue("Started task should be OK as failure is in the running state", start.isOK());
	assertEquals("started\n", start.show());
	
	RunningTask runningTask = start.runningTask();
	
	try {
		runningTask.spinUntil(Task.FINISHED); 
	} catch (InterruptedException e) {
		assertFalse("Spinning throwing exception should not be OK", runningTask.isOK());
	} // task may finish before throwing the exception
	assertTrue("Running failed task should also be done", runningTask.isDone());
	
	FinishedTask finishedTask = runningTask.finishedTask();
	finishedTask.waitFor();
	assertFalse("finished failed task should not be OK", finishedTask.isOK());
	assertEquals("result of command not found should be 127", 127, finishedTask.result());
	
}


//@Test // SKIPPED
public void testOneTimeBinaryNotFoundTask() {
	// can't avoid the stacktrace being printed, so disabling for the time being

	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/NOTFOUNDFORSURE")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> s.contains("command not found"))
												.build()
												.readyTask();
//	assertAll("problematic task",
//			() -> assertFalse(task.isDone()),
//			() -> assertEquals(Task.READY, task.getStatus())
//	);
//	assertThrows(EurinomeRuntimeException.class, () -> task.start());

}


@Test
public void testStopOneTimeStartingTask() throws Exception {

	String command = "trap 'sleep 2 && echo stopped' SIGTERM; sleep 500 & wait %1";
	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash", "-c", command)
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)
												.stoppedMatcher(s -> s.equals("stopped") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> !s.contains("hangup"))
												.build()
												.readyTask();
	assertFalse(task.isDone());
	assertEquals(Task.READY, task.getStatus());

	StartingTask starting = task.start();
	assertFalse(starting.isDone());

	int status = starting.getStatus();
	assertEquals("Starting task should be STARTING ("+starting.translate(status)+")", Task.STARTING, status);

	StoppingTask stopping = starting.stop();
	assertNotNull(stopping);
	assertEquals(Task.STOPPED, starting.getStatus());
	assertFalse(stopping.isDone());

	status = stopping.getStatus();
	assertEquals("Starting task should be STOPPING ("+starting.translate(status)+")", Task.STOPPING, status);

	stopping.spinUntil(Task.FINISHED);
	// FIXME: this is brittle for some reason
	//System.err.println("stopping --->"+stopping.show());
	//assertEquals("stopped\n", stopping.show());

}


@Test
public void testStopOneTimeRunningTask() throws Exception {
	
	String command = "trap 'sleep 1 && echo 0' SIGTERM; echo 'started' && sleep 500 & wait %1";
	ReadyTask task = DaggerExecTaskComponent.builder()
			.exec( "/bin/bash", "-c", command)
			.type(Task.ONE_TIME)
			.startedMatcher(s -> s.equals("started") ? Task.NEXT : Task.MAX)	// this matches
			.stoppedMatcher(s -> Integer.parseInt(s))
			.problemMatcher(s -> true)	// if anything shows on STDERR
			.build()
			.readyTask();
	assertFalse(task.isDone());
	assertEquals(Task.READY, task.getStatus());

	StartingTask start = task.start();
	start.spinUntil(Task.STARTED);
	assertEquals("started\n", start.show());

	RunningTask running = start.runningTask();
	assertFalse(running.isDone());
	assertEquals(Task.RUNNING, running.getStatus());

	StoppingTask stopping = running.stop();
	assertTrue(running.isDone());

	int status = running.getStatus();
	assertEquals("Stopped task should not be ("+running.translate(status)+")", Task.STOPPED, status);
	assertNotNull(stopping);

	stopping.spinUntil(Task.FINISHED);
	assertEquals("0\n",stopping.show());
	assertTrue(stopping.isDone());
	assertEquals(Task.FINISHED, stopping.getStatus());

	FinishedTask finished = stopping.finishedTask();
	finished.waitFor();
	assertFalse("finished failed task should not be OK", finished.isOK());
	assertEquals("result of stopped task should be 143", 143, finished.result());

}


//@Test 
public void testOneTimeExecSTDINTask() throws Exception {

	// this blocks so can't run at the moment
	ReadyTask task = DaggerExecTaskComponent.builder()
												.exec( "/bin/bash")
												.type(Task.ONE_TIME)
												.startedMatcher(s -> s.equals("hello world") ? Task.NEXT : Task.MAX)
												.problemMatcher(s -> true)	// if anything shows on STDERR
												.build()
												.readyTask();
	assertFalse("Task not started should not be 'done'", task.isDone());
	assertEquals("Task not started should be ready", Task.READY, task.getStatus());
	StartingTask starting = task.start("hello world");
	starting.spinUntil(Task.STARTED);
	assertEquals("hello world\n", starting.show());
	
	RunningTask running = starting.runningTask();
	running.spinUntil(Task.FINISHED);
	
	FinishedTask finishedTask = running.finishedTask();
	assertTrue("Task finished should be 'done'", finishedTask.isDone());
	assertTrue(finishedTask.isOK());
	assertEquals(0, finishedTask.result());

}

}

/*
 *    Copyright 2020 Daniel Giribet
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

