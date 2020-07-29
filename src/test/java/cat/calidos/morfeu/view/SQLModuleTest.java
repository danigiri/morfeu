package cat.calidos.morfeu.view;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.view.injection.SQLModule;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class SQLModuleTest {

private Connection conn;
private Statement statement;


public void setupDatabase() throws Exception {

	org.h2.Driver.load();
	conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
	statement = SQLModule.statement(conn);

}

@Test @DisplayName("Test connection")
public void okResultTest() throws Exception {

	try {
		setupDatabase();

		System.err.println("foo");
		int update = SQLModule.update(statement, "DROP TABLE IF EXISTS Persons");
		assertEquals(0, update);
		String CREATE = "CREATE TABLE Persons (\n" + 
						"    PersonID int,\n" + 
						"    LastName varchar(255),\n" + 
						"    FirstName varchar(255),\n" + 
						"    Address varchar(255),\n" + 
						"    City varchar(255) \n" + 
						") ";
		update = SQLModule.update(statement, CREATE);
		assertEquals(0, update);
		List<List<String>> output = SQLModule.query(statement, "SHOW TABLES");
		assertAll("",
			() -> assertEquals(1, output.size()),
			() -> assertEquals(2, output.get(0).size()),
			() -> assertTrue(output.get(0).contains("PERSONS")),
			() -> assertTrue(output.get(0).contains("PUBLIC"))
		);
	} finally {
		teardownDatabase();
	}

}


public void teardownDatabase() throws Exception  {

	if (statement!=null) {
		statement.execute("SHUTDOWN");
		statement.close();
	}
	if (conn!=null) {
		conn.close();
	}

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

