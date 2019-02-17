package cat.calidos.morfeu.transform.injection;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.transform.Processor;
import dagger.BindsInstance;
import dagger.Component;

@Component(modules={ContentJSONToXMLModule.class})
public interface ContentJSONToXMLComponent {

@Named("begin")	Processor<JsonNode, String> processor();
@Named("end")	Processor<JsonNode, String> processorSlash();


@Component.Builder
interface Builder {

	@BindsInstance Builder fromNode(JsonNode node);
	@BindsInstance Builder withPrefix(String pref);

	ContentJSONToXMLComponent builder();

}

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
