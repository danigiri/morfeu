package cat.calidos.morfeu.utils.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConfigPropertyModuleTest {



@Test @DisplayName("Test arg value extraction")
public void testArgsValue() {
	assertEquals(null, ConfigPropertyModule.argsValue("foo", null));
	assertEquals(null, ConfigPropertyModule.argsValue("foo", new String[]{}));
	assertEquals(null, ConfigPropertyModule.argsValue("foo", new String[]{"bar"}));
	assertEquals(null, ConfigPropertyModule.argsValue("foo", new String[]{"foo"}));
	assertEquals(null, ConfigPropertyModule.argsValue("foo", new String[]{"--foo"}));
	assertEquals("bar", ConfigPropertyModule.argsValue("foo", new String[]{"--foo", "bar"}));
	assertEquals(null, ConfigPropertyModule.argsValue("foo", new String[]{"foobar"}));
	assertEquals("bar", ConfigPropertyModule.argsValue("foo", new String[]{"foo=bar"}));
	assertEquals("", ConfigPropertyModule.argsValue("foo", new String[]{"foo="}));
}


@Test @DisplayName("Test value")
public void testValue() {
	var p = new Properties();
	assertTrue(ConfigPropertyModule.value("foo", p, null, null, null, null, null).isEmpty());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, "bar", null, null, null, null).get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, null, "bar", null, null, null).get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, null, null, "bar", null, null).get());

	// default value testing
	assertEquals("bar", ConfigPropertyModule.value("foo", p, "bar", null, null, null, "bar2").get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, null, "bar", null, null, "bar2").get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, null, null, "bar", null, "bar2").get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, null, null, null, null, "bar").get());

	// override
	assertEquals("bar", ConfigPropertyModule.value("foo", p, "bar2", "bar", null, null, null).get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, "bar2", "bar", null, null, null).get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, "bar2", null, "bar", null, null).get());
	assertEquals("bar", ConfigPropertyModule.value("foo", p, "bar2", "bar3", "bar", null, null).get());

	// null value handling
	assertEquals("", ConfigPropertyModule.value("", p, "", null, null, null, "bar2").get());
	assertEquals("", ConfigPropertyModule.value("", p, "", null, null, true, "bar2").get());
	assertEquals("bar2", ConfigPropertyModule.value("", p, "", null, null, false, "bar2").get());

}


@Test @DisplayName("Allow empty test")
public void testAllowEmpty() {
	System.setProperty("foo", "");
	String value = DaggerConfigPropertyComponent.builder()
		.forName("foo")
		.allowEmpty(false)
		.andDefault("bar")
		.build()
		.value()
		.get();
	assertEquals("bar", value);
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
