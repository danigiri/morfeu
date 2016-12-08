package config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import cat.calidos.partikle.webapp.di.ServletConfigModule;


public class ConfigModuleTest {

@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

@Mock ServletConfig servletConfig;

@Test
public void testProvideConfigWithServletConfig() {

		Enumeration<String> names = Collections.enumeration(Arrays.asList("a","b","c"));
		when(servletConfig.getInitParameterNames()).thenReturn(names);
		when(servletConfig.getInitParameter("a")).thenReturn("A");
		when(servletConfig.getInitParameter("b")).thenReturn("B");
		when(servletConfig.getInitParameter("c")).thenReturn("C");
		
		Properties p = (new ServletConfigModule(servletConfig).provideConfig());
		
		assertEquals("A", p.getProperty("a"));
		assertEquals("B", p.getProperty("b"));
		assertEquals("C", p.getProperty("c"));
		
}

}
