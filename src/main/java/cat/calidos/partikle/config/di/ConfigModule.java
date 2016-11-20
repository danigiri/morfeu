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

package cat.calidos.partikle.config.di;

import java.util.Collections;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.servlet.ServletConfig;

import dagger.Module;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
@Module
public class ConfigModule {

public Properties provideConfigWithServletConfig(@Nonnull ServletConfig c) {
	
	Properties config = new Properties();
	Collections.list(c.getInitParameterNames())
		.forEach(name -> config.setProperty(name, c.getInitParameter(name)));

	return config;

}

}
