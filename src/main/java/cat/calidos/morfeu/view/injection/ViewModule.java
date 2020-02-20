// VIEW MODULE . JAVA

package cat.calidos.morfeu.view.injection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;

import dagger.Module;
import dagger.Provides;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ViewModule {


@Provides
public static String render(JtwigTemplate template, JtwigModel model) {
	return template.render(model);
}


@Provides
public Map<String, Object> values(@Named("value") Object v, @Nullable @Named("problem") String problem) {

	Map<String, Object> value = new HashMap<String, Object>(1);
	if (problem!=null && problem!="") {

		problem = problem.replaceAll("\t|\n", " ");	// error messages may contain illegal JSON text
		problem = problem.replaceAll("\"", "'");	// 
		value.put("problem", problem);

	}
	value.put("hasProblem", Optional.ofNullable(problem));
	value.put("v", v);

	return value;

}


@Provides
public static EnvironmentConfiguration defaultConfiguration() {

	return EnvironmentConfigurationBuilder.configuration()
											.functions()
												.add(range)
												.add(chop)
												.add(list)
												.add(debn)
												.add(deb)
												.add(break_)
												.add(quote)
												.add(isMultiline)
												.add(multiline)
												.add(xmla)
												.add(xmlc)
												.add(yamlc)
												.add(yamla)
												.add(substr)
											.and()
											.build();

}


@Provides
public static JtwigTemplate produceTemplate(@Nullable @Named("templatePath") String path,
											@Nullable @Named("template") String template,
											EnvironmentConfiguration config) {

	if (path==null && template==null) {
		throw new NullPointerException("Cannot produce a template without a path or template value");
	}

	return path!=null ? JtwigTemplate.classpathTemplate(path, config) : JtwigTemplate.inlineTemplate(template, config);

}





@Provides
public static JtwigModel produceJTwigModel(Map<String, Object> values) {
	return JtwigModel.newModel(values);
}


final static SimpleJtwigFunction range = new SimpleJtwigFunction() {

	@Override
	public String name() {
		return "range";
	}

	@Override
	public Object execute(FunctionRequest request) {

			request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
    		int n = request.getEnvironment()
    						.getValueEnvironment()
    						.getNumberConverter()
    						.convert(request.get(0))
    						.get()
    						.intValue();
    		List<Integer> range = new ArrayList<Integer>(n);
    		for (int i=0; i<n; i++) {
    			range.add(i);
    		} 

    		return range;
	}

};

/** remove last char */
final static SimpleJtwigFunction chop = new SimpleJtwigFunction() {

    @Override
    public String name() {
        return "chop";
    }

	@Override
	public Object execute(FunctionRequest request) {

    		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
    		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));

    		return s.substring(0, s.length()-1);
	}

};


/** iterator to list */
final static SimpleJtwigFunction list = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "list";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
		@SuppressWarnings("unchecked")
		Iterator<Object> iterator = (Iterator<Object>) request.get(0);

		ArrayList<Object> list = new ArrayList<Object>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}

		return list;
	}

	};


final static SimpleJtwigFunction debn = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "debn";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(10);
		List<Object> arguments = request.getArguments();
		arguments.stream().forEachOrdered(System.err::print);
		System.err.println();

		return new Object();
	}

};


final static SimpleJtwigFunction deb = new SimpleJtwigFunction() {

	@Override
	public String name() {
		return "deb";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(10);
		List<Object> arguments = request.getArguments();
		arguments.stream().forEachOrdered(System.err::print);

		return new Object();
	}

};


final static SimpleJtwigFunction break_ = new SimpleJtwigFunction() {

	@Override
	public String name() {
		return "break";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(10);
		List<Object> arguments = request.getArguments();

		return new Object();

	}

};


/** add double quotes around the content */
final static SimpleJtwigFunction quote = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "quote";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
		String s = request.getEnvironment().getValueEnvironment()
							.getStringConverter().convert(request.get(0));
		s = (!s.startsWith("\"") && !s.endsWith("\"")) ? s = "\"" + s + "\"" : s;

		return s;

	}

};


/** returns true if the input has the '\n' literal */
final static SimpleJtwigFunction isMultiline = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "isMultiline";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
		// System.err.println("\t\t"+s+","+s.contains("\\n"));

		return s.contains("\\n");

	}

};


/** returns true if the input has the '\n' literal */
final static SimpleJtwigFunction multiline = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "multiline";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(2).maximumNumberOfArguments(2);
		String indent = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(1));
		String indentedLinefeed = "\n"+indent;
		
		return s.replace("\\n", indentedLinefeed);

	}

};


/** returns xml attribute scaping & --> &amp;, " --> &quot; */
final static SimpleJtwigFunction xmla = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "xmla";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));

		s = s.replace("&", "&amp;");
		
		if (s.startsWith("\"") && s.endsWith("\"")) {	// these are delimiters
			s = s.substring(1, s.length()-1);
			s = s.replace("\\\"", "&quot;");	// yaml escaped
			s = s.replace("\"", "&quot;");		// not yaml escaped
			s = "\""+s+"\"";
			
		} else {
			s = s.replace("\\\"", "&quot;");	// yaml escaped
			s = s.replace("\"", "&quot;");		// not yaml escaped
		}
		return s;

	}

};


/** returns xml content scaping & --> &amp;, < --> &gt; < --> &lt; */
final static SimpleJtwigFunction xmlc = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "xmlc";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));

		s = s.replace("&", "&amp;");
		s = s.replace(">", "&gt;");
		s = s.replace("<", "&lt;");

		return s;

	}

};


/** returns yaml content scaping \" --> " */
final static SimpleJtwigFunction yamlc = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "yamlc";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));

		s = s.replace("\\\"", "\"");

		return s;

	}

};


/** returns yaml attribute scaping \" --> ", */
final static SimpleJtwigFunction yamla = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "yamla";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(1);
		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));

		s = s.replace("\\\"", "\"");

		return s;

	}

};


/** returns yaml attribute scaping \" --> ", */
final static SimpleJtwigFunction substr = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "substr";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(3).maximumNumberOfArguments(3);
		String s = request.getEnvironment().getValueEnvironment().getStringConverter().convert(request.get(0));
		int start = request.getEnvironment()
								.getValueEnvironment()
								.getNumberConverter()
								.convert(request.get(1))
								.get()
								.intValue();
		int end = request.getEnvironment()
							.getValueEnvironment()
							.getNumberConverter()
							.convert(request.get(2))
							.get()
							.intValue();
		s = s.substring(start, end);

		return s;

	}

};


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