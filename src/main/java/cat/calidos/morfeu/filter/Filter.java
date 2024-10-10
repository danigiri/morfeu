// FILTER . JAVA

package cat.calidos.morfeu.filter;

import java.util.concurrent.ExecutionException;

import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.problems.TransformException;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@FunctionalInterface
public interface Filter<F, R> {

R apply(F f) throws ParsingException, TransformException, ExecutionException, InterruptedException;


default <V> Filter<F, V> andThen(Filter<? super R, ? extends V> after) {
	return (F f) -> after.apply(apply(f));
}


default <V> Filter<V, R> compose(Filter<? super V, ? extends F> before) {
	return (V v) -> apply(before.apply(v));
}

}

/*
 * Copyright 2018 Daniel Giribet
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
