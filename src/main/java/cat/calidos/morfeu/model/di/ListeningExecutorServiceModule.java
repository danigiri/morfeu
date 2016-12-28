package cat.calidos.morfeu.model.di;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import dagger.Module;
import dagger.Provides;
import dagger.producers.Production;

@Module
public final class ListeningExecutorServiceModule {
	
	@Provides
	@Production
	public static ListeningExecutorService executor() {
		// Following the Dagger 2 official docs, this private executor pool should be OK for just the HTTP stuff
		return MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
	}
	
}