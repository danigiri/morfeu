package cat.calidos.morfeu.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
public class NamedPairTest {

@Test @DisplayName("Named pair basic tests")
public void namedPairTest() {
	var pair = new NamedPair<Integer>("a", 1, "b", 2);
	assertEquals(1, pair.get("a"));
	assertEquals(2, pair.get("b"));
	assertEquals(1, pair.getLeft());
	assertEquals(2, pair.getRight());
}


@Test @DisplayName("Named pair error tests")
public void namedPairErrorsTest() {
	assertThrows(IllegalArgumentException.class, () -> new NamedPair<Integer>("a", 1, "a", 2));
	assertThrows(
			NoSuchElementException.class,
			() -> new NamedPair<Integer>("a", 1, "b", 2).get("x"));

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
