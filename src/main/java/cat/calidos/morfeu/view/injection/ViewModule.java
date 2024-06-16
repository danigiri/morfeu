// VIEW MODULE . JAVA

package cat.calidos.morfeu.view.injection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.thymeleaf.templateresolver.StringTemplateResolver;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import dagger.Module;
import dagger.Provides;

/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ViewModule {


@Provides
public static String render(TemplateEngine engine, @Nullable @Named("templatePath") String path,
		@Nullable @Named("template") String template, IContext context) {

	return path != null ? engine.process(path, context) : engine.process(template, context);

}


@Provides
public Map<String, Object> values(@Named("value") Object v, @Nullable @Named("problem") String problem) {

	Map<String, Object> value = new HashMap<String, Object>(1);
	if (problem != null && problem != "") {

		problem = problem.replaceAll("\t|\n", " "); // error messages may contain illegal JSON text
		problem = problem.replaceAll("\"", "'"); //
		value.put("problem", problem);

	}
	value.put("hasProblem", Optional.ofNullable(problem));
	value.put("v", v);

	return value;

}


@Provides
public IContext context(Map<String, Object> values) {

	Context ctx = new Context();
	values.forEach((k, v) -> ctx.setVariable(k, v));
	return ctx;
}


@Provides
ITemplateResolver resolver(@Nullable @Named("templatePath") String path, @Nullable @Named("template") String template) {

	// TODO: we can probably chain both
	if (path == null) {
		StringTemplateResolver stringResolver = new StringTemplateResolver();
		stringResolver.setTemplateMode(TemplateMode.TEXT);
		return stringResolver;

	} else {
		ClassLoaderTemplateResolver classLoaderResolver = new ClassLoaderTemplateResolver();
		classLoaderResolver.setTemplateMode(TemplateMode.TEXT);
		return classLoaderResolver;
	}
}


@Provides
public static TemplateEngine engine(ITemplateResolver resolver, AbstractDialect dialect) {


	// TODO: turn into a singleton
	TemplateEngine engine = new TemplateEngine();
	engine.setTemplateResolver(resolver);
	engine.addDialect(dialect);

	return engine;

}


@Provides
AbstractDialect dialect(IExpressionObjectFactory expressionObjectFactory) {

	return new ExpressionDialect(expressionObjectFactory);
}


@Provides
IExpressionObjectFactory expressionObjectFactory() {

	return new IExpressionObjectFactory() {

	private static final String STR = "str";

	@Override
	public boolean isCacheable(String expressionObjectName) {

		return true;
	}


	@Override
	public Set<String> getAllExpressionObjectNames() {

		return Set.of(STR);
	}


	@Override
	public Object buildObject(IExpressionContext context, String expressionObjectName) {

		if (expressionObjectName.equals(STR)) {
			return new Str();
		}
		return null;
	}
	};
}


private class ExpressionDialect extends AbstractDialect implements IExpressionObjectDialect {

private IExpressionObjectFactory expressionObjectFactory;

public ExpressionDialect(IExpressionObjectFactory expressionObjectFactory) {

	super("morfeu");
	this.expressionObjectFactory = expressionObjectFactory;
}


@Override
public IExpressionObjectFactory getExpressionObjectFactory() {

	return expressionObjectFactory;
}
}

@SuppressWarnings("unused")
private final class Str {

/** remove last char */
public String chop(String s) {
	return s.substring(0, s.length() - 1);
}


/** adds double quotes around the string if they are not there */
public String quote(String s) {
	s = (!s.startsWith("\"") && !s.endsWith("\"")) ? s = "\"" + s + "\"" : s;
	return s;
}


/** returns true if the input has the '\n' literal */
public boolean isMultiline(String s) {
	return s.contains("\\n");
}


/** returns xml content scaping & --> &amp;, < --> &gt; < --> &lt; */
public String xmlc(String s) {
	s = s.replace("&", "&amp;");
	s = s.replace(">", "&gt;");
	s = s.replace("<", "&lt;");
	return s;
}


/** returns safe json attribute scaping '\' --&gt; '\\' */
public String jsona(String s) {
	s = s.replace("\\", "\\\\");
	return s;
}


/** returns xml attribute scaping & --> &amp;, " --> &quot; */
public String xmla(String s) {
	 s = s.replace("&", "&amp;");
	
	 if (s.startsWith("\"") && s.endsWith("\"")) { // these are delimiters
	 s = s.substring(1, s.length()-1);
	 s = s.replace("\\\"", "&quot;"); // yaml escaped
	 s = s.replace("\"", "&quot;"); // not yaml escaped
	 s = "\""+s+"\"";
	
	 } else {
	 s = s.replace("\\\"", "&quot;"); // yaml escaped
	 s = s.replace("\"", "&quot;"); // not yaml escaped
	 }
	 return s;
}

}

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


}

/*
 * Copyright 2019 Daniel Giribet
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
