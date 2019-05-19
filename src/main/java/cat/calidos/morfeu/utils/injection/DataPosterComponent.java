// DATA POSTER COMPONENT . JAVA

package cat.calidos.morfeu.utils.injection;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.problems.PostingException;
import dagger.BindsInstance;
import dagger.producers.ProductionComponent;

/**
*	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@ProductionComponent(modules = {DataPosterModule.class, ListeningExecutorServiceModule.class})
public interface DataPosterComponent {


ListenableFuture<InputStream> postData() throws PostingException;


@ProductionComponent.Builder
interface Builder {

	@BindsInstance Builder forURI(URI u);
	@BindsInstance Builder withClient(CloseableHttpClient c);
	@BindsInstance Builder andData(Map<String, String> data);
	DataPosterComponent build();

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

