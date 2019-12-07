package cat.calidos.morfeu.filter.injection;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.filter.Filter;
import cat.calidos.morfeu.transform.injection.DaggerContentConverterComponent;
import cat.calidos.morfeu.view.injection.DaggerViewComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public class ContentFiltersModule {


@Produces @IntoMap @Named("objectToString")
@StringKey("content-to-xml")
Filter<Object, String> contentToXML() {
	return (json) -> DaggerContentConverterComponent.builder().from((JsonNode)json).build().xml();
}


@Produces @IntoMap @Named("objectToString")
@StringKey("content-to-yaml")
Filter<Object, String> contentToYAML() {
	return (values) -> DaggerViewComponent.builder()
											.withTemplatePath("templates/transform/content-to-yaml.twig")
											.withValue(values)
											.build()
											.render();
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
