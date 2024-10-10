package cat.calidos.morfeu.utils.injection;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

import org.w3c.dom.Node;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.problems.ConfigurationException;
import cat.calidos.morfeu.problems.ParsingException;


/**
 * Utility component to get an XML document out of a string
 * 
 * @author daniel giribet
 *//////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = { XMLNodeToStringModule.class,
		ListeningExecutorServiceModule.class })
public interface XMLNodeToStringComponent {

ListenableFuture<String> xml() throws ConfigurationException, ParsingException;

//@formatter:off
@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder fromNode(Node node);

	XMLNodeToStringComponent build();

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
