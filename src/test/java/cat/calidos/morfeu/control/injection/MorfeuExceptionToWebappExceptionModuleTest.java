package cat.calidos.morfeu.control.injection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.PostingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.webapp.control.problems.WebappBadGatewayException;
import cat.calidos.morfeu.webapp.control.problems.WebappInternalException;
import cat.calidos.morfeu.webapp.control.problems.WebappNotFoundException;


public class MorfeuExceptionToWebappExceptionModuleTest {

@Test @DisplayName("Testing runtime to webapp translation")
public void testTranslation() {

	assertEquals(
			WebappInternalException.class,
			MorfeuExceptionToWebappExceptionModule
					.toWebappException(new ConfigurationException(""))
					.getClass());
	assertEquals(
			WebappNotFoundException.class,
			MorfeuExceptionToWebappExceptionModule
					.toWebappException(new FetchingException(""))
					.getClass());
	assertEquals(
			WebappInternalException.class,
			MorfeuExceptionToWebappExceptionModule
					.toWebappException(new ParsingException(""))
					.getClass());
	assertEquals(
			WebappBadGatewayException.class,
			MorfeuExceptionToWebappExceptionModule
					.toWebappException(new PostingException("post"))
					.getClass());
	assertEquals(
			WebappInternalException.class,
			MorfeuExceptionToWebappExceptionModule
					.toWebappException(new TransformException(""))
					.getClass());
	assertEquals(
			WebappInternalException.class,
			MorfeuExceptionToWebappExceptionModule
					.toWebappException(new ValidationException(""))
					.getClass());
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
