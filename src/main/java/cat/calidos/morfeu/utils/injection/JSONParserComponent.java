// JSON PARSER COMPONENT . JAVA

package cat.calidos.morfeu.utils.injection;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.problems.ParsingException;
import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={JSONParserModule.class, ListeningExecutorServiceModule.class})
public interface JSONParserComponent {

ListenableFuture<JsonNode> json() throws ParsingException;
@Named("pretty") ListenableFuture<String> pretty() throws ParsingException;

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder from(String content);
	JSONParserComponent build();

}

}

/*
 *    Copyright 2024 Daniel Giribet
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
