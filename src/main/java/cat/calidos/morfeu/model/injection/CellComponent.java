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

import java.net.URI;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules=CellModule.class)
public interface CellComponent {

Cell createCell();

@Component.Builder
interface Builder {
	@BindsInstance Builder fromURI(URI u);
	@BindsInstance Builder named(@Named("name") String name);	
	@BindsInstance Builder withDesc(@Named("desc") String desc);
	@BindsInstance Builder value(@Named("value") String value);
	@BindsInstance Builder withCellModel(CellModel cm);
	CellComponent builder();
}

}
