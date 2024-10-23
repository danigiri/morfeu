package cat.calidos.morfeu.cli;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import cat.calidos.morfeu.control.ContentGETControl;
import cat.calidos.morfeu.control.ContentSaveControl;


/**
 * Utility CLI helpful for diagnostics
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@Command(name = "MorfeuCLI", version = "MorfeuCLI 0.8", mixinStandardHelpOptions = true)
public class MorfeuCLI extends MorfeuBaseCLI implements Callable<Integer> {

public static final String	PARSE	= "parse";
public static final String	SAVE	= "save";

@Option(names = { "-m", "--model" }, required = true, description = "model to use")
String modelPath;

@Option(names = { "-p", "--prefix" }, description = "model to use (default is file://<cwd>)")
String prefix;

@Option(names = "--filters", description = "filters (default is no filtering)")
String filters;

@Option(names = { "-q", "--quiet" }, description = "do not print anything")
boolean quiet = false;

@Parameters(description = "command {parse|save}")
String command;

@Parameters(description = "content to parse or destination to save into")
String path;

@Override
public Integer call() {

	Optional<String> appliedfilters = Optional.ofNullable(filters);
	prefix = prefix == null ? "file://" + System.getProperty("user.dir") : prefix;
	// we can happily reuse the content controllers
	if (command.equalsIgnoreCase(PARSE)) {
		output = new ContentGETControl(prefix, path, appliedfilters, modelPath).processRequest();
	} else if (command.equalsIgnoreCase(SAVE)) {
		String content;
		try {
			content = readSystemIn();
		} catch (IOException e) {
			e.printStackTrace();
			return EX_NOINPUT;
		}
		output = new ContentSaveControl(prefix, path, content, appliedfilters, modelPath)
				.processRequest();
	}
	if (!quiet) {
		System.out.println(output);
	}
	return EX_OK;
}


public static void main(String[] args) {
	System.exit(MorfeuCLI.mainImpl(new MorfeuCLI(), args).getLeft());
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
