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
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSType;

import cat.calidos.morfeu.model.CellModel;
import dagger.Module;
import dagger.Provides;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class CellModelModule {

protected final static Logger log = LoggerFactory.getLogger(CellModelModule.class);


@Provides
public static CellModel buildCellModelFrom(XSElementDecl elem) throws RuntimeException {
	Locator locator = elem.getLocator();
	CellModel cellModel = null;
	try {
		//TODO: build a real URI that can be accessed in the future
		URI u = new URI(locator.getSystemId());
		cellModel = new CellModel(u, elem.getName());
	} catch (URISyntaxException e) {
		log.error("What the heck, URI '{}' of element '{}' is not valid ", locator.getSystemId(), elem.getName());
	}
	
	XSType type = elem.getType();
//	type.get
	
	return cellModel;

}


}
