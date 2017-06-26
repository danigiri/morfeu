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

package cat.calidos.morfeu.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cat.calidos.morfeu.model.Composite;
import cat.calidos.morfeu.problems.InternalException;


/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CompositeTest {

private Composite<String> composite;

@Before
public void setup() {
	
//TODO: put in a module somewhere
	this.composite = new OrderedMap<String>();
}


@Test
public void testChildren() {

	assertTrue(composite.children().isEmpty());
	assertFalse(composite.hasChildren());
	
	assertTrue(composite.addChild("foo", "bar"));
	assertFalse(composite.children().isEmpty());
	assertTrue(composite.hasChildren());
	
	assertEquals("bar", composite.children().get(0));
	
	assertEquals("bar", composite.child(0));

	assertTrue(composite.addChild("foo2", "bar2"));
	assertEquals("bar", composite.children().get(0));
	assertEquals("bar2", composite.children().get(1));
	
	assertFalse(composite.addChild("foo2", "bar3"));
	assertEquals("bar3", composite.children().get(1));
	
	assertEquals("bar", composite.child(0));
	assertEquals("bar3", composite.child(1));
		
	assertEquals(2, composite.clearChildren().size());
	assertTrue(composite.children().isEmpty());
	assertFalse(composite.hasChildren());
	
}


@Test(expected=InternalException.class)
public void testBadGet() {
	
	assertTrue(composite.addChild("foo", "bar"));
	composite.child(1);
	
}


@Test(expected=InternalException.class)
public void testBadGetName() {
	
	assertTrue(composite.addChild("foo", "bar"));
	composite.child("nope");
	
}


}
