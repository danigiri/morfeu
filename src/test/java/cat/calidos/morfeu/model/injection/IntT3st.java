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

import java.util.concurrent.ExecutionException;

import cat.calidos.morfeu.model.Document;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class IntT3st {

protected String uriModuleForPath(String path) {
	return this.getClass().getClassLoader().getResource(path).toString();
}

protected Document produceDocumentFromPath(String path) throws InterruptedException, ExecutionException {
	
	String doc1Path = uriModuleForPath(path);
	URIModule uriModule = new URIModule(doc1Path);
	DocumentComponent docComponent = DaggerDocumentComponent.builder().URIModule(uriModule).build();
	
	return docComponent.produceDocument().get();

}


}
