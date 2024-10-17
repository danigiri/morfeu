package cat.calidos.morfeu.cli;

import java.io.PrintStream;
import java.util.Optional;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import cat.calidos.morfeu.control.ContentGETControl;


/**
 * Utility CLI helpful for diagnostics Example usage: `java -cp
 * "target/morfeu-webapp-*-SNAPSHOT/WEB-INF/lib/picocli-4.7.6.jar:target/classes" \
 * cat.calidos.morfeu.cli.MorfeuCLIParser`
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@Command(name = "MorfeuCLIParser", version = "MorfeuParser 0.8", mixinStandardHelpOptions = true)
public class MorfeuCLIParser implements Callable<Integer> {

@Option(names = "--model", description = "model to use")
String modelPath;

@Option(names = "--prefix", description = "model to use (default is file://cwd)")
String prefix;

@Option(names = "--filters", description = "filters")
String filters;

@Parameters(description = "content to parse")
String path;

@Override
public Integer call() {

	Optional<String> appliedfilters = Optional.ofNullable(filters);
	prefix = prefix == null ? "file://" + System.getProperty("user.dir") : prefix;
	// we can happily reuse the content controller
	String out = new ContentGETControl(prefix, path, appliedfilters, modelPath).processRequest();
	getOutStream().print(out);
	return 0;
}


public static void main(String[] args) {

	int exitCode = mainImpl(args);
	System.exit(exitCode);
}


public static int mainImpl(String[] args) {
	return  new CommandLine(new MorfeuCLIParser()).execute(args);
}

public PrintStream getOutStream() {
	return System.out;
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
