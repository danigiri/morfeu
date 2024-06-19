package cat.calidos.morfeu.utils.injection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.Tezt;


public class SQLTezt extends Tezt {

protected Connection connection;
protected Statement statement;
private String databasePath;


@BeforeEach
public void setupDatabase() throws Exception {

	File databaseDirectory = setupTempDirectory();
	// we may have concurrency issues with the database, we skip testing then
	org.h2.Driver.load();
	databasePath = databaseDirectory+"/database";
	FileUtils.forceMkdir(new File (databasePath));
	System.err.println("Using database temp path "+databasePath);
	connection = DriverManager.getConnection("jdbc:h2:"+databasePath+"/foo", "sa", "sa");
	statement = SQLModule.statement(connection);

}


@AfterEach
public void teardown() throws Exception {

	if (statement!=null) {
		statement.execute("SHUTDOWN");
		statement.close();
	}
	if (connection!=null) {
		connection.close();
	}

//	if (!databasePath.isEmpty()) {
//		FileUtils.deleteQuietly(new File (databasePath));
//	}

}


protected void dropPersonsTable() throws ParsingException {
	SQLModule.update(statement, "DROP TABLE IF EXISTS Persons");
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
