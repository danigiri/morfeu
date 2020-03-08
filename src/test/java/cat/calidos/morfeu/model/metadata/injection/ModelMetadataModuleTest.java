package cat.calidos.morfeu.model.metadata.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ModelMetadataModuleTest {


@Test @DisplayName("Test readonly")
public void testReadonly() {

	Optional<Boolean> readonly0 = ModelMetadataModule.readonly(Optional.empty());
	assertAll("test empty", 
		() -> assertNotNull(readonly0),
		() -> assertFalse(readonly0.isPresent())
	);

	Optional<Boolean> readonly1 = ModelMetadataModule.readonly(Optional.of("yes"));
	assertAll("test yes", 
		() -> assertNotNull(readonly1),
		() -> assertTrue(readonly1.isPresent()),
		() -> assertTrue(readonly1.get())
	);

	Optional<Boolean> readonly2 = ModelMetadataModule.readonly(Optional.of("true"));
	assertAll("test yes", 
		() -> assertNotNull(readonly2),
		() -> assertTrue(readonly2.isPresent()),
		() -> assertTrue(readonly2.get())
	);

	Optional<Boolean> readonly3 = ModelMetadataModule.readonly(Optional.of("nope"));
	assertAll("test nope", 
		() -> assertNotNull(readonly3),
		() -> assertTrue(readonly3.isPresent()),
		() -> assertFalse(readonly3.get())
	);

	Optional<Boolean> readonly4 = ModelMetadataModule.readonly(Optional.of("no"));
	assertAll("test no", 
		() -> assertNotNull(readonly4),
		() -> assertTrue(readonly4.isPresent()),
		() -> assertFalse(readonly4.get())
	);

	Optional<Boolean> readonly5 = ModelMetadataModule.readonly(Optional.of("false"));
	assertAll("test false", 
		() -> assertNotNull(readonly5),
		() -> assertTrue(readonly5.isPresent()),
		() -> assertFalse(readonly5.get())
	);

}


}


/*
 *    Copyright 2020 Daniel Giribet
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

