package cat.calidos.morfeu.utils.injection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;

import cat.calidos.morfeu.problems.MorfeuRuntimeException;
import cat.calidos.morfeu.problems.ParsingException;

/** Basic SQL interpreter using H2 with a sample database
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class SQLModule {

@Produces
public static List<List<String>> query(Statement statement, @Named("SQL") String query) throws ParsingException {

	List<List<String>> rows;
	try {
		ResultSet resultSet = statement.executeQuery(query);
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCount = rsmd.getColumnCount();
		rows = new ArrayList<List<String>>();
		List<String> header = new ArrayList<String>(columnCount);
		for (int i = 1; i <= columnCount; i++ ) {
			header.add(rsmd.getColumnLabel(i));			
		}
		rows.add(header);

		while (resultSet.next()) {
			List<String> row = new ArrayList<String>(columnCount);
			for (int i = 1; i <= columnCount; i++ ) {
				row.add(resultSet.getString(i));
			}
			rows.add(row);
		}

	} catch (SQLException e) {
		throw new ParsingException("Problem running query '"+query+"'", e);
	}

	return rows;

}


@Produces
public static Integer update(Statement statement, @Named("SQL") String update)  throws ParsingException  {
	try {
		return statement.executeUpdate(update);//==0 ? "" : "";
	} catch (SQLException e) {
		throw new ParsingException("Problem running update '"+update+"'", e);
	}
}


@Produces
public static Statement statement(Connection connection) throws MorfeuRuntimeException{
	try {
		return connection.createStatement();
	} catch (SQLException e) {
		throw new MorfeuRuntimeException("Could not connect to "+connection.toString(), e);
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

