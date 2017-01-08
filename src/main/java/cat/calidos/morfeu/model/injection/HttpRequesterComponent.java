package cat.calidos.morfeu.model.injection;

import java.io.InputStream;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.common.util.concurrent.ListenableFuture;

import dagger.producers.ProductionComponent;

@ProductionComponent(modules = {HttpRequesterModule.class, HttpClientModule.class, ListeningExecutorServiceModule.class})
public interface HttpRequesterComponent {

ListenableFuture<InputStream> fetchHttpData();
	
@ProductionComponent.Builder
interface Builder {
	Builder httpRequesterModule(HttpRequesterModule m);
	Builder httpClientModule(HttpClientModule m);
	HttpRequesterComponent build();
}

}