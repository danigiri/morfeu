package cat.calidos.morfeu.utils.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SQLModuleTest extends SQLTezt {


@Test @DisplayName("Test connection")
public void okResultTest() throws Exception {

	dropPersonsTable();

	String create = "CREATE TABLE Persons (\n" + 
					"    PersonID int,\n" + 
					"    LastName varchar(255),\n" + 
					"    FirstName varchar(255),\n" + 
					"    Address varchar(255),\n" + 
					"    City varchar(255) \n" + 
					") ";
	int update = SQLModule.update(statement, create);
	assertEquals(0, update);
	List<List<String>> output = SQLModule.query(statement, "SHOW TABLES");
	assertAll("show tables asserts",
		() -> assertEquals(2, output.size()),
		() -> assertEquals(2, output.get(0).size()),
		() -> assertTrue(output.get(0).contains("TABLE_NAME")),
		() -> assertTrue(output.get(0).contains("TABLE_SCHEMA")),
		() -> assertTrue(output.get(1).contains("PERSONS")),
		() -> assertTrue(output.get(1).contains("PUBLIC"))
	);

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

