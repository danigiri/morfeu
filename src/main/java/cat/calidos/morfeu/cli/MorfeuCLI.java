package cat.calidos.morfeu.cli;

import java.util.Optional;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import cat.calidos.morfeu.control.ContentGETControl;
import cat.calidos.morfeu.utils.Pair;


/**
 * Utility CLI helpful for diagnostics
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@Command(name = "MorfeuCLI", version = "MorfeuCLI 0.8", mixinStandardHelpOptions = true)
public class MorfeuCLI implements Callable<Integer> {

public static final String PARSE = "parse";

@Option(names = "--model", description = "model to use")
String modelPath;

@Option(names = "--prefix", description = "model to use (default is file://cwd)")
String prefix;

@Option(names = "--filters", description = "filters")
String filters;

@Option(names = { "-q", "--quiet" }, description = "do not print anything")
boolean quiet = false;

@Parameters(description = "command {parse|}")
String command;

@Parameters(description = "content to parse")
String path;

private String output;

@Override
public Integer call() {

	if (command.equalsIgnoreCase(PARSE)) {
		Optional<String> appliedfilters = Optional.ofNullable(filters);
		prefix = prefix == null ? "file://" + System.getProperty("user.dir") : prefix;
		// we can happily reuse the content controller
		output = new ContentGETControl(prefix, path, appliedfilters, modelPath).processRequest();
		if (!quiet) {
			System.out.println(output);
		}
	}

	return 0;
}


public static void main(String[] args) {
	int exitCode = mainImpl(args).getLeft();
	System.exit(exitCode);
}


// this decoupling is useful to do testing of the implementation
public static Pair<Integer, String> mainImpl(String[] args) {
	MorfeuCLI cli = new MorfeuCLI();
	int code = new CommandLine(cli).execute(args);
	return Pair.of(code, cli.getOutput());
}


public String getOutput() { return output; }

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
