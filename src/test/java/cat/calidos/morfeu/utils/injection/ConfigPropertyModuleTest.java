package cat.calidos.morfeu.utils.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.utils.MorfeuUtils;


public class ConfigPropertyModuleTest {

private Properties p;

@BeforeEach
public void setup() {
	p = new Properties();

}


@Test @DisplayName("Test arg value extraction")
public void testArgsValue() {
	assertEquals(null, ConfigPropertyModule.arrayValue("foo", null));
	assertEquals(null, ConfigPropertyModule.arrayValue("foo", new String[] {}));
	assertEquals(null, ConfigPropertyModule.arrayValue("foo", new String[] { "bar" }));
	assertEquals(null, ConfigPropertyModule.arrayValue("foo", new String[] { "foo" }));
	assertEquals(null, ConfigPropertyModule.arrayValue("foo", new String[] { "--foo" }));
	assertEquals("bar", ConfigPropertyModule.arrayValue("foo", new String[] { "--foo", "bar" }));
	assertEquals(null, ConfigPropertyModule.arrayValue("foo", new String[] { "foobar" }));
	assertEquals("bar", ConfigPropertyModule.arrayValue("foo", new String[] { "foo=bar" }));
	assertEquals("", ConfigPropertyModule.arrayValue("foo", new String[] { "foo=" }));
}


@Test @DisplayName("Test value from properties")
public void testValueProperties() {
	Optional<Object> value = ConfigPropertyModule
			.effectiveValue("foo", p, null, null, null, null, false, null);
	assertTrue(value.isEmpty());
	p.put("foo", "bar");
	value = ConfigPropertyModule.effectiveValue("foo", p, null, null, null, null, false, null);
	assertEquals("bar", value.get());
}


@Test @DisplayName("Test value")
public void testValue() {

	Optional<Object> value = ConfigPropertyModule
			.effectiveValue("foo", p, "bar", null, null, null, false, null);
	assertEquals("bar", value.get());

	value = ConfigPropertyModule.effectiveValue("foo", p, null, "bar", null, null, false, null);
	assertEquals("bar", value.get());

	Map<String, Object> map = MorfeuUtils.paramMap("foo", "bar");
	value = ConfigPropertyModule.effectiveValue("foo", p, null, null, map, null, false, null);
	assertEquals("bar", value.get());

	map = MorfeuUtils.paramMap();
	value = ConfigPropertyModule.effectiveValue("foo", p, null, null, map, null, false, null);
	assertTrue(value.isEmpty());

	value = ConfigPropertyModule.effectiveValue("foo", p, null, null, null, "bar", false, null);
	assertEquals("bar", value.get());
}


@Test @DisplayName("Test default value")
public void testDefaultValue() {
	// default value testing
	Optional<Object> value = ConfigPropertyModule
			.effectiveValue("foo", p, "bar", null, null, null, false, "bar2");
	assertEquals("bar", value.get());

	value = ConfigPropertyModule.effectiveValue("foo", p, null, "bar", null, null, false, "bar2");
	assertEquals("bar", value.get());

	value = ConfigPropertyModule.effectiveValue("foo", p, null, null, null, "bar", false, "bar2");
	assertEquals("bar", value.get());

	value = ConfigPropertyModule.effectiveValue("foo", p, null, null, null, null, false, "bar");
	assertEquals("bar", value.get());

	// override
	value = ConfigPropertyModule.effectiveValue("foo", p, "bar2", "bar", null, null, false, null);
	assertEquals("bar", value.get());

	value = ConfigPropertyModule.effectiveValue("foo", p, "bar2", "bar", null, null, false, null);
	assertEquals("bar", value.get());

	value = ConfigPropertyModule.effectiveValue("foo", p, "bar2", null, null, "bar", false, null);
	assertEquals("bar", value.get());

	value = ConfigPropertyModule.effectiveValue("foo", p, "bar2", "bar3", null, "bar", false, null);
	assertEquals("bar", value.get());

	// null value handling
	value = ConfigPropertyModule.effectiveValue("", p, "", null, null, null, null, "bar2");
	assertEquals("", value.get());

	value = ConfigPropertyModule.effectiveValue("", p, "", null, null, null, true, "bar2");
	assertEquals("", value.get());

	value = ConfigPropertyModule.effectiveValue("", p, "", null, null, null, false, "bar2");
	assertEquals("bar2", value.get());

}


@Test @DisplayName("Allow empty test")
public void testAllowEmpty() {
	System.setProperty("foo", "");
	String value = DaggerConfigPropertyComponent
			.builder()
			.forName("foo")
			.allowEmpty(false)
			.andDefault("bar")
			.build()
			.stringValue()
			.get();
	assertEquals("bar", value);
	System.clearProperty("foo");
}


@Test @DisplayName("Conversion test")
public void testConversion() {
	var p = new Properties();
	p.put("foo", 1);
	int value = DaggerConfigPropertyComponent
			.builder()
			.withProps(p)
			.forName("foo")
			.build()
			.integerValue()
			.get();
	assertEquals(1, value);

	p = new Properties();
	p.put("foo", "1");
	value = DaggerConfigPropertyComponent
			.builder()
			.withProps(p)
			.forName("foo")
			.build()
			.integerValue()
			.get();
	assertEquals(1, value);

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
