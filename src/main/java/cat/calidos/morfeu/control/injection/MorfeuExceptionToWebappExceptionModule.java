package cat.calidos.morfeu.control.injection;

import dagger.Module;
import dagger.Provides;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.MorfeuException;
import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.PostingException;
import cat.calidos.morfeu.problems.TransformException;
import cat.calidos.morfeu.problems.ValidationException;
import cat.calidos.morfeu.webapp.control.problems.WebappBadGatewayException;
import cat.calidos.morfeu.webapp.control.problems.WebappInternalException;
import cat.calidos.morfeu.webapp.control.problems.WebappNotFoundException;
import cat.calidos.morfeu.webapp.control.problems.WebappRuntimeException;


@Module
public class MorfeuExceptionToWebappExceptionModule {

@Provides
public static WebappRuntimeException toWebappException(MorfeuException e) {

	switch (e) {
		case ConfigurationException cfe:
			return new WebappInternalException(cfe);
		case FetchingException fe: // usually due to broken URLs, going for not found
			return new WebappNotFoundException(fe);
		case ParsingException ie:
			return new WebappInternalException(ie);
		case PostingException pe: // we apply a heuristic here to decide if it's gateway or internal
			return pe.getMessage().contains("post") || pe.getMessage().contains("POST")
					? new WebappBadGatewayException(pe) : new WebappInternalException(pe);
		case TransformException te:
			return new WebappInternalException(te);
		case ValidationException ve:
			return new WebappInternalException(ve);
		default:
			return new WebappInternalException(e);
	}
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
