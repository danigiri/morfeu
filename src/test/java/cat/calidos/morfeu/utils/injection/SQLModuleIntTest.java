package cat.calidos.morfeu.utils.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SQLModuleIntTest extends SQLTezt {

@Test @DisplayName("Test connection")
public void okResultTest() throws Exception {

	dropPersonsTable();

	var create = """
			CREATE TABLE Persons (
			    PersonID int,
			    LastName varchar(255),
			    FirstName varchar(255),
			    Address varchar(255),
			    City varchar(255)\s
			)""";
	int update = SQLModule.update(statement, create);
	assertEquals(0, update);

	var insert = "INSERT INTO Persons VALUES(1,'a','b','c','d')";
	update = SQLModule.update(statement, insert);
	assertEquals(1, update);

	List<List<String>> output = SQLModule.query(statement, "SELECT * FROM Persons");
	assertAll(
			"show tables asserts",
			() -> assertEquals(2, output.size()),
			() -> assertEquals(5, output.get(0).size()),
			() -> assertEquals(5, output.get(1).size()),
			() -> assertTrue(output.get(0).contains("PERSONID")),
			() -> assertTrue(output.get(0).contains("LASTNAME")),
			() -> assertTrue(output.get(0).contains("FIRSTNAME")),
			() -> assertTrue(output.get(0).contains("ADDRESS")),
			() -> assertTrue(output.get(1).contains("a")),
			() -> assertTrue(output.get(1).contains("b")));

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
