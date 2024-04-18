// TEST STACK CONTEXT . JAVA

package cat.calidos.morfeu.transform;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EmptyStackException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class StackContextTest {

private StackContext<String> context;


@BeforeEach
public void setup() {
	context = new StackContext<String>();
}


@Test
public void testEmptyContext() {

	assertTrue(context.empty());
	assertEquals("", context.output());

}


@Test
public void testPeekFail() {
	assertThrows(EmptyStackException.class, () -> context.peek());
}


@Test
public void testPopFail() {
	assertThrows(EmptyStackException.class, () -> context.pop());
}


@Test
public void testPushPop() {

	context.push(new IdentityProcessor("foo"));
	assertFalse(context.empty());
	assertEquals("foo", context.peek().output());
	assertEquals("foo", context.pop().output());
	assertTrue(context.empty());

	context.push(new IdentityProcessor("foo"));
	context.push(new IdentityProcessor("bar"));
	assertFalse(context.empty());
	assertEquals("bar", context.peek().output());
	assertEquals("bar", context.pop().output());
	assertEquals("foo", context.peek().output());

}


@Test
public void testOutputAppend() {

	context.appendToOutput("foo");
	assertEquals("foo", context.output());

	context.appendToOutput("bar");
	assertEquals("foobar", context.output());

}


@Test
public void testToString() {

	context.push(new IdentityProcessor("foo"));
	context.push(new IdentityProcessor("barx"));
	context.push(new IdentityProcessor("tooLong-01234567890123456789012345678901234567890123456789"));
	context.push(new IdentityProcessor("bar"));
	var expected = 	"\n"+
					"⊏------------------------------------------------⊐\n"+
					"|bar                                             |\n"+
					"|tooLong-0123456789012345678901234567890123456789|\n"+
					"|barx                                            |\n"+
					"|foo                                             |\n"+
					"⊏------------------------------------------------⊐\n";
	assertEquals(expected, context.toString());

}

private class IdentityProcessor implements Processor<String, String> {

String input;

IdentityProcessor(String input) {
	this.input = input;
}

@Override
public Context<String, String> generateNewContext(Context<String, String> oldContext) {
	return oldContext;
}


@Override
public String input() {
	return input;
}


@Override
public String output() {
	return input;
}

@Override
public String toString() {
	return output();
}


}

}

/*
 *    Copyright 2024 Daniel Giribet
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

