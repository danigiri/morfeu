package cat.calidos.morfeu.utils.injection;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.utils.injection.DaggerDataFetcherComponent;
import cat.calidos.morfeu.problems.FetchingException;

public class RemoteModule {

public RemoteModule() {
	super();
}

protected static ListenableFuture<InputStream> fetchRemoteStream(URI u, CloseableHttpClient c) throws FetchingException {

	return DaggerDataFetcherComponent.builder()
			.forURI(u)
			.withClient(c)
			.build()
			.fetchData();

}

}
