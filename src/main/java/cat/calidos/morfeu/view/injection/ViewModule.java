/*
 *    Copyright 2018 Daniel Giribet
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

package cat.calidos.morfeu.view.injection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
* @author daniel giribet
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
												.add(deb)
												.add(quote)
												.add(isMultiline)
												.add(multiline)
								//			.escape()
								//			.withDefaultEngine("js")
											.and()
//											.parser()
//												.syntax()
//													.withStartCode("$(").withEndCode(")$")
//													.withStartOutput("$[").withEndOutput("]$")
//													.withStartComment("$#").withEndComment("#$")
//												.and()
//											.and()
											.build();
	
}


@Provides
public static JtwigTemplate produceTemplate(@Named("templatePath") String path, EnvironmentConfiguration config) {
	return JtwigTemplate.classpathTemplate(path, config);
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


final static SimpleJtwigFunction deb = new SimpleJtwigFunction() {

	@Override
	public String name() {

		return "deb";
	}


	@Override
	public Object execute(FunctionRequest request) {

		request.minimumNumberOfArguments(1).maximumNumberOfArguments(10);
		Object v = request.get(0);
		//System.err.println(v);

		return new Object();
	}

};


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
		if (!s.startsWith("\"") && !s.endsWith("\"")) {
			s = "\"" + s + "\"";
		}

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
		String s = request.getEnvironment().getValueEnvironment()
							.getStringConverter().convert(request.get(0));
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
		String indent = request.getEnvironment().getValueEnvironment()
								.getStringConverter().convert(request.get(0));
		String s = request.getEnvironment().getValueEnvironment()
							.getStringConverter().convert(request.get(1));
		String indentedLinefeed = "\n"+indent;
		
		return s.replace("\\n", indentedLinefeed);

	}

};


}