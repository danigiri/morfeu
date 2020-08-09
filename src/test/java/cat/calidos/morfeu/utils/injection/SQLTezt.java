package cat.calidos.morfeu.utils.injection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import cat.calidos.morfeu.problems.ParsingException;


public class SQLTezt {

protected Connection connection;
protected Statement statement;


@BeforeEach
public void setupDatabase() throws Exception {

	org.h2.Driver.load();
	connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "sa");
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

}


protected void dropPersonsTable() throws ParsingException {
	SQLModule.update(statement, "DROP TABLE IF EXISTS Persons");
}


}

