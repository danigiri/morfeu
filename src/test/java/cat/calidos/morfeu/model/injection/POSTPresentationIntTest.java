// POST PRESENTATION INT TEST . JAVA

package cat.calidos.morfeu.model.injection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import cat.calidos.morfeu.api.APITezt;
import cat.calidos.morfeu.utils.Config;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class POSTPresentationIntTest extends APITezt {


@Test //@DisplayName("Testing post presentation")
public void testPOSTPresentation() throws Exception {

	InputStream answerStream = postToRemote("preview/html/","text=text%20here&color=00ff0a");
	assertNotNull(answerStream);
	String answer = IOUtils.toString(answerStream, Config.DEFAULT_CHARSET);
	//System.err.println(answer);
	assertTrue(answer.contains("#00ff0a"));
	assertTrue(answer.contains("text here"));

}


}

/*
 *    Copyright 2024 Daniel Giribet
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

