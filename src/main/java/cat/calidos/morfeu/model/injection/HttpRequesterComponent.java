package cat.calidos.morfeu.model.injection;

import org.apache.http.HttpResponse;

import com.google.common.util.concurrent.ListenableFuture;

import cat.calidos.morfeu.injection.RequestScoped;
import dagger.producers.ProductionSubcomponent;

@RequestScoped
@ProductionSubcomponent(modules = HttpRequesterModule.class)
public interface HttpRequesterComponent {

ListenableFuture<HttpResponse> fetchHttpData();
	
@ProductionSubcomponent.Builder
interface Builder {
	Builder httpRequesterModule(HttpRequesterModule m);
	HttpRequesterComponent build();
}

}