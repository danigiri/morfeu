package cat.calidos.morfeu.utils.injection;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class ConfigPropertyModule {

protected final static Logger log = LoggerFactory.getLogger(ConfigPropertyModule.class);

private static final char VARIABLE_DELIMITER = '=';

@Provides
public static Optional<Object> value(@Named("EffectiveValue") Optional<Object> value) {
	return value;
}


@Provides @Named("EffectiveValue")
public static Optional<Object> effectiveValue(	@Named("PropertyName") String name,
												@Named("Configuration") Properties p,
												@Nullable @Named("SystemValue") String systemValue,
												@Nullable @Named("EnvValue") String envValue,
												@Nullable Map<String, Object> map,
												@Nullable @Named("ArrayValue") String argsValue,
												@Nullable Boolean allowEmpty,
												@Nullable @Named("DefaultValue") Object defaultValue) {

	var message = new StringBuffer();
	message.append("Property " + name + "' in [");

	Object value = p.get(name);
	message.append(value != null ? "properties," : "");

	value = systemValue != null ? systemValue : value;
	message.append(systemValue != null ? "system," : "");

	value = envValue != null ? envValue : value;
	message.append(envValue != null ? "env," : "");

	value = map != null && map.containsKey(name) ? map.get(name) : value;
	message.append(map != null && map.containsKey(name) ? "map," : "");

	value = argsValue != null ? argsValue : value;
	message.append(argsValue != null ? "args," : "");

	allowEmpty = allowEmpty == null ? true : allowEmpty;
	value = value == null
			|| ((value instanceof String) && ((String) value).isEmpty() && !allowEmpty)
					? defaultValue : value;
	message.append(value == null ? "default" : "");

	message.append("], final='" + value + "'");
	log.trace(message.toString());

	return Optional.ofNullable(value);
}


@Provides
public static Optional<Integer> integerValue(@Named("EffectiveValue") Optional<Object> value) {
	if (value.isPresent()) {
		Object data = value.get();
		if (data instanceof Integer) {
			return Optional.of((Integer) data);
		} else if (data instanceof String) {
			return Optional.of(Integer.parseInt((String) data));
		} else {
			throw new ClassCastException("Cannot get value as Integer");
		}
	} else {
		return Optional.empty();
	}
}


@Provides
Optional<String> stringValue(@Named("EffectiveValue") Optional<Object> value) {
	if (value.isPresent()) {
		Object data = value.get();
		if (data instanceof String) {
			return Optional.of((String) data);
		} else {
			throw new ClassCastException("Cannot get value as String");
		}
	} else {
		return Optional.empty();
	}
}


@Provides @Nullable @Named("SystemValue")
public static String systemValue(@Named("PropertyName") String name) {
	return System.getProperty(name);
}


@Provides @Nullable @Named("EnvValue")
public static String envValue(@Named("PropertyName") String name) {
	return System.getenv(name);
}


@Provides @Nullable @Named("ArrayValue")
public static String arrayValue(@Named("PropertyName") String name,
								@Nullable String args[]) {
	if (args == null) {
		return null;
	}
	String value = null;
	var i = 0;
	int nameLength = name.length();
	while (i < args.length && value == null) {
		String current = args[i];
		if (current.equals("--" + name)) {
			if (i + 1 < args.length) {
				value = args[i + 1];
			}
		} else {
			if (current.length() > nameLength && current.substring(0, nameLength).equals(name)
					&& current.charAt(nameLength) == VARIABLE_DELIMITER) {
				value = current.substring(nameLength + 1);
			}
		}
		i++;
	}
	return value;
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
