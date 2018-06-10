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
import java.util.stream.Collectors;
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
									@Named("CellModelFilter") Optional<URI> cellModelFilter) throws ParsingException  {

	if (cellModelFilter.isPresent()) {
		URI filter = cellModelFilter.get();
		CellModel cellModel = cellModels.stream().map(cm -> lookForCellModel(cm,filter))
													.findFirst()
													.get()
													.orElseThrow(() ->  new ParsingException("Wrong filter "+filter));
	
		return Collections.singletonList(cellModel);
	
	} else {

		return cellModels;
		
	}

}


@Named("CellModelFilter")
@BindsOptionalOf abstract URI cellModelFilter();


private static Optional<CellModel> lookForCellModel(CellModel cellModel, URI filter) {
	
	Optional<CellModel> found;
	
	if (cellModel.getURI().equals(filter)) {
		found = Optional.of(cellModel);
	} else if (cellModel.isSimple()) {
		found = Optional.empty();
	} else {

		return cellModel.asComplex()
							.children()
							.asList()
							.stream()
							.map(cm -> lookForCellModel(cm, filter))
							.filter(f -> f.isPresent())
							.findAny()	// returns an Optional<Optional<CellModel>>
							.get();
		
	}
	
	return found;
}

}
