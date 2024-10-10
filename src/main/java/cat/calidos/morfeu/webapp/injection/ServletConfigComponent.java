// SERVLET CONFIG COMPONENT . JAVA

package cat.calidos.morfeu.webapp.injection;

import java.util.Properties;

import javax.annotation.Nullable;

import dagger.BindsInstance;
import dagger.Component;

import jakarta.servlet.ServletConfig;


/**
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////
@Component(modules = ServletConfigModule.class)
public interface ServletConfigComponent {

/**
 * @return merge of configuration properties, from servlet to java system, and env vars (from less
 *         to more priority)
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
Properties getProperties();

//@formatter:off
@Component.Builder
interface Builder {

	@BindsInstance Builder with(@Nullable ServletConfig c); // will use empty configuration if not set

	ServletConfigComponent build();

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
