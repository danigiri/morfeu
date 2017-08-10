/*
 *    Copyright 2017 Daniel Giribet
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

import javax.annotation.Nullable;
import javax.inject.Named;

import org.w3c.dom.Node;

import com.sun.xml.xsom.XSAnnotation;

import dagger.BindsInstance;
import dagger.Component;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules=ModelMetadataModule.class)
public interface ModelMetadataComponent {

String value();

@Component.Builder
interface Builder {

	@BindsInstance Builder from(@Nullable XSAnnotation annotation);
	@BindsInstance Builder named(@Named("tag") String tag);
	
	ModelMetadataComponent build();

}

}
