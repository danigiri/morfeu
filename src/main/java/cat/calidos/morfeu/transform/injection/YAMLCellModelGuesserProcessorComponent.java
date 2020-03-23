package cat.calidos.morfeu.transform.injection;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.transform.JsonNodeCellModel;
import cat.calidos.morfeu.transform.PrefixProcessor;
import cat.calidos.morfeu.transform.injection.YAMLCellToXMLProcessorComponent.Builder;
import dagger.BindsInstance;
import dagger.Component;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules={YAMLCellModelGuesserProcessorModule.class})
public interface YAMLCellModelGuesserProcessorComponent {

List<PrefixProcessor<JsonNodeCellModel, String>> processors();

@Component.Builder
interface Builder {

	@BindsInstance Builder withPrefix(String pref);
	@BindsInstance Builder givenCase(@Named("Case") String case_);
	@BindsInstance Builder name(@Nullable @Named("name") String name);
	@BindsInstance Builder fromNode(JsonNode node);
	@BindsInstance Builder parentCellModel(ComplexCellModel parentCellmodel);

	YAMLCellModelGuesserProcessorComponent build();

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

