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

package cat.calidos.morfeu.control.injection;

import java.util.Map;

import javax.inject.Named;

import cat.calidos.morfeu.webapp.injection.ControlModule;
import dagger.BindsInstance;
import dagger.Component;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = {ControlModule.class, PingControlModule.class, ContentControlModule.class})
public interface ControlComponent {

	//Map<String, BiFunction<List<String>, Map<String, String>, String>> controls();
	String process();
	boolean matches();

@Component.Builder
interface Builder {

	@BindsInstance Builder withPath(@Named("Path") String path);
	@BindsInstance Builder withParams(@Named("Params") Map<String, String> params);
	
	ControlComponent build();
}

}
