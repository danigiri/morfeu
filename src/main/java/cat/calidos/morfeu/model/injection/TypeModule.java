// TYPE MODULE . JAVA

package cat.calidos.morfeu.model.injection;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;

import dagger.Module;
import dagger.Provides;

import cat.calidos.morfeu.model.Metadata;
import cat.calidos.morfeu.model.Type;
import cat.calidos.morfeu.model.metadata.injection.DaggerModelMetadataComponent;

/** Module to create the Type instance from the XML Schema XSType
* 	@author daniel giribet
*///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class TypeModule {

protected final static Logger log = LoggerFactory.getLogger(TypeModule.class);


@Provides
public static Type type(@Named("EffectiveURI") URI uri,
						String defaultName,
						@Nullable XSType xsType,
						@Nullable @Named("regex") String regex,
						@Named("possibleValues") Set<String> possibleValues,
						Metadata metadata) {

	// if it's a local type we use the cell model name
	String name = (xsType.isLocal()) ? defaultName : xsType.getName();
	boolean global = xsType.isGlobal();

	return new Type(uri, name, xsType, regex, possibleValues, global, metadata);

}


@Provides @Named("Empty")
public static Type emptyType(@Named("EffectiveURI") URI uri, String defaultName) {
	return new Type(uri, defaultName);
}


@Provides @Named("EffectiveURI")
URI uri(@Nullable XSType xsType, @Nullable URI uri) {


	URI u = uri;	// if not overwritten by our caller (when uri!=null, we try to extract it from the typee
	if (u==null) {
		try {
			Locator locator = xsType.getLocator();
			u = new URI(locator.getSystemId());
		} catch (URISyntaxException e) {
			log.error("What the heck, locator-based URI of element '{}' is not valid ", xsType.getName());
		}
	}

	return u;

}


@Provides @Nullable @Named("regex")
public static String regex(@Nullable XSType xsType) {

	String regex = null;

	if (xsType!=null) {
		XSSimpleType xsSimpleType = xsType.asSimpleType();
		if (xsSimpleType!=null && xsSimpleType.isRestriction()) {
			XSFacet pattern = xsSimpleType.asRestriction().getDeclaredFacet("pattern");
				if (pattern!=null) {
				regex = pattern.getValue().value;
				}
		}
	}

	return regex;

}


@Provides @Named("possibleValues")
public static Set<String> possibleValues(@Nullable XSType xsType) {

	Set<String> values = new HashSet<String>();

	if (xsType!=null) {
		XSSimpleType xsSimpleType = xsType.asSimpleType();
		if (xsSimpleType!=null && xsSimpleType.isRestriction() && xsSimpleType.getFacet("enumeration")!=null) {
			xsSimpleType.asRestriction().getFacets("enumeration").forEach(f -> values.add(f.getValue().value));
		}
	}

	return values;

}


@Provides
public static Metadata metadata(@Nullable XSType xsType, @Named("EffectiveURI") URI uri) {
	return DaggerModelMetadataComponent.builder().from(xsType.getAnnotation()).withParentURI(uri).build().value();
}

// STASHED REFERENCE
// get the types from a complex type
//XSComplexType complexType = xsType.asComplexType();
//XSTerm t = complexType.getContentType().asParticle().getTerm();
//while (t.isModelGroup()) {
//	t = t.asModelGroup().getChild(0).getTerm();
//}
//XSElementDecl asElementDecl = t.asElementDecl();
//
// get the attributes
//Collection<? extends XSAttributeUse> attributeUses = complexType.getAttributeUses();

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
