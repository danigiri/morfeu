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

package cat.calidos.morfeu.transform;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.Test;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformTest {

Transformer<String, String> identity = (a , p) -> a;
Transformer<String, String> replace = (a, p) -> new StringHolder(a.getContent().replace((String)p.get("original"), (String) p.get("replacement")));

@Test
public void basicTransformTest() {
	
	StringHolder holder = new StringHolder("This is a test");
	

	Map<String, Object> params = new HashMap<String, Object>();
	assertEquals("This is a test", identity.apply(holder, params).getContent());
	
	

	params = new HashMap<String, Object>(1);
	params.put("original", "a");
	params.put("replacement", "b");
	assertEquals("This is b test", replace.apply(holder, params).getContent());
	
}

UnaryOperator<String> identity2 = (a) -> a;

@Test
public void streamOfTransformsTest() {
	
	
	//////////// ITS BETTER THAT WE SETUP A TERNARY THAT ACCEPTS ANY OBJECT DIRECTLY (MAYBE HOLDERS MAYBE NOT)
	/// AND THE THIRD PARAMETER IS LIKE A TRANSFORMATION OBJECT (INCLUDING SPECIFIC PARAMETERS
	
	//////////// THE NEXT IDEA IS TO RUN THE TRANSFORMATIONS AS STREAMS
	
	String[] arr = { "program", "creek", "program", "creek", "java", "web",
	"program" };
	 Stream<String> stream = Stream.of(arr);
	
}


@Test
public void basicTransform2Test() {
	
	
	Transformer2<String, String> transform = (a, f) -> f.apply(a);
	assertEquals("This is a test", identity2.apply("This is a test"));
	
	UnaryOperator<String> repl = new StrReplace("a", "b");
	assertEquals("This is b test", repl.apply("This is a test"));
	
}


@Test
public void streamOfTransforms2Test() {
	//Transformer2<String, String> transforms = {identity, };


	List<UnaryOperator<String>> ops = new LinkedList<UnaryOperator<String>>();
	ops.add(identity2);
	ops.add( new StrReplace("a", "b"));
	ops.add(new StrReplace("b", "c"));
	String d = "This is a test";
	for (UnaryOperator<String> t : ops) {
		d = t.apply(d);
	}
	assertEquals("This is c test", d);
	
}

//TODO: test with holders so we can do type transforms


}
