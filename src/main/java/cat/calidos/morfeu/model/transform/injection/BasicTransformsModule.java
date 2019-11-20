package cat.calidos.morfeu.model.transform.injection;

import javax.inject.Named;

import cat.calidos.morfeu.model.transform.Transform;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class BasicTransformsModule {


@Produces @IntoMap @Named("stringToString")
@StringKey("identity")
Transform<String, String> stringIdentity() {
	return s -> s;
}


@Produces @IntoMap @Named("stringToString")
@StringKey("uppercase")
Transform<String, String> uppercase() {	// mainly for testing
	return s -> s.toUpperCase();
}


@Produces @IntoMap @Named("stringToString")
@StringKey("lowercase")
Transform<String, String> lowercase() {	// mainly for testing
	return s -> s.toLowerCase();
}


@Produces @IntoMap @Named("objectToString")
@StringKey("to-string")
Transform<Object, String> toString_() {
	return (o) -> o.toString();
}



@Produces @IntoMap @Named("objectToObject")
@StringKey("identity")
 Transform<Object, Object> iobjectIdentity() {
	return o -> o;
}


}


/*
 *    Copyright 2019 Daniel Giribet
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

