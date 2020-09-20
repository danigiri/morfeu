
package cat.calidos.morfeu.model.injection;

import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.model.Model;
import cat.calidos.morfeu.problems.ValidationException;
import dagger.producers.ProductionSubcomponent;

/**	Subcomponent to provide the model
* 	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionSubcomponent(modules={ModelModule.class})
public interface ModelSubcomponent {

ListenableFuture<Model> model() throws ValidationException, ExecutionException;

@ProductionSubcomponent.Builder
interface Builder {
	ModelSubcomponent builder();
}

}

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
