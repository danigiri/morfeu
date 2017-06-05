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

package cat.calidos.morfeu.model;

import java.net.URI;
import java.util.List;

import cat.calidos.morfeu.utils.Composite;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class CompositeCell extends Cell implements Composite<Cell> {

	public CompositeCell(URI u, String name, String desc) {
		super(u, name);
		this.desc = desc;
	}

	protected List<Cell> children;

	
	@Override
	public List<Cell> children() {
		return children;
	}
	

	@Override
	public Cell child(int i) {
		if (children==null) {
			throw new NullPointerException("Attempt to get cell on null list position "+i);
		}
		return children.get(i);
	}


	@Override
	public List<Cell> clearChildren() {

		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public int addChild(Cell c) {

		// TODO Auto-generated method stub
		return 0;
	}
}
