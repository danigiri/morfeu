package cat.calidos.morfeu.utils.injection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import cat.calidos.morfeu.problems.ParsingException;
import cat.calidos.morfeu.utils.Tezt;


public class SQLTezt extends Tezt {

private DBThread		dbThread;
protected Connection	connection;
protected Statement		statement;
private String			databasePath;

@BeforeEach
public void setupDatabase() throws Exception {

	File databaseDirectory = setupTempDirectory();
	databasePath = databaseDirectory + "/database";
	FileUtils.forceMkdir(new File(databasePath));

	var databaseName = "test";
	dbThread = new DBThread(databasePath, databaseName);
	dbThread.start();
	sleep(Duration.ofMillis(200)); // so db starts
	Class.forName("org.hsqldb.jdbcDriver");
	connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/" + databaseName, "SA",
			"");
	statement = SQLModule.statement(connection);

}


@AfterEach
public void teardown() throws Exception {

	if (statement != null) {
		statement.execute("SHUTDOWN");
		statement.close();
	}
	if (connection != null) {
		connection.close();
	}

	if (dbThread != null) {
		dbThread.interrupt();
	}
	if (!databasePath.isEmpty()) {
		FileUtils.deleteQuietly(new File(databasePath));
	}

}


protected void dropPersonsTable() throws ParsingException {
	SQLModule.update(statement, "DROP TABLE IF EXISTS Persons;");
}

class DBThread extends Thread {

private String	path;
private Server	server;
private String	name;

DBThread(	String path,
			String name) {
	this.path = path;
	this.name = name;
}


@Override
public void run() {

	HsqlProperties props = new HsqlProperties();
	props.setProperty("server.database.0", "file:" + path + "/" + name + ";");
	props.setProperty("server.dbname.0", name);
	server = new Server();
	// server.setTrace(true);
	// server.setSilent(false);
	try {
		server.setProperties(props);

	} catch (Exception e) {
		e.printStackTrace();
		return;
	}
	server.start();

}


@Override
public void interrupt() {
	super.interrupt();
	server.stop();
}

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
