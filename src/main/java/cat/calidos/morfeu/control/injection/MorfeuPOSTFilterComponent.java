package cat.calidos.morfeu.control.injection;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {MorfeuPOSTFilterModule.class})
public interface MorfeuPOSTFilterComponent {

boolean handle();

@Component.Builder
interface Builder {

	@BindsInstance Builder request(ServletRequest request);
	@BindsInstance Builder response(ServletResponse response);

	MorfeuPOSTFilterComponent build();

}

}