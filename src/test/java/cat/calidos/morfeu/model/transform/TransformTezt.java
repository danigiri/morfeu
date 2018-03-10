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

package cat.calidos.morfeu.model.transform;

import static org.junit.Assert.assertFalse;

import java.io.File;

import javax.xml.transform.Source;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import cat.calidos.morfeu.model.injection.ModelTezt;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TransformTezt extends ModelTezt {

protected void compareWithXML(String content, String path) {

	Source transformedSource = Input.fromString(content).build();
	
	File originalFile = new File(path);
	Source originalSource = Input.fromFile(originalFile).build();

	Diff diff = DiffBuilder.compare(originalSource)
							.withTest(transformedSource)
							.ignoreComments()
							.ignoreWhitespace()
							.build();
	
	assertFalse("Transformed JSON to XML should be the same as original"+diff.toString(), diff.hasDifferences());
}

}
