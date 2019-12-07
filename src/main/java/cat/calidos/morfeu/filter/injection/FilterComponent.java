// FILTER COMPONENT . JAVA

package cat.calidos.morfeu.filter.injection;

import javax.inject.Named;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;
import cat.calidos.morfeu.filter.Filter;
import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules={FilterModule.class, BasicFiltersModule.class, AdvancedFiltersModule.class,
								ListeningExecutorServiceModule.class})
public interface FilterComponent {

ListenableFuture<Filter<String, String>> stringToString();
ListenableFuture<Filter<String, Object>> stringToObject();
ListenableFuture<Filter<Object, String>> objectToString();
ListenableFuture<Filter<Object, Object>> objectToObject();

@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder filters(@Named("Filters") String filters);

	FilterComponent build();

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
