// CELL MODELS FILTER MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import java.net.URI;
import java.util.Optional;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cat.calidos.morfeu.model.CellModel;
import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ParsingException;
import dagger.BindsOptionalOf;
import dagger.producers.ProducerModule;
import dagger.producers.Produces;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProducerModule
public abstract class CellModelsFilterModule {

protected final static Logger log = LoggerFactory.getLogger(CellModelsFilterModule.class);


// it may be that we want to match content with a cell model deep down the hierarchy
@Produces @Named("CellModel") 
public static CellModel filterCellModels(Model model, 
									@Named("CellModelFilter") Optional<URI> cellModelFilter) throws ParsingException  {

	// if there is a filter we want to look for it in the model hierarchy
	if (cellModelFilter.isPresent()) {
		URI filter = cellModelFilter.get();
		log.trace("*** Looking for cell model filter "+filter);
		CellModel cellModel = model.children().stream()
												.map(cm -> lookForCellModel(cm,filter))
												.findFirst()
												.get()
												.orElseThrow(() ->  new ParsingException("Wrong filter "+filter));

		return cellModel;

	} else {

		return model;

	}

}


@Named("CellModelFilter")
@BindsOptionalOf abstract URI cellModelFilter();


private static Optional<CellModel> lookForCellModel(CellModel cellModel, URI filter) {

	Optional<CellModel> found;

 	if (cellModel.getURI().equals(filter)) {
		found = Optional.of(cellModel);
	} else if (cellModel.isSimple() || cellModel.isReference()) {	// guard against references to avoid infinite loops
		found = Optional.empty();
	} else {

		// do a recursive search, for each child do a recursive call and unfold the optional if found in the children
		// if no children have it, the last filter will not find match anything so we return a plain empty
		found = cellModel.asComplex()
							.children()
							.asList()
							.stream()
							.map(cm -> lookForCellModel(cm, filter))
							.filter(f -> f.isPresent())
							.findAny()	// return Optional<Optional<CellModel>>
							.orElse(Optional.empty());
	
	}

	return found;
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

