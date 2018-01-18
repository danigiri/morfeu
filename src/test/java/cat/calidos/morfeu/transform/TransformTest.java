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
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
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

@Test
public void holder2Test() {
	
	
	UnaryOperator<Holder2<Object>> identity2 = (h) -> h;
	UnaryOperator<Holder2<Object>> replace2 = (h) -> Holder2.from(((String)h.getData()).replace("a", "b"));
	
	LinkedList<UnaryOperator<Holder2<Object>>> ops = new LinkedList<UnaryOperator<Holder2<Object>>>();
	ops.add(identity2);
	ops.add(replace2);
	
	
	Holder2<Object> d = new Holder2<Object>("This is a test");
	for (UnaryOperator<Holder2<Object>> t : ops) {
		d = t.apply(d);
	}
	assertEquals("This is b test", d.getData());
	
	
	UnaryOperator<String> identity3 = (s) -> s;
	UnaryOperator<String> replace3 = (s) ->s.replace("1", "2");
	Function<String, Integer> toInt3 = (s) -> Integer.valueOf(s);
	
	Function<String, Integer> composite3 = identity3.andThen(replace3).andThen(toInt3);
	assertEquals(22, composite3.apply("11").intValue());
	
	
	UnaryOperator<Holder3> identity4 = (s) -> s;
	UnaryOperator<Holder3> replace4 = (s) -> Holder3.string(s.asString().replace("1", "2"));
	UnaryOperator<Holder3>  toInt4 = (s) -> Holder3.integer(Integer.valueOf(s.asString()));
	
	Function<Holder3, Holder3> composite4 = identity4.andThen(replace4).andThen(toInt4);
	assertEquals(22, composite4.apply(Holder3.string("11")).asInteger().intValue());
	
}


@Test
public void streamChainTest() {
	
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

}
