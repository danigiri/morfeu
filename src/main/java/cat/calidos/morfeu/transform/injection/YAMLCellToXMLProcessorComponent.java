package cat.calidos.morfeu.transform.injection;

import java.util.List;

import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.transform.JsonNodeCellModel;
import cat.calidos.morfeu.transform.PrefixProcessor;
import cat.calidos.morfeu.transform.injection.ContentJSONToXMLComponent.Builder;
import dagger.BindsInstance;
import dagger.Component;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = YAMLCellToXMLProcessorModule.class)
public interface YAMLCellToXMLProcessorComponent {

List<PrefixProcessor<JsonNodeCellModel, String>> processors(); // we return a list of children from
																// the node

PrefixProcessor<JsonNodeCellModel, String> processorSlash();// we return slash from the node

//@formatter:off
@Component.Builder
interface Builder {

	@BindsInstance Builder withPrefix(String pref);
	@BindsInstance Builder givenCase(@Named("Case") String case_);
	@BindsInstance Builder fromNode(JsonNode node);	// either a parent or a plain node (for the slash generation)
	@BindsInstance Builder cellModel(CellModel cellModel);

	YAMLCellToXMLProcessorComponent build();

}
//@formatter:on

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
