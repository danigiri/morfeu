// SVG PREVIEW CONTROL MODULE . JAVA

package cat.calidos.morfeu.control.injection;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import java.sql.Connection;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import jakarta.servlet.ServletContext;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.control.MorfeuServlet;
import cat.calidos.morfeu.control.SVGPreviewGETControl;
import cat.calidos.morfeu.utils.MorfeuUtils;
import cat.calidos.morfeu.utils.injection.DaggerSQLComponent;
import cat.calidos.morfeu.view.injection.DaggerSQLViewComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import cat.calidos.morfeu.webapp.injection.ControlComponent;
import cat.calidos.morfeu.webapp.GenericHttpServlet;

/** Controller module to generatie dynamic previews (SVG and TXT supported)
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class PreviewControlModule {

protected final static Logger log = LoggerFactory.getLogger(PreviewControlModule.class);

private static final String HEADER_PARAM = "__header";	// this is used as the SVG header
private static final Pattern colorRegexp = Pattern.compile("^[0-9a-fA-F]{6}$");


@Provides @IntoMap @Named("GET")
@StringKey("/preview/svg/(.+)")
public static BiFunction<List<String>, Map<String, String>, String> getContentSVG() {

	return (pathElems, params) -> {

		String resourcesPrefix = params.get(MorfeuServlet.RESOURCES_PREFIX);
		String path = pathElems.get(1);		// normalised already
		Optional<String> header = PreviewControlModule.extractHeaderFrom(params);

		params = GenericHttpServlet.removeInternalHeaders(params);	// remove all __* we do not want as a param

		return new SVGPreviewGETControl(resourcesPrefix, path, header, params).processRequest();

	};

}


@Provides @IntoMap @Named("GET")
@StringKey("/preview/html/(.*)")
public static BiFunction<List<String>, Map<String, String>, String> getContentHTML() {

	return (pathElems, params) -> {

		String path = pathElems.get(1);		// normalised already
		String color = params.get("color");

		return renderPresentation(path, color);

	};

}


@Provides @IntoMap @Named("POST")
@StringKey("/preview/html/(.*)")
public static BiFunction<List<String>, Map<String, String>, String> getContentHTMLPOST() {
	return (pathElems, params) -> {

		String path = params.get("text");
		String color = params.get("color");

		return renderPresentation(path, color);

	};
}



@Provides @IntoMap @Named("GET")
@StringKey("/preview/code/?")
public BiFunction<List<String>, Map<String, String>, String> getCodePreview(@Nullable ServletContext context) {
	return (pathElems, params) -> {

		Connection connection = (Connection)context.getAttribute("connection");
		initDatabase(connection, context);	//FIXME: we need to listen to servlet state and clean the connection up!!!
		String query = params.get("sql");
		return DaggerSQLViewComponent.builder().query(query).isUpdate(false).andConnection(connection).build().render();

	};
}


@Provides @IntoMap @Named("Content-Type")
@StringKey("/preview/svg/(.+)")
public static String contentTypeSVG(@Named("Path") String path) {
	return ControlComponent.SVG;
}


@Provides @IntoMap @Named("Content-Type")
@StringKey("/preview/html/(.*)")
public static String contentTypeHTML(@Named("Path") String path) {
	return ControlComponent.TEXT;
}


private static String renderPresentation(String path, String color) {

	String template = "<div class=\"card\">\n" + 
					"	<div class=\"card-body html-preview\" style=\"background-color: #{{v.color}}\">\n" + 
					"		<h4 class=\"card-title html-preview-title\">{{v.path}}</h4>\n" + 
					"	</div>\n" + 
					"</div>";

	color = color!=null && colorRegexp.matcher(color).matches() ? color : "ff0000";
	Map<String, String> values = MorfeuUtils.paramStringMap("path", path, "color", color);

	return DaggerViewComponent.builder().withTemplate(template).withValue(values).build().render();

}


private static Optional<String> extractHeaderFrom(Map<String, String> params) {

	Optional<String> header =  Optional.empty();
	if (params.containsKey(HEADER_PARAM)) {
		String param = params.get(HEADER_PARAM);
		header = Optional.ofNullable(param);
	}

	return header;

}


private void initDatabase(Connection conn, ServletContext context) {

	try {
		log.info("Creating test database...");
		String tables = "SHOW TABLES";
		List<List<String>> pt = DaggerSQLComponent.builder().sql(tables).andConnection(conn).build().query().get();
		if (!contains(pt, "PERSONS")) {
			String create = "CREATE TABLE Persons (\n" + 
							"	PersonID int,\n" + 
							"	LastName varchar(255),\n" + 
							"	FirstName varchar(255),\n" + 
							"	Address varchar(255)\n," + 
							"	City varchar(255) " +
				")";
			DaggerSQLComponent.builder().sql(create).andConnection(conn).build().update().get();
		}
		String count = "SELECT COUNT(*) FROM Persons";
		List<List<String>> c = DaggerSQLComponent.builder().sql(count).andConnection(conn).build().query().get();
		if (!contains(c, "2")) {
			String insert = "INSERT INTO Persons VALUES (1, 'Doe', 'John', 'foo', 'fairyland')";
			DaggerSQLComponent.builder().sql(insert).andConnection(conn).build().update().get();
			insert = "INSERT INTO Persons VALUES (1, 'Doe', 'Daisy', 'bar', 'fairyland')";
			DaggerSQLComponent.builder().sql(insert).andConnection(conn).build().update().get();
		}
	} catch (Exception e) {
		log.error("Problem creating test database ", e.getMessage());
		e.printStackTrace();
	}

}


private boolean contains(List<List<String>> result, String toFind) {
	return result.stream().anyMatch(list -> list.stream().anyMatch(s -> s.equals(toFind)));
}


}

/*
 *    Copyright 2019 Daniel Giribet
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