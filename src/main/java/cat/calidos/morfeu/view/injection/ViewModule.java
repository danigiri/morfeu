// VIEW MODULE . JAVA

package cat.calidos.morfeu.view.injection;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.common.hash.Hashing;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.FetchingException;
import cat.calidos.morfeu.utils.Config;
import cat.calidos.morfeu.utils.TemplateUtils;

import dagger.Module;
import dagger.Provides;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ViewModule {


@Provides
public String render(Template template, Map<String, Object> values) {
	try {
		var writer = new StringWriter();
		template.process(values, writer);
		return writer.toString();
	} catch (TemplateException e) {
		throw new RuntimeException(new ConfigurationException("Template '" + template + "' has issues", e));
	} catch (IOException e) {
		throw new RuntimeException(new FetchingException("Template '" + template + "' hit IO issues", e));
	}
}


@Provides
@Named("effectiveTemplate")
public static String effectiveTemplate(@Nullable @Named("templatePath") String path,
		@Nullable @Named("template") String template) {
	return path != null ? path
			: (template.substring(0, Math.min(template.length(), 10))) // so it's minimally readable
					+ Hashing.murmur3_32_fixed().hashString(template, Config.DEFAULT_NIO_CHARSET).toString();
}


@Provides
public static Configuration configuration(StringTemplateLoader stringLoader) {
	Configuration cfg = new Configuration(Configuration.VERSION_2_3_33);
	cfg.setDefaultEncoding("UTF-8");
	// use the logger below to debug
	var ctl = new ClassTemplateLoader(ViewModule.class, "/templates");
	var mtl = new MultiTemplateLoader(new TemplateLoader[] { stringLoader, ctl });

	cfg.setTemplateLoader(mtl);
	return cfg;
}


@Provides
public static StringTemplateLoader stringLoader(@Named("effectiveTemplate") String effectiveTemplate,
		@Nullable @Named("template") String template) {
	var loader = new StringTemplateLoader();
	if (template != null) {
		loader.putTemplate(effectiveTemplate, template);
	}
	return loader;
}


@Provides
public static Template template(Configuration configuration, @Named("effectiveTemplate") String template) {
	try {
		return configuration.getTemplate(template);
	} catch (TemplateNotFoundException e) {
		throw new IllegalArgumentException(new ConfigurationException("Template '" + template + "' not found", e));
	} catch (MalformedTemplateNameException | ParseException e) {
		throw new RuntimeException(new ConfigurationException("Template '" + template + "' has issues", e));
	} catch (IOException e) {
		throw new RuntimeException(new FetchingException("Template '" + template + "' hit IO issues", e));
	}
}


@Provides
public static Map<String, Object> values(@Named("value") Object v, @Nullable @Named("problem") String problem) {

	var value = new HashMap<String, Object>(1);
	if (problem != null && problem != "") {

		problem = problem.replaceAll("\t|\n", " "); // error messages may contain illegal JSON text
		problem = problem.replaceAll("\"", "'"); //
		value.put("problem", problem);

	}
	value.put("hasProblem", Optional.ofNullable(problem));
	value.put("v", v);
	value.put("f", new TemplateUtils());

	return value;

}



}


/*
package cat.calidos.morfeu.view.injection;

import java.io.IOException;

import freemarker.cache.ClassTemplateLoader;

class LoggingClassTemplateLoader extends ClassTemplateLoader {

    public LoggingClassTemplateLoader(Class<ViewModule> resourceLoaderClass, String basePackagePath) {
		super(resourceLoaderClass, basePackagePath);
	}

	@Override
    public Object findTemplateSource(String name) throws IOException {
        // Log the attempted path before processing
        System.out.println("AAAA Trying to load template: " + this.getBasePackagePath());
        return super.findTemplateSource(name);
    }
}

 */

//
// final static SimpleJtwigFunction range = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
// return "range";
// }
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// int n = request.getEnvironment()
// .getValueEnvironment()
// .getNumberConverter()
// .convert(request.get(0))
// .get()
// .intValue();
// List<Integer> range = new ArrayList<Integer>(n);
// for (int i=0; i<n; i++) {
// range.add(i);
// }
//
// return range;
// }
//
// };
//
//

//
//
/// ** iterator to list */
// final static SimpleJtwigFunction list = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "list";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// @SuppressWarnings("unchecked")
// Iterator<Object> iterator = (Iterator<Object>) request.get(0);
//
// ArrayList<Object> list = new ArrayList<Object>();
// while (iterator.hasNext()) {
// list.add(iterator.next());
// }
//
// return list;
// }
//
// };
//
//
// final static SimpleJtwigFunction debn = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "debn";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(10);
// List<Object> arguments = request.getArguments();
// arguments.stream().forEachOrdered(System.err::print);
// System.err.println();
//
// return new Object();
// }
//
// };
//
//
// final static SimpleJtwigFunction deb = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
// return "deb";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(10);
// List<Object> arguments = request.getArguments();
// arguments.stream().forEachOrdered(System.err::print);
//
// return new Object();
// }
//
// };
//
//
// final static SimpleJtwigFunction break_ = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
// return "break";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(10);
// List<Object> arguments = request.getArguments();
//
// return new Object();
//
// }
//
// };
//
//
/// ** returns true if the input has the '\n' literal */
// final static SimpleJtwigFunction isMultiline = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "isMultiline";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
// // System.err.println("\t\t"+s+","+s.contains("\\n"));
//
// return s.contains("\\n");
//
// }
//
// };
//
//
/// ** returns true if the input has the '\n' literal */
// final static SimpleJtwigFunction multiline = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "multiline";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(2).maximumNumberOfArguments(2);
// String indent =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(1));
// String indentedLinefeed = "\n"+indent;
//
// return s.replace("\\n", indentedLinefeed);
//
// }
//
// };
//
//
/// ** returns xml attribute scaping & --> &amp;, " --> &quot; */
// final static SimpleJtwigFunction xmla = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "xmla";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
//
// s = s.replace("&", "&amp;");
//
// if (s.startsWith("\"") && s.endsWith("\"")) { // these are delimiters
// s = s.substring(1, s.length()-1);
// s = s.replace("\\\"", "&quot;"); // yaml escaped
// s = s.replace("\"", "&quot;"); // not yaml escaped
// s = "\""+s+"\"";
//
// } else {
// s = s.replace("\\\"", "&quot;"); // yaml escaped
// s = s.replace("\"", "&quot;"); // not yaml escaped
// }
// return s;
//
// }
//
// };
//
//
/// ** returns xml content scaping & --> &amp;, < --> &gt; < --> &lt; */
// final static SimpleJtwigFunction xmlc = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "xmlc";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
//
// s = s.replace("&", "&amp;");
// s = s.replace(">", "&gt;");
// s = s.replace("<", "&lt;");
//
// return s;
//
// }
//
// };
//
//
/// ** returns yaml content scaping \" --> " */
// final static SimpleJtwigFunction yamlc = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "yamlc";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
//
// s = s.replace("\\\"", "\"");
//
// return s;
//
// }
//
// };
//
//
/// ** returns yaml attribute scaping \" --> ", */
// final static SimpleJtwigFunction yamla = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "yamla";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
//
// s = s.replace("\\\"", "\"");
//
// return s;
//
// }
//
// };
//
//
/// ** returns yaml attribute scaping \" --> ", */
// final static SimpleJtwigFunction substr = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "substr";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(3).maximumNumberOfArguments(3);
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
// int start = request.getEnvironment()
// .getValueEnvironment()
// .getNumberConverter()
// .convert(request.get(1))
// .get()
// .intValue();
// int end = request.getEnvironment()
// .getValueEnvironment()
// .getNumberConverter()
// .convert(request.get(2))
// .get()
// .intValue();
// s = s.substring(start, end);
//
// return s;
//
// }
//
// };
//
//
//
/// ** returns safe json attribute scaping \ --> \\ */
// final static SimpleJtwigFunction jsona = new SimpleJtwigFunction() {
//
// @Override
// public String name() {
//
// return "jsona";
// }
//
//
// @Override
// public Object execute(FunctionRequest request) {
//
// request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
// String s =
// request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
//
// s = s.replace("\\", "\\\\");
//
// return s;
//
//
// }
//
// };
//


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
