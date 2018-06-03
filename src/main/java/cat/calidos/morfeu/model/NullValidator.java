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

package cat.calidos.morfeu.model;

import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.problems.ValidationException;


/** Always return true, whenever we do not want to do any semantic validation at all
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class NullValidator implements Validable {

/* (non-Javadoc)
* @see cat.calidos.morfeu.model.Validable#isValid()
*//////////////////////////////////////////////////////////////////////////////
@Override
public boolean isValid() {
	return true;
}


/* (non-Javadoc)
* @see cat.calidos.morfeu.model.Validable#validate()
*//////////////////////////////////////////////////////////////////////////////
@Override
public void validate() throws ValidationException, FetchingException {}

}
