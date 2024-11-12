// CONTROL COMPONENT INT TEST . JAVA

package cat.calidos.morfeu.control.injection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletContext;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.webapp.control.problems.WebappNotFoundException;
import cat.calidos.morfeu.webapp.injection.WebappControlComponent;
import cat.calidos.morfeu.webapp.injection.DaggerWebappControlComponent;


/**
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
public class WebappControlComponentIntTest {

ServletContext context = mock(ServletContext.class);

HashMap<String, String> emptyParams = new HashMap<String, String>(0);

@Test
public void testPingControl() {

	WebappControlComponent controlComponent = DaggerWebappControlComponent
			.builder()
			.withPath("/ping")
			.method(WebappControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /ping path");
	assertEquals("OK", controlComponent.process());
	assertEquals(WebappControlComponent.TEXT, controlComponent.contentType());

}


@Test
public void testPingControlWithParam() {

	WebappControlComponent controlComponent = DaggerWebappControlComponent
			.builder()
			.withPath("/ping/param")
			.method(WebappControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /ping/param path");
	assertEquals("OK param", controlComponent.process());
	assertEquals(WebappControlComponent.TEXT, controlComponent.contentType());

	controlComponent = DaggerWebappControlComponent
			.builder()
			.withPath("/ping/param%20with%20spaces")
			.method(WebappControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /ping/param path with %20 encoded spaces");
	assertEquals("OK param with spaces", controlComponent.process());

}


@Test
public void testNoMatch() {

	WebappControlComponent controlComponent = DaggerWebappControlComponent
			.builder()
			.withPath("/foo")
			.method(WebappControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();

	assertFalse(controlComponent.matches());
	assertEquals(WebappControlComponent.TEXT, controlComponent.contentType());

}


@Test
public void testContext() {

	when(context.getAttribute("counter")).thenReturn(0);

	WebappControlComponent controlComponent = DaggerWebappControlComponent
			.builder()
			.withPath("/counter")
			.method(WebappControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /counter path");
	assertEquals("1", controlComponent.process());
	assertEquals(WebappControlComponent.TEXT, controlComponent.contentType());

}


@Test
public void testNotFound() {
	WebappControlComponent controlComponent = DaggerWebappControlComponent
			.builder()
			.withPath("/notfound/foo")
			.method(WebappControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();

	// we match but any subpath (/foo or whatever) will always be not found
	assertTrue(controlComponent.matches());
	assertThrows(WebappNotFoundException.class, () -> controlComponent.process());
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
