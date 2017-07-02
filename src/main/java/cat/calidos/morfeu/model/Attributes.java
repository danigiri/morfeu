/*
 *    Copyright 2017 Daniel Giribet
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

package cat.calidos.morfeu.model;

import java.util.List;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public interface Attributes<T> {

boolean hasAttributes();

boolean hasAttribute(String name);

List<T> attributes();

int size();

T attribute(int i);

T attribute(String name);

/** If the attribute already exists it will replace the value with the new one
*	@return true if the attribute was new and false if it was replaced
*/
boolean addAttribute(String name, T a);

List<T> clearAttributes();



}
