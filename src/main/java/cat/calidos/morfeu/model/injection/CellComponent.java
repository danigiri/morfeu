// CELL COMPONENT . JAVA

package cat.calidos.morfeu.model.injection;

import java.net.URI;

import org.w3c.dom.Node;

import dagger.BindsInstance;
import dagger.Component;

import cat.calidos.morfeu.model.Cell;
import cat.calidos.morfeu.model.CellModel;


/**
 * Cell: content element, always related to a cell model
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = CellModule.class)
public interface CellComponent {

/** @return the instance of the cell, given the DOM Node, and the supplied cell model */
Cell createCell();

//@formatter:off
@Component.Builder
interface Builder {

	@BindsInstance Builder withURI(URI u);
	@BindsInstance Builder fromNode(Node node);
	@BindsInstance Builder withCellModel(CellModel cm);

	CellComponent build();

}
//@formatter:on

}

/*
 * Copyright 2019 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
