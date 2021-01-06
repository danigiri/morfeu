// LISTENING EXECUTOR SERVICE MODULE . JAVA

package cat.calidos.morfeu.utils.injection;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;
import dagger.producers.Production;


//FIXME: this should be a singleton that is reused between requests, etc.
@Module
public final class ListeningExecutorServiceModule {

protected final static Logger log = LoggerFactory.getLogger(ListeningExecutorServiceModule.class);
public final static ExecutorService executor = Executors.newCachedThreadPool();

@Provides
@Production
public static Executor executor() {
	// this has been used to prove that the 'protected final static' means that we reuse the thread pool, yay!
	log.trace("[Executor producer called] ({})", executor.hashCode());
	return executor;
}


}

/*
 *    Copyright 2016 Daniel Giribet
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