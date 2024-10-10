package cat.calidos.morfeu.proxy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;


public class MorfeuProxyServletTest {

@Test @DisplayName("Test get target uri config")
public void testGetFinalTargetURI() throws ServletException {

	// assertEquals("foo", MorfeuProxyServlet.getFinalTargetURI("foo", null));

	System.setProperty(MorfeuProxyServlet.__PROXY_PREFIX, "foo");
	var targetUri = "${" + MorfeuProxyServlet.__PROXY_PREFIX + "}";
	assertEquals("foo", MorfeuProxyServlet.getFinalTargetURI(targetUri, null));

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
