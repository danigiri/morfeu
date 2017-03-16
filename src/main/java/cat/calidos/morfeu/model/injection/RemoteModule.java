package cat.calidos.morfeu.model.injection;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;

public class RemoteModule {

public RemoteModule() {
	super();
}

protected static ListenableFuture<InputStream> fetchRemoteStream(URI u, CloseableHttpClient c) {

	return DaggerHttpRequesterComponent.builder()
			.forURI(u)
			.withClient(c)
			.build()
			.fetchHttpData();
}

}
