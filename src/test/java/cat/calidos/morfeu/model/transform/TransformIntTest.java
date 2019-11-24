// TRANSFORM INT TEST . JAVA

package cat.calidos.morfeu.model.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.transform.injection.DaggerTransformComponent;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformIntTest {


@Test @DisplayName("Stream chain test")
public void streamChainTest() throws Exception {

	// I pre-create a set of variables that hold the different states 
	// and keep playing with them: <string, string>, <int,string>, <string, int>, <int,int>
	// to create a virtual chain list of composite operations
	// string->string->string ---> composite, when I need to convert, I push the composite onto the list
	// push string-> integer and then either get an integer->string
	// or just integer->integer...

	// while ops left to be done
	UnaryOperator<String> identity3 = (s) -> s;
	UnaryOperator<String> replace3 = (s) -> s.replace("1", "2");

		// while string do compose..
	Function<String, String> stringComposite = identity3.compose(replace3);
	// if ned to change, apply and then switch modes
	Function<String, Integer> toInt3 = (s) -> Integer.valueOf(s);	
	Function<String, Integer> compose = toInt3.compose(stringComposite);

	UnaryOperator<Integer> increment = (i) -> i.intValue()+1;	
	Function<String, Integer> chain = increment.compose(compose);

	Function<Integer, String> back = (i) -> i.toString();

	Function<String, String> finalChain = back.compose(chain);

	assertEquals("23", finalChain.apply("11"));

	// this way I always play with strong types and there are no casts anywhere =)

}


@Test @DisplayName("Identity test")
public void identityTest() throws Exception {

	Transform<String, String> t = DaggerTransformComponent.builder()
															.transforms("identity")
															.build()
															.stringToString()
															.get();
	assertEquals("foo",t.apply("foo"));

	Transform<Object, Object> t2 = DaggerTransformComponent.builder()
															.transforms("identity")
															.build()
															.objectToObject()
															.get();
	Map<String, String> map = new HashMap<String, String>(1);
	map.put("foo", "bar");
	assertEquals(map,t2.apply(map));

}


@Test @DisplayName("More complex test test")
public void objectToStringTest() throws Exception {

	String transforms = "to-string;identity;lowercase";
	Transform<Object, String> t = DaggerTransformComponent.builder()
															.transforms(transforms)
															.build()
															.objectToString()
															.get();
	assertNotNull(t);

	StringBuffer fooObject = new StringBuffer("FOO");
	String result = t.apply(fooObject);
	assertAll("to string and to lower case",
		() -> assertNotNull(result),
		() -> assertEquals("foo", result, "Correct transform chain was not applied")
	);

}


@Test @DisplayName("JSON to YAML test")
public void jsonToYAMLTest() throws Exception {

	String transforms = "yaml-to-json";
	Transform<String, String> t = DaggerTransformComponent.builder()
															.transforms(transforms)
															.build()
															.stringToString()
															.get();
	assertNotNull(t);

	String yaml = "a:\n" + 
					"- a0\n" + 
					"- a1";
	String result = t.apply(yaml);
	String expected = "{\n" + 
						"  \"a\" : [ \"a0\", \"a1\" ]\n" + 
						"}\n";
	assertAll("check yaml to json outcome",
			() -> assertNotNull(result),
			() -> assertEquals(expected, expected, "Correct transform chain was not applied")
	);
}


@Test @DisplayName("Apply template transform test")
public void applyTemplateTest() throws Exception {

	String transforms = "apply-template{\"template\":\"templates/transform/map-identity.twig\"}";
	Transform<Object, String> t = DaggerTransformComponent.builder()
															.transforms(transforms)
															.build()
															.objectToString()
															.get();

	Map<String, Object> values = new HashMap<String, Object>(2);
	values.put("a", "foo");
	values.put("b", "bar");
	String result = t.apply(values);
	String expected = 	"a=foo,b=bar,";
	assertAll("check yaml to json outcome",
			() -> assertNotNull(result),
			() -> assertEquals(expected, result, "Correct template was not applied")
	);

	transforms = "apply-template{}";
	Transform<Object, String> t2 = DaggerTransformComponent.builder()
															.transforms(transforms)
															.build()
															.objectToString()
															.get();
	String result2 = t2.apply(values);
	String expected2 = "APPLY TEMPLATE HAS NO TEMPLATE PARAMETER";
	assertAll("check yaml to json outcome",
			() -> assertNotNull(result2),
			() -> assertEquals(expected2, result2, "Should not have applied a template")
	);


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
