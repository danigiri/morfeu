package cat.calidos.morfeu.control.injection;

import javax.annotation.Nullable;
import javax.inject.Named;

import cat.calidos.morfeu.control.injection.OperationResultComponent.Builder;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;

/** Module to create a standarised operation result output
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class OperationResultModule {


private static final String TEMPLATE = "{" +
										"\"result\": \"[(${v.result})]\",\n" + 
										"\"target\": \"[(${v.target})]\",\n" + 
										"\"operation\": \"[(${v.operation})]\",\n" + 
										"\"operationTime\": [(${v.operationTime})]\n" + 
										"[# th:if=\"${hasProblem.isPresent}\"], \"problem\": \"[(${hasProblem.get})]\"[/]"+
										"}";

@Provides
public static String result(@Named("Result") String result,
							@Named("Target") String target,
							@Named("Operation") String op,
							long operationTime,
							@Nullable @Named("Problem")  String problem) {

	return DaggerViewComponent.builder()
								.withTemplate(TEMPLATE)
								.withValue(MorfeuUtils.paramMap("result", result,
																"target", target,
																"operation", op,
																"operationTime", operationTime))
								.andProblem(problem)
								.build()
								.render();
	
}

}

/*
 *    Copyright 2024 Daniel Giribet
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

