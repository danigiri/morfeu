package cat.calidos.morfeu.webapp;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.webapp.ui.UICellData;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class CellDataComponentUITest extends UITezt {


@Test @DisplayName("Test col data")
public void testColData() {

	open(appBaseURL+"test/cell-data/col");
	UICellData cellData = new UICellData();
	cellData.shouldBeVisible();

	assertAll("check basics",
		() -> assertTrue(cellData.isFromCell()),
		() -> assertEquals("col [0..∞]", cellData.header()),
		() -> assertEquals("Column, can accept content", cellData.desc())
	);

	List<String> extraInfo = cellData.extraInfo();
	assertAll("check extra info",
			() -> assertNotNull(extraInfo),
			() -> assertEquals(1, extraInfo.size()),
			() -> assertTrue(extraInfo.contains("[Children need to be in order]"))
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
