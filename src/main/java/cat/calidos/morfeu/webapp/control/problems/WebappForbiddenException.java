package cat.calidos.morfeu.webapp.control.problems;

public class WebappForbiddenException extends WebappRuntimeException {

public WebappForbiddenException(String message, String payload, Exception e) {
	super(message, payload, e);
}

}
