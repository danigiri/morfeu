// VIEW COMPONENT . JAVA

package cat.calidos.morfeu.view.injection;

import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = ViewModule.class)
public interface ViewComponent {

String render();

//@formatter:off
@Component.Builder
interface Builder {

	@BindsInstance Builder withValue(@Nullable @Named("value") Object value); // we set v.<value>
	@BindsInstance Builder withValues(@Nullable Map<Object, Object> values); // we set values directly, can override all 
	@BindsInstance Builder withTemplatePath(@Nullable @Named("templatePath") String p);
	@BindsInstance Builder withTemplate(@Nullable @Named("template") String p);
	@BindsInstance Builder andProblem(@Nullable @Named("problem") String p);

	ViewComponent build();

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
