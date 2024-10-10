package cat.calidos.morfeu.utils.injection;

import java.util.Properties;

import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


/**
 * Generate an empty configuration or a configuration from the input properties
 * 
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ConfigurationModule {

@Provides @Named("Configuration")
public static Properties configuration(@Nullable @Named("InputProperties") Properties p) {
	return p == null ? new Properties() : p;
}

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
