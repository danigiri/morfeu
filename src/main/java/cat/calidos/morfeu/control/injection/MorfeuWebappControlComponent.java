// MORFEU CONTROL COMPONENT . JAVA

package cat.calidos.morfeu.control.injection;

import cat.calidos.morfeu.webapp.injection.WebappControlComponent;
import cat.calidos.morfeu.webapp.injection.WebappControlModule;
import dagger.Component;


/**
 * This is where we register al the webapp controllers that will handle our requests
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = { WebappControlModule.class, PingControlModule.class, DocumentControlModule.class,
		ContentControlModule.class, ModelsControlModule.class, PreviewControlModule.class,
		SnippetsControlModule.class, ClientEventControlModule.class })
public interface MorfeuWebappControlComponent extends WebappControlComponent {

//@formatter:off
@Component.Builder
interface Builder extends WebappControlComponent.Builder {}
//@formatter.on

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
