// SQL VIEW COMPONENT . JAVA

package cat.calidos.morfeu.utils.injection;

import java.sql.Connection;
import java.util.List;

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import cat.calidos.morfeu.problems.ParsingException;


@ProductionComponent(modules = { SQLModule.class, ListeningExecutorServiceModule.class })
public interface SQLComponent {

ListenableFuture<List<List<String>>> query() throws ParsingException, MorfeuRuntimeException;

ListenableFuture<Integer> update() throws ParsingException, MorfeuRuntimeException;

//@formatter:off
@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder sql(@Named("SQL") String query);
	@BindsInstance Builder andConnection(Connection connection);
	SQLComponent build();

}
//@formatter:on

}

/*
 * Copyright 2024 Daniel Giribet
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
