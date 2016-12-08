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

package cat.calidos.partikle.webapp.di;

import java.util.Collections;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.servlet.ServletConfig;

import dagger.Module;
import dagger.Provides;

/**
* @author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
@Module
public class ServletConfigModule {

private ServletConfig servletConfig;

public ServletConfigModule(ServletConfig c) {
	this.servletConfig = c;
}

@Provides
public Properties provideConfig() {
	
	Properties p = new Properties();
	Collections.list(servletConfig.getInitParameterNames())
		.forEach(name -> p.setProperty(name, servletConfig.getInitParameter(name)));

	return p;

}

}
