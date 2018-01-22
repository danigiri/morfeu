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

package cat.calidos.morfeu.model.transform;

import java.util.concurrent.ExecutionException;

import cat.calidos.morfeu.problems.ParsingException;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@FunctionalInterface
public interface Transform<T, R> {

R apply(T t) throws ParsingException, ExecutionException, InterruptedException;

default <V> Transform<T, V> andThen(Transform<? super R, ? extends V> after) {
	return (T t) -> after.apply(apply(t));
}


default <V> Transform<V, R> compose(Transform<? super V, ? extends T> before) {
    return (V v) -> apply(before.apply(v));
}

}
