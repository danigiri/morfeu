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

package cat.calidos.morfeu.model.injection;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import dagger.Module;
import dagger.Provides;
import dagger.producers.Production;

@Module
public final class ListeningExecutorServiceModule {
	
	@Provides
	@Production
	public static ListeningExecutorService executor() {
		// Following the Dagger 2 official docs, this private executor pool should be OK for just the HTTP stuff
		return MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
	}
	
}