package cat.calidos.morfeu.view.injection;

import java.sql.Connection;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = SQLViewModule.class)
public interface SQLViewComponent {

String render();

//@formatter:off
@Component.Builder
interface Builder {

	@BindsInstance Builder query(@Named("query") String query);
	@BindsInstance Builder isUpdate(@Nullable @Named("isUpdate") boolean isUpdate);
	@BindsInstance Builder andConnection(Connection connection);

	SQLViewComponent build();

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
