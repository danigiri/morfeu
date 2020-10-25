// CONTROL COMPONENT . JAVA

package cat.calidos.morfeu.webapp.injection;

import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.servlet.ServletContext;

import dagger.BindsInstance;
import dagger.Component;

import cat.calidos.morfeu.control.injection.PingControlModule;

/** Component that aggregates all controlers modules, it is passed the path, method and a map of parameters  
* @author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = {ControlModule.class, PingControlModule.class})
public interface ControlComponent {

public static final String GET = "GET";
public static final String POST = "POST";
public static final String TEXT = "text/plain";
public static final String JSON = "application/json";
public static final String SVG = "image/svg+xml";

/**  @return return the content of the processed request as a string */
@Named("Content") String process();


/** @return return the content type that is associated with this request */
@Named("Content-Type") String contentType();


/** @return return true if we match the path with any of the registered controller modules*/
boolean matches();

@Component.Builder
interface Builder {

	@BindsInstance Builder withPath(@Named("Path") String path);
	@BindsInstance Builder method(@Named("Method") String method);
	@BindsInstance Builder withParams(@Named("Params") Map<String, String> params);
	@BindsInstance Builder andContext(@Nullable ServletContext context);
	@BindsInstance Builder encoding(@Named("encoding") @Nullable String encoding);

	ControlComponent build();

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

