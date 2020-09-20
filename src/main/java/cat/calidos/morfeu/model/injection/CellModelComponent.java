// CELL MODEL COMPONENT . JAVA
package cat.calidos.morfeu.model.injection;

import java.net.URI;
import java.util.Map;

import javax.inject.Named;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Metadata;
import dagger.BindsInstance;
import dagger.Component;


/** Semantic model of a cell. It can be a reference to other existing cell models
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = CellModelModule.class)
public interface CellModelComponent {

/**	@return instance of the cell model, given the XSD definition data*/
CellModel cellModel();


@Component.Builder
interface Builder {

	@BindsInstance Builder fromElem(XSElementDecl elem);
	@BindsInstance Builder fromParticle(XSParticle particle);
	@BindsInstance Builder withParentURI(@Named("ParentURI") URI uri);
	@BindsInstance Builder withGlobalMetadata(Map<URI, Metadata> globalMetadata);
	@BindsInstance Builder andExistingGlobals(Map<String, CellModel> globals);

	CellModelComponent build();

}

}

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

