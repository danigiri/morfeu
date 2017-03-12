package cat.calidos.morfeu.model;

import java.net.URI;

import org.apache.commons.lang.NullArgumentException;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RemoteResource implements Locatable {

protected Exception fetchException = null;
//@JsonDeserialize(using=URIDeserializer.class)
protected URI uri;

RemoteResource(URI u) {
	this.uri = u;
}


public URI getUri() {
	return uri;
}

@Deprecated
public boolean couldBeFetched() {
	return fetchException==null;
}


@Deprecated
public void couldNotBeFetchedDueTo(Exception e) {
	if (e==null) {
		throw new NullArgumentException("Trying to assing a null fetch exception");
	}
	fetchException = e;
}


@Deprecated
public Exception fetchException() {
	return fetchException;
}

}
