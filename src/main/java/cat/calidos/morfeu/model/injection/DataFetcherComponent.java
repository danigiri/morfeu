package cat.calidos.morfeu.model.injection;

import java.io.InputStream;
import java.net.URI;

import javax.inject.Named;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.BindsInstance;
import dagger.producers.ProductionComponent;


@ProductionComponent(modules = {DataFetcherModule.class, ListeningExecutorServiceModule.class})
public interface DataFetcherComponent {

ListenableFuture<InputStream> fetchData();

	
@ProductionComponent.Builder
interface Builder {
	@BindsInstance Builder forURI(URI u);
	@BindsInstance Builder withClient(CloseableHttpClient c);
	DataFetcherComponent build();
}

}