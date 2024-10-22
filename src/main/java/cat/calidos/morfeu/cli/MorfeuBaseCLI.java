package cat.calidos.morfeu.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cat.calidos.morfeu.utils.Pair;
import picocli.CommandLine;


public abstract class MorfeuBaseCLI {

public static final int EX_OK = 0;
public static final int EX_NOINPUT = -66;
protected String output;

public static Pair<Integer, String> mainImpl(	MorfeuBaseCLI cli,
												String[] args) {
	int code = new CommandLine(cli).execute(args);
	return Pair.of(code, cli.getOutput());
}


public String getOutput() { return output; }


protected String readSystemIn() throws IOException {

	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	StringBuilder inputBuilder = new StringBuilder();
	String line;
	while ((line = reader.readLine()) != null) {
		inputBuilder.append(line).append("\n");
	}
	return inputBuilder.toString();
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
