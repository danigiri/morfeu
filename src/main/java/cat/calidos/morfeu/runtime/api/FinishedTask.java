package cat.calidos.morfeu.runtime.api;

public interface FinishedTask extends Task {

default public boolean failed() {
	return !isOK();
}


public void waitFor() throws InterruptedException;

public int result() throws InterruptedException;

}
