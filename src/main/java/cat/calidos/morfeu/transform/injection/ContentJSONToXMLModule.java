package cat.calidos.morfeu.transform.injection;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import dagger.Module;
import dagger.Provides;

import cat.calidos.morfeu.transform.ContentJSONToXMLProcessor;
import cat.calidos.morfeu.transform.ContentJSONToXMLProcessorSlash;
import cat.calidos.morfeu.transform.Processor;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ContentJSONToXMLModule {


@Provides @Named("begin")
Processor<JsonNode, String> processor(String pref, JsonNode node) {
	return new ContentJSONToXMLProcessor(pref, node);
}


@Provides @Named("end")
Processor<JsonNode, String> processorSlash(String pref, JsonNode node) {
	return new ContentJSONToXMLProcessorSlash(pref, node);
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

