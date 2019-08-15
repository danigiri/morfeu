// SERVLET CONFIG MODULE . JAVA

package cat.calidos.morfeu.webapp.injection;

import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.stream.StreamSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import dagger.Module;
import dagger.Provides;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ServletConfigModule {

@Provides
public static Properties getProperties(ServletConfig c) {

	Properties p = new Properties();
	Collections.list(c.getInitParameterNames()).forEach(name -> p.setProperty(name, c.getInitParameter(name)));

	ServletContext context = c.getServletContext();
	Iterable<String> iterable = () -> context.getAttributeNames().asIterator();
	StreamSupport.stream(iterable.spliterator(), false)
					.filter(a -> context.getAttribute(a) instanceof String)
					.forEach(a -> p.setProperty(a, (String)context.getAttribute(a)));

	p.putAll(System.getProperties());
	p.putAll(System.getenv());

	//p.propertyNames().asIterator().forEachRemaining(null);;
	
	return p;

}

}

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
