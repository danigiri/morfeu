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

package cat.calidos.morfeu.model.injection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.xsom.parser.XSOMParser;

import cat.calidos.morfeu.injection.ApplicationScoped;
import dagger.Component;
import dagger.Subcomponent;
import dagger.producers.ProductionComponent;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
@Component(dependencies = ParserModule.class)
public interface ParserComponent {

ObjectMapper provideJSONObjectMapper();

}
