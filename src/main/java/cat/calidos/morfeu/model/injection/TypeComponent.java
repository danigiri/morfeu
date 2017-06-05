/*
 *    Copyright 2017 Daniel Giribet
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

import com.sun.xml.xsom.XSType;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Type;
import dagger.BindsInstance;
import dagger.Component;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules=TypeModule.class)
public interface TypeComponent {

Type type();

@Component.Builder
interface Builder {

	@BindsInstance Builder withXSType(XSType xsType);
	@BindsInstance Builder withDefaultName(String name);
	
	TypeComponent build();
}
}
