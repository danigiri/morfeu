package cat.calidos.morfeu.control.injection;

import dagger.BindsInstance;
import dagger.Component;

import cat.calidos.morfeu.problems.MorfeuException;
import cat.calidos.morfeu.webapp.control.problems.WebappRuntimeException;


/**
 * Translate exceptions between domains, currently the only use case is from controller to webapp
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = MorfeuExceptionToWebappExceptionModule.class)
public interface MorfeuExceptionTranslatorComponent {

WebappRuntimeException toWebappException();

//@formatter:off
@Component.Builder
interface Builder {

	@BindsInstance Builder from(MorfeuException e);

	MorfeuExceptionTranslatorComponent build();

}
//@formatter.on


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
