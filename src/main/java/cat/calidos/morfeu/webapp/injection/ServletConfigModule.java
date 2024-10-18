// SERVLET CONFIG MODULE . JAVA

package cat.calidos.morfeu.webapp.injection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import dagger.Module;
import dagger.Provides;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ServletConfigModule {

@Provides
public static Properties getProperties(	@Nullable ServletConfig config,
										Optional<ServletContext> servletContext,
										List<String> parameterNames,
										Iterable<String> attributeNames) {

	Properties p = new Properties();
	if (config != null) {
		parameterNames.forEach(name -> p.setProperty(name, config.getInitParameter(name)));
	}

	if (servletContext.isPresent()) {
		ServletContext context = servletContext.get();
		StreamSupport
				.stream(attributeNames.spliterator(), false)
				.filter(a -> context.getAttribute(a) instanceof String)
				.forEach(a -> p.setProperty(a, (String) context.getAttribute(a)));
	}

	p.putAll(System.getProperties());
	p.putAll(System.getenv());

	return p;

}


@Provides
public static List<String> parameterNames(@Nullable ServletConfig c) {
	return c == null ? List.of() : Collections.list(c.getInitParameterNames());
}


@Provides
public static Iterable<String> attributeNames(@Nullable ServletConfig c) {
	if (c == null) {
		return () -> new ArrayList<String>().iterator();
	}
	ServletContext context = c.getServletContext();
	return () -> context.getAttributeNames().asIterator();
}


@Provides
public static Optional<ServletContext> servletContext(@Nullable ServletConfig c) {
	return Optional.ofNullable(c == null ? null : c.getServletContext());
}

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
