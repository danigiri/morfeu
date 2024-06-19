package cat.calidos.morfeu.view.injection;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.utils.injection.DaggerSQLComponent;
import cat.calidos.morfeu.utils.injection.SQLComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SQLViewModule {

private static final String TEMPLATE = "view/sql.ftl";


@Provides
public static String render(SQLComponent sql, @Nullable @Named("isUpdate") boolean isUpdate) {

	Map<String, Object> v;
	String problem = null;
	try {
		if (isUpdate) {
			Integer result = sql.update().get();
			v = MorfeuUtils.paramMap("isUpdate", true, "result", result);
		} else {
			List<List<String>> results = sql.query().get();
			v = MorfeuUtils.paramMap("isUpdate", false, "results", results);
		}
	} catch (Exception e) {
		problem = e.getMessage();
		v = MorfeuUtils.paramMap();
	}

	return DaggerViewComponent.builder().withTemplatePath(TEMPLATE).withValue(v).andProblem(problem).build().render();
}


@Provides
public static SQLComponent sql(Connection connection, @Named("query") String query) {
	return DaggerSQLComponent.builder().sql(query).andConnection(connection).build();
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

