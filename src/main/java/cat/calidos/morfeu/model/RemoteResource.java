package cat.calidos.morfeu.model;

import java.net.URI;

import org.apache.commons.lang.NullArgumentException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RemoteResource implements Locatable {

protected Exception fetchException = null;
//@JsonDeserialize(using=URIDeserializer.class)
protected URI uri;
protected String name;

RemoteResource(URI u) {
	this.uri = u;
}
RemoteResource(URI u, String name) {
	this(u);
	this.name = name;
}


public URI getUri() {
	return uri;
}


public String getName() {
	return name;
}

}
