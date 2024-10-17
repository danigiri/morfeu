package cat.calidos.morfeu.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.model.injection.ModelTezt;


public class MorfeuCLIParserTest  extends ModelTezt {

@Test @DisplayName("Content GET test")
public void parseContent() throws Exception {
	
	String prefix = testAwareFullPathFrom(".");
	String path = "test-resources/documents/document1.xml";
	String model = "test-resources/models/test-model.xsd";

	MorfeuCLIParser cli = mock(MorfeuCLIParser.class);
	var output = new PrintStream(new ByteArrayOutputStream());
	when(cli.getOutStream()).thenReturn(output);
	int code = MorfeuCLIParser.mainImpl(new String[] {"--model", model, "--prefix", prefix, path});

	assertEquals(0, code);
	System.out.println(output.toString());
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
