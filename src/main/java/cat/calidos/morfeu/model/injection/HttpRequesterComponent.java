package cat.calidos.morfeu.model.injection;

import java.io.InputStream;

import org.apache.http.HttpResponse;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.injection.RequestScoped;
import dagger.producers.ProductionSubcomponent;

@RequestScoped
@ProductionSubcomponent(modules = HttpRequesterModule.class)
public interface HttpRequesterComponent {

ListenableFuture<InputStream> fetchHttpData();
	
@ProductionSubcomponent.Builder
interface Builder {
	Builder httpRequesterModule(HttpRequesterModule m);
	HttpRequesterComponent build();
}

}