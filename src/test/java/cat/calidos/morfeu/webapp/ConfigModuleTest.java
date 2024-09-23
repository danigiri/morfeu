// CONFIG MODULE TEST . JAVA

package cat.calidos.morfeu.webapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.injection.ServletConfigModule;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;


public class ConfigModuleTest {


@Test
public void testProvideConfigWithServletConfig() {

	var servletConfig = mock(ServletConfig.class);
	var servletContext = mock(ServletContext.class);

	Enumeration<String> names = Collections.enumeration(Arrays.asList("a", "b", "c"));
	when(servletConfig.getInitParameterNames()).thenReturn(names);
	when(servletContext.getAttributeNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
	when(servletConfig.getServletContext()).thenReturn(servletContext);
	when(servletConfig.getInitParameter("a")).thenReturn("A");
	when(servletConfig.getInitParameter("b")).thenReturn("B");
	when(servletConfig.getInitParameter("c")).thenReturn("C");

	var parameterNames = new ArrayList<String>(3);
	parameterNames.add("a");
	parameterNames.add("b");
	parameterNames.add("c");
	
	Properties p = (ServletConfigModule.getProperties(servletConfig, Optional.empty(), parameterNames, ServletConfigModule.attributeNames(null) ));
	assertEquals("A", p.getProperty("a"));
	assertEquals("B", p.getProperty("b"));
	assertEquals("C", p.getProperty("c"));

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
