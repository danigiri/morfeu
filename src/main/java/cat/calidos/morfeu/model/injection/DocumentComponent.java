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

import java.net.URI;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.model.Document;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.utils.injection.HttpClientModule;
import cat.calidos.morfeu.utils.injection.JSONMapperModule;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;
import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={DocumentModule.class, HttpClientModule.class, JSONMapperModule.class, 
							  ListeningExecutorServiceModule.class})
public interface DocumentComponent {

ListenableFuture<Document> produceDocument() throws ParsingException, FetchingException, ValidationException;

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder from(URI u);
	@BindsInstance Builder withPrefix(@Named("Prefix") String p);
	DocumentComponent build();

}

}
