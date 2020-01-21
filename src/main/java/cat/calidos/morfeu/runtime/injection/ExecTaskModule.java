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

package cat.calidos.morfeu.runtime.injection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import org.apache.commons.io.IOUtils;
import org.zeroturnaround.exec.ProcessExecutor;

import cat.calidos.morfeu.runtime.ExecFinishedTask;
import cat.calidos.morfeu.runtime.ExecOutputProcessor;
import cat.calidos.morfeu.runtime.ExecProblemProcessor;
import cat.calidos.morfeu.runtime.ExecReadyTask;
import cat.calidos.morfeu.runtime.ExecRunningTask;
import cat.calidos.morfeu.runtime.ExecStartingTask;
import cat.calidos.morfeu.runtime.ExecStoppingTask;
import cat.calidos.morfeu.runtime.RunningOutputProcessor;
import cat.calidos.morfeu.runtime.StartingOutputProcessor;
import cat.calidos.morfeu.runtime.StoppingOutputProcessor;
import cat.calidos.morfeu.runtime.api.ReadyTask;
import cat.calidos.morfeu.runtime.api.Task;
import cat.calidos.morfeu.utils.Config;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ExecTaskModule {


@Provides
ReadyTask readyTask(@Named("Type") int type,
					ProcessExecutor executor,
					@Named("ProblemMatcher") Predicate<String> problemMatcher,
					ExecStartingTask startingTask,
					ExecRunningTask runningTask,
					ExecStoppingTask stoppingTask,
					ExecFinishedTask finishedTask) {
	return new ExecReadyTask(type, executor, startingTask, runningTask, stoppingTask, finishedTask);
}


@Provides @Singleton
ProcessExecutor executor(@Named("Path") String... command) {
	return new ProcessExecutor().command(command);
}


@Provides @Singleton
ExecStartingTask startingTask(@Named("Type") int type,
								ProcessExecutor executor,
								@Named("OutputWrapper") ExecOutputProcessor outputProcessorWrapper,
								@Named("ProblemWrapper") ExecProblemProcessor problemProcessorWrapper,
								StartingOutputProcessor startingOutputProcessor,
								@Named("StartingProblemProcessor") ExecProblemProcessor problemProcessor,
								@Named("StartedCallback") BiConsumer<ExecStartingTask, ExecRunningTask> started,
								ExecRunningTask runningTask,
								ExecStoppingTask stoppingTask,
								ExecFinishedTask finishedTask) {
	return new ExecStartingTask(type,
								executor,
								outputProcessorWrapper,
								problemProcessorWrapper,
								startingOutputProcessor,
								problemProcessor,
								started,
								runningTask,
								stoppingTask,
								finishedTask);
}


@Provides @Singleton @Named("OutputWrapper") ExecOutputProcessor outputProcessorWrapper() {
	return new ExecOutputProcessor();
}


@Provides @Singleton @Named("ProblemWrapper") ExecProblemProcessor problemProcessorWrapper() {
	return new ExecProblemProcessor(); 
}


@Provides @Singleton
StartingOutputProcessor startingOutputProcessor(@Named("StartedMatcher") Function<String, Integer> matcher) {
	return new StartingOutputProcessor(matcher);
}


@Provides @Singleton @Named("StartingProblemProcessor") 
ExecProblemProcessor startingProblemProcessor(@Named("ProblemMatcher") Predicate<String> problemMatcher) {
	return new ExecProblemProcessor(problemMatcher);
}


@Provides @Singleton
ExecRunningTask runningTask(@Named("Type") int type,
							ProcessExecutor executor,
							@Named("OutputWrapper") ExecOutputProcessor outputProcessorWrapper,
							@Named("ProblemWrapper") ExecProblemProcessor problemProcessorWrapper,
							RunningOutputProcessor runningOutputProcessor,
							@Named("RunningProblemProcessor") ExecProblemProcessor problemProcessor,
							ExecStoppingTask stoppingTask,
							ExecFinishedTask finishedTask,
							@Named("FinishedCallback") BiConsumer<ExecRunningTask, ExecFinishedTask> callback) {
	return new ExecRunningTask(type, 
								executor, 
								outputProcessorWrapper,
								problemProcessor,
								runningOutputProcessor, 
								problemProcessor, 
								stoppingTask,
								finishedTask, 
								callback);
}


@Provides @Singleton
RunningOutputProcessor runningOutputProcessor(@Named("EffectiveRunningMatcher") Function<String, Integer> matcher) {
	return new RunningOutputProcessor(matcher);
}


@Provides @Singleton @Named("RunningProblemProcessor") 
ExecProblemProcessor problemProcessor(@Named("ProblemMatcher") Predicate<String> problemMatcher) {
	return new ExecProblemProcessor(problemMatcher);
}


@Provides @Singleton
ExecStoppingTask stoppingTask(@Named("Type") int type,
								ProcessExecutor executor,
								@Named("OutputWrapper") ExecOutputProcessor outputProcessorWrapper,
								@Named("ProblemWrapper") ExecProblemProcessor problemProcessorWrapper,
								@Named("EffectiveStoppingProcessor") StoppingOutputProcessor stoppingProcessor,
								@Named("StoppingProblemProcessor") ExecProblemProcessor stoppingProblemProcessor,
								ExecFinishedTask finishedTask) {
	return new ExecStoppingTask(type, 
								executor, 
								outputProcessorWrapper,
								problemProcessorWrapper, 
								stoppingProcessor,
								stoppingProblemProcessor, 
								finishedTask);
}


@Provides @Singleton @Named("EffectiveStoppingProcessor")
StoppingOutputProcessor stoppingProcessor(@Named("EffectiveStoppedMatcher") Function<String, Integer> stoppedMatcher) {
	return new StoppingOutputProcessor(stoppedMatcher);
}



@Provides @Singleton @Named("StoppingProblemProcessor") 
ExecProblemProcessor stoppingProblemProcessor(@Named("ProblemMatcher") Predicate<String> problemMatcher) {
	return new ExecProblemProcessor(problemMatcher);
}



@Provides @Singleton
ExecFinishedTask finishedTask(@Named("Type") int type, ProcessExecutor executor) {
	return new ExecFinishedTask(type, executor);
}


@Provides @Named("StartedCallback") 
BiConsumer<ExecStartingTask, ExecRunningTask> startedCallback(
												@Nullable BiConsumer<ExecStartingTask, ExecRunningTask> callback) {
	return (callback==null)? (s, r) -> {} : callback;
}


@Provides @Named("EffectiveRunningMatcher") 
Function<String, Integer> matcher(@Nullable @Named("RunningMatcher") Function<String, Integer> matcher) {
	return (matcher==null)? s -> Task.MAX : matcher;	// default matcher is progress is always 100%, wait for process
}														// to finish on its own


@Provides @Named("EffectiveStoppedMatcher") 
Function<String, Integer> stoppedMatcher(@Nullable @Named("StoppedMatcher") Function<String, Integer> matcher) {
	return (matcher==null)? s -> Task.MAX : matcher;	// default matcher is progress is always 100%, wait for process
}														// to finish on its own


@Provides @Named("FinishedCallback") 
BiConsumer<ExecRunningTask, ExecFinishedTask> finishedCallback(
												@Nullable BiConsumer<ExecRunningTask, ExecFinishedTask> callback) {
	return (callback==null)? (s, r) -> {} : callback;
}


}
