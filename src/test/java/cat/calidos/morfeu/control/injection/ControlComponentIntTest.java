/*
 *    Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.control.injection;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ControlComponentIntTest {

@Test
public void testPingControl() {

	ControlComponent controlComponent = DaggerControlComponent.builder()
																.withPath("/ping")
																.withParams(new HashMap<String, String>(0))
																.build();
	assertTrue("Should match /ping path", controlComponent.matches());
	assertEquals("OK", controlComponent.process());
}


@Test
public void testPingControlWithParam() {

	ControlComponent controlComponent = DaggerControlComponent.builder()
																.withPath("/ping/param")
																.withParams(new HashMap<String, String>(0))
																.build();
	assertTrue("Should match /ping/param path", controlComponent.matches());
	assertEquals("OK param", controlComponent.process());
	
}


@Test
public void testNoMatch() {
	
	ControlComponent controlComponent = DaggerControlComponent.builder()
			.withPath("/foo")
			.withParams(new HashMap<String, String>(0))
			.build();

	assertFalse(controlComponent.matches());

}

}
