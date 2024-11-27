package cat.calidos.morfeu.utils.injection;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Component;


/**
 * Read a configuration propert value in the following reverse priority order 1) from
 * properties(optional) 2) from sysenv 3) from env vars 4) from a map (optional) 5) from args array
 * (optional) name=value or --name value) Each step overrides the previous ones
 * 
 * This means we can have configuration properties that can be progressively overriden by '-D' or
 * env vars or CLI flags
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Component(modules = { ConfigurationModule.class, ConfigPropertyModule.class })
public interface ConfigPropertyComponent {

Optional<Object> value();

Optional<Integer> integerValue();

Optional<String> stringValue();

//@formatter:off
@Component.Builder
interface Builder {
	@BindsInstance Builder forName(@Named("PropertyName") String name);
	@BindsInstance Builder withProps(@Nullable @Named("InputProperties") Properties p);
	@BindsInstance Builder withMap(@Nullable Map<String,Object> map);
	@BindsInstance Builder withArray(@Nullable String args[]);
	@BindsInstance Builder allowEmpty(@Nullable Boolean allowEmpty);
	@BindsInstance Builder andDefault(@Nullable @Named("DefaultValue") Object defaultValue);

	ConfigPropertyComponent build();
}
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
