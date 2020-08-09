// VIEW COMPONENT INT TEST . JAVA

package cat.calidos.morfeu.view;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.morfeu.view.injection.ViewComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ViewComponentIntTest {


@Test
public void testInlineTemplate() {

	Map<String, Object> values = MorfeuUtils.paramMap("foo", "bar");
	String templ = "value={{ v.foo }}";

	ViewComponent view = DaggerViewComponent.builder().withValue(values).withTemplate(templ).andProblem("").build();

	assertEquals("value=bar", view.render());

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

