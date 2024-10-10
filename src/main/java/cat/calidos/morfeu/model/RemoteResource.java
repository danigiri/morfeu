package cat.calidos.morfeu.model;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RemoteResource implements Locatable {

protected Exception fetchException = null;
// @JsonDeserialize(using=URIDeserializer.class)
protected URI		uri;
protected String	name;
protected String	desc;

RemoteResource(URI u) {
	this.uri = u;
}


RemoteResource(	URI u,
				String name,
				String desc) {

	this(u);

	this.name = name;
	this.desc = desc;

}


public URI getURI() {
	return uri;
}


public String getName() {
	return name;
}


public String getDesc() {
	return desc;
}


@JsonProperty("name")
public void setName(String name) {
	this.name = name;
}


@JsonProperty("desc")
public void setDesc(String desc) {
	this.desc = desc;
}

}
