// CONFIG MODULE TEST . JAVA

package cat.calidos.morfeu.webapp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import cat.calidos.morfeu.webapp.injection.ServletConfigModule;


public class ConfigModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

@Mock ServletConfig servletConfig;
@Mock ServletContext servletContext;

@Test
public void testProvideConfigWithServletConfig() {

		Enumeration<String> names = Collections.enumeration(Arrays.asList("a","b","c"));
		when(servletConfig.getInitParameterNames()).thenReturn(names);
		when(servletContext.getAttributeNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
		when(servletConfig.getServletContext()).thenReturn(servletContext);
		when(servletConfig.getInitParameter("a")).thenReturn("A");
		when(servletConfig.getInitParameter("b")).thenReturn("B");
		when(servletConfig.getInitParameter("c")).thenReturn("C");

		Properties p = (ServletConfigModule.getProperties(servletConfig));
		assertEquals("A", p.getProperty("a"));
		assertEquals("B", p.getProperty("b"));
		assertEquals("C", p.getProperty("c"));

}

}

/*
 *	  Copyright 2019 Daniel Giribet
 *
 *	 Licensed under the Apache License, Version 2.0 (the "License");
 *	 you may not use this file except in compliance with the License.
 *	 You may obtain a copy of the License at
 *
 *		 http://www.apache.org/licenses/LICENSE-2.0
 *
 *	 Unless required by applicable law or agreed to in writing, software
 *	 distributed under the License is distributed on an "AS IS" BASIS,
 *	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	 See the License for the specific language governing permissions and
 *	 limitations under the License.
 */
