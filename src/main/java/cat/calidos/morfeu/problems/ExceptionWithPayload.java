package cat.calidos.morfeu.problems;

import java.util.Optional;

public interface ExceptionWithPayload {

public void setPayload(String payload);

public Optional<String> getPayload();

}
