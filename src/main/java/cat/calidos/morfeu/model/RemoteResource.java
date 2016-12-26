package cat.calidos.morfeu.model;

import java.net.URI;

import org.apache.commons.lang.NullArgumentException;

public class RemoteResource implements Locatable {

protected Exception fetchException = null;
protected URI uri;

public URI getUri() {
	return uri;
}

public boolean couldBeFetched() {
	return fetchException==null;
}

public void couldNotFetchResource(Exception e) {
	if (e==null) {
		throw new NullArgumentException("Trying to assing a null fetch exception");
	}
	fetchException = e;
}

public Exception fetchException() {
	return fetchException;
}

}
