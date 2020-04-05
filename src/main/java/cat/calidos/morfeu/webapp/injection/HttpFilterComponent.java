package cat.calidos.morfeu.webapp.injection;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;

/** Generic filter component interface, meant to be subclassed
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={HttpFilterModule.class, ListeningExecutorServiceModule.class})
public interface HttpFilterComponent {

public static final String GET = "GET";
public static final String POST = "POST";

/**	@return true if we want to continue with filtering or false if we want to stop handling the request */
ListenableFuture<Boolean> process() throws MorfeuRuntimeException;

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder request(HttpServletRequest request);
	@BindsInstance Builder response(HttpServletResponse response);
	@BindsInstance Builder chain(FilterChain chain);

	HttpFilterComponent build();

}

}

/*
 *    Copyright 2019 Daniel Giribet
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

