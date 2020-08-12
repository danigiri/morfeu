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

