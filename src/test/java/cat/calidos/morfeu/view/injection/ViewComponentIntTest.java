// VIEW COMPONENT INT TEST . JAVA

package cat.calidos.morfeu.view.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.utils.MorfeuUtils;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ViewComponentIntTest {


@Test
public void testInlineTemplate() {

	Map<String, Object> values = MorfeuUtils.paramMap("foo", "bar");
	String templ = "value=${v.foo}";

	ViewComponent view = DaggerViewComponent.builder().withValue(values).withTemplate(templ).andProblem("").build();

	assertEquals("value=bar", view.render());

}


@Test
public void testInlineTemplateWithProblem() {

	Map<String, Object> values = MorfeuUtils.paramMap();
	String templ = "problem=${problem}";

	ViewComponent view = DaggerViewComponent.builder().withValue(values).withTemplate(templ).andProblem("bar").build();

	assertEquals("problem=bar", view.render());

}


@Test
public void testChop() {

	var templ = "${f.chop('abc')}";

	ViewComponent view = DaggerViewComponent.builder().withValue(MorfeuUtils.paramMap()).withTemplate(templ)
			.andProblem("").build();

	assertEquals("ab", view.render());

}


@Test
public void testQuote() {
	var templ = "${f.quote('abc')}";

	ViewComponent view = DaggerViewComponent.builder().withValue(MorfeuUtils.paramMap()).withTemplate(templ)
			.andProblem("").build();

	assertEquals("\"abc\"", view.render());

	String templ2 = "${f.quote('\"abc\"')}";

	ViewComponent view2 = DaggerViewComponent.builder().withValue(MorfeuUtils.paramMap()).withTemplate(templ2)
			.andProblem("").build();

	assertEquals("\"abc\"", view2.render());

}


@Test
public void testIsMultiline() {
	var templ = "${f.isMultiline('abc')?c}";

	ViewComponent view = DaggerViewComponent.builder().withValue(MorfeuUtils.paramMap()).withTemplate(templ)
			.andProblem("").build();

	assertEquals("false", view.render());
}


@Test
public void testXMLEscaping() {
	var templ = "${f.xmlc('& > <')}";

	ViewComponent view = DaggerViewComponent.builder().withValue(MorfeuUtils.paramMap()).withTemplate(templ)
			.andProblem("").build();

	assertEquals("&amp; &gt; &lt;", view.render());
}


@Test
public void testYAMLEscaping() {
	var templ = "${f.yamla('\\\"')}";

	ViewComponent view = DaggerViewComponent.builder().withValue(MorfeuUtils.paramMap()).withTemplate(templ)
			.andProblem("").build();

	assertEquals("\"", view.render());

	templ = "${f.yamlc('\\\"')}";

	view = DaggerViewComponent.builder().withValue(MorfeuUtils.paramMap()).withTemplate(templ)
			.andProblem("").build();

	assertEquals("\"", view.render());
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

