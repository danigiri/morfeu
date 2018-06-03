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

package cat.calidos.morfeu.model.injection;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Named;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.ComplexCellModel;
import cat.calidos.morfeu.problems.ParsingException;
import dagger.BindsOptionalOf;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public abstract class CellModelsFilterModule {

// it may be that we want to match content with a cell model deep down the hierarchy
@Produces @Named("CellModels") 
public static List<CellModel> filterCellModels(@Named("RootCellModels") List<CellModel> cellModels, 
									@Named("CellModelFilter") Optional<URI> filter) throws ParsingException  {

	List<CellModel> effectiveCellModels = filter.isPresent() 
												? lookForCellModel(cellModels, filter.get())
												: cellModels;
	if (filter.isPresent() && effectiveCellModels.isEmpty()) {
		throw new ParsingException("Incorrect cell model filter "+filter.get());
	}

	return effectiveCellModels;

}


@Named("CellModelFilter")
@BindsOptionalOf abstract URI cellModelFilter();


private static List<CellModel> lookForCellModel(List<CellModel> cellModels, URI filter) {
	
	Optional<CellModel> found = cellModels.stream().filter( cm -> cm.getURI().equals(filter)).findFirst();
	if (!found.isPresent()) {
	
		Iterator<ComplexCellModel> children = cellModels.stream()
															.filter(cm -> cm.isComplex())
															.map(cm -> cm.asComplex())
															.iterator();
		while (children.hasNext() && !found.isPresent()) {
			List<CellModel> childList = new ArrayList<CellModel>(1);
			childList.add(children.next());
			List<CellModel> matchedCellModel = lookForCellModel(childList, filter);
			if (matchedCellModel.size()>0) {
				found = Optional.of(matchedCellModel.get(0));
			}
		}
	}

	return found.isPresent() ? Collections.singletonList(found.get()) : Collections.emptyList();	
}

}
