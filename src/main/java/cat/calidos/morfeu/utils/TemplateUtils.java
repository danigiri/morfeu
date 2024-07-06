package cat.calidos.morfeu.utils;


public class TemplateUtils {

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
		s = s.substring(1, s.length() - 1);
		s = s.replace("\\\"", "&quot;"); // yaml escaped
		s = s.replace("\"", "&quot;"); // not yaml escaped
		s = "\"" + s + "\"";

	} else {
		s = s.replace("\\\"", "&quot;"); // yaml escaped
		s = s.replace("\"", "&quot;"); // not yaml escaped
	}
	return s;
}


/** returns yaml attribute scaping \" --> ", */
public String yamla(String s) {
	s = s.replace("\\\"", "\"");
	return s;
}


/** returns yaml content scaping \" --> " */
public String yamlc(String s) {
	 s = s.replace("\\\"", "\"");
	 return s;
}

// convert multiline string into a yaml multiline value
public String multiline(String indent, String s) {
	 var indentedLinefeed = "\n"+indent;
	 return s.replace("\\n", indentedLinefeed);
}


// this is used to reduce indentation in yaml by two spaces (just by removing two chars at the beginning
public String trimtwo(String s) {
	return s.substring(2);
}


// this is used to increase indentation in yaml, by appending two spaces at the end
public String addtwospaces(String s) {
	return s.concat("  ");
}


public String debug(Object...objects) {
	return "";
}


public String p(Object...objects) {
	var s = new StringBuffer();
	var i = 0;
	int length = objects.length-1;
	while (i<length) {
		s.append(objects[i++]+":'"+objects[i++]+"'");
		if (i<length) {
			s.append(",");
		}
	}
	return s.toString();
}


public String y(Object...objects) {
	return "# "+p(objects);
}


public String d(String s) {
	System.err.println(s);
	return s;
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
