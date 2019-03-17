// MODEL PARSER COMPONENT . JAVA

package cat.calidos.morfeu.model.injection;

import com.google.common.util.concurrent.ListenableFuture;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;
import dagger.producers.ProductionComponent;

/** This is at the moment only used for testing, as SchemaParserModule is included directly as a dependency
* 	(Eclipse gets confused about this one, so moving it from the test location to here)
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={ModelParserModule.class, ListeningExecutorServiceModule.class})
public interface ModelParserComponent {

ListenableFuture<XSOMParser> produceXSOMParser() throws ConfigurationException;

}

/*
 *    Copyright 2019 Daniel Giribet
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