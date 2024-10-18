// CONTROL COMPONENT INT TEST . JAVA

package cat.calidos.morfeu.control.injection;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.injection.DaggerControlComponent;
import jakarta.servlet.ServletContext;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ControlComponentIntTest {

ServletContext context = mock(ServletContext.class);

HashMap<String, String> emptyParams = new HashMap<String, String>(0);

@Test
public void testPingControl() {

	ControlComponent controlComponent = DaggerControlComponent
			.builder()
			.withPath("/ping")
			.method(ControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /ping path");
	assertEquals("OK", controlComponent.process());
	assertEquals(ControlComponent.TEXT, controlComponent.contentType());

}


@Test
public void testPingControlWithParam() {

	ControlComponent controlComponent = DaggerControlComponent
			.builder()
			.withPath("/ping/param")
			.method(ControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /ping/param path");
	assertEquals("OK param", controlComponent.process());
	assertEquals(ControlComponent.TEXT, controlComponent.contentType());

	controlComponent = DaggerControlComponent
			.builder()
			.withPath("/ping/param%20with%20spaces")
			.method(ControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /ping/param path with %20 encoded spaces");
	assertEquals("OK param with spaces", controlComponent.process());

}


@Test
public void testNoMatch() {

	ControlComponent controlComponent = DaggerControlComponent
			.builder()
			.withPath("/foo")
			.method(ControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();

	assertFalse(controlComponent.matches());
	assertEquals(ControlComponent.TEXT, controlComponent.contentType());

}


@Test
public void testContext() {

	when(context.getAttribute("counter")).thenReturn(0);

	ControlComponent controlComponent = DaggerControlComponent
			.builder()
			.withPath("/counter")
			.method(ControlComponent.GET)
			.withParams(emptyParams)
			.andContext(context)
			.encoding(Config.DEFAULT_CHARSET)
			.build();
	assertTrue(controlComponent.matches(), "Should match /counter path");
	assertEquals("1", controlComponent.process());
	assertEquals(ControlComponent.TEXT, controlComponent.contentType());

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
