/*
 *    Copyright 2016 Daniel Giribet
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

package cat.calidos.partikle.model.di;

import java.net.URI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import cat.calidos.partikle.model.Catalogue;

/**
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class CatalogueModule {

private String baseUri;

public CatalogueModule(String baseUri) {
	this.baseUri = baseUri;
}

@Provides 
Catalogue provideCatalogue(String uri) {
	URI catalogueUri = URI.create(baseUri+uri);
	return null;
}

}
