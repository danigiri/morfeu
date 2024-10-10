// PREFIX PROCESSOR . JAVA

package cat.calidos.morfeu.transform;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class PrefixProcessor<T, S> implements Processor<T, S> {

protected S prefix;

protected PrefixProcessor(S prefix) {
	this.prefix = prefix;
}


@Override
public S output() {
	return prefix;
}


@Override
public String toString() {
	return output().toString();
}

}

/*
 * Copyright 2019 Daniel Giribet
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
