package cat.calidos.morfeu.transform.injection;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.fasterxml.jackson.databind.JsonNode;

import dagger.Module;
import dagger.Provides;

import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.transform.JsonNodeCellModel;
import cat.calidos.morfeu.transform.PrefixProcessor;

@Module
public class YAMLCellModelGuesserProcessorModule {


@Provides
List<PrefixProcessor<JsonNodeCellModel, String>> processors(String pref,
															@Named("Case") String case_,
															@Nullable @Named("name") String name,
															JsonNode node,
															ComplexCellModel parentCellModel) {

	var p = new LinkedList<PrefixProcessor<JsonNodeCellModel, String>>();
	p.add(new YAMLCellModelGuesserProcessor(pref, case_, name, new JsonNodeCellModel(node, parentCellModel)));

	return p;
}


}


