package cat.calidos.morfeu.control.injection;

import javax.annotation.Nullable;
import javax.inject.Named;

import cat.calidos.morfeu.webapp.injection.ControlComponent.Builder;
import dagger.BindsInstance;
import dagger.Component;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules=OperationResultModule.class)
public interface OperationResultComponent {

String result();

@Component.Builder
interface Builder {

	@BindsInstance Builder result(@Named("Result") String result);
	@BindsInstance Builder target(@Named("Target") String target);
	@BindsInstance Builder operation(@Named("Result") String result);
	@BindsInstance Builder operationTime(long result);
	@BindsInstance Builder problem(@Nullable @Named("Problem")  String problem);
	
	
	OperationResultComponent build();

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

