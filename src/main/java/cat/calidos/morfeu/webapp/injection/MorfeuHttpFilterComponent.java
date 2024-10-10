package cat.calidos.morfeu.webapp.injection;

import dagger.producers.ProductionComponent;

import cat.calidos.morfeu.utils.injection.ListeningExecutorServiceModule;


/**
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = { HttpFilterModule.class, ListeningExecutorServiceModule.class })
public interface MorfeuHttpFilterComponent extends HttpFilterComponent {

//@formatter:off
@ProductionComponent.Builder
interface Builder extends HttpFilterComponent.Builder {}
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
