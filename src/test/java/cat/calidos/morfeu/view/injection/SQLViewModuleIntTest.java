package cat.calidos.morfeu.view.injection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.utils.injection.SQLComponent;
import cat.calidos.morfeu.utils.injection.SQLModule;
import cat.calidos.morfeu.utils.injection.SQLTezt;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SQLViewModuleIntTest extends SQLTezt {

@Test @DisplayName("Test render table")
public void testRender() throws Exception {

	dropPersonsTable();
	var create = "CREATE TABLE Persons (\n" + "    PersonID int,\n" + "    LastName varchar(255),\n"
			+ "    FirstName varchar(255),\n" + "    Address varchar(255),\n"
			+ "    City varchar(255) \n" + ") ";
	int update = SQLModule.update(statement, create);
	assertEquals(0, update);
	update = SQLModule
			.update(statement, "INSERT INTO Persons VALUES (1, 'Doe', 'John', 'foo', 'fairyland')");
	assertEquals(1, update);
	update = SQLModule
			.update(
					statement,
					"INSERT INTO Persons VALUES (1, 'Doe', 'Daisy', 'bar', 'fairyland')");
	assertEquals(1, update);

	var query = "SELECT * FROM Persons";
	SQLComponent sql = SQLViewModule.sql(connection, query);
	String table = SQLViewModule.render(sql, false);
	// System.err.println(table);
	assertAll(
			"select * asserts",
			() -> assertNotNull(table),
			() -> assertTrue(table.contains("PERSONID")),
			() -> assertTrue(table.contains("LASTNAME")),
			() -> assertTrue(table.contains("FIRSTNAME")),
			() -> assertTrue(table.contains("ADDRESS")),
			() -> assertTrue(table.contains("CITY")));

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
