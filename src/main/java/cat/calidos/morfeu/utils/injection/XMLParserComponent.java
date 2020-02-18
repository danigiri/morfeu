package cat.calidos.morfeu.utils.injection;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.model.injection.StringToParsedModule;
import cat.calidos.morfeu.model.injection.XMLDocumentBuilderModule;

/** Plain vanilla XML parser
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = {StringToParsedModule.class, XMLDocumentBuilderModule.class, 
								ListeningExecutorServiceModule.class})
public interface XMLParserComponent {

ListenableFuture<org.w3c.dom.Document> document();

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder withContent(@Named("Content") String content);

	XMLParserComponent build();

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

