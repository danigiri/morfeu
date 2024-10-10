package cat.calidos.morfeu.runtime;

import java.util.function.Function;

import org.zeroturnaround.exec.stream.LogOutputStream;


public class ExecOutputProcessor extends LogOutputStream {

private Function<String, Integer>	matcher;
private ExecOutputProcessor			indirectProcessor;

public ExecOutputProcessor() {
}


public ExecOutputProcessor(Function<String, Integer> matcher) {
	this.matcher = matcher;
}


public ExecOutputProcessor(ExecOutputProcessor indirectProcessor) {
	this.indirectProcessor = indirectProcessor;
}


public void setIndirectProcessor(ExecOutputProcessor indirectProcessor) {
	this.indirectProcessor = indirectProcessor;
}


/**
 * @return given a log line, it returns how much is remaining in the current state, or negative if
 *         there is an error or some kind of problem. The remaining time is undefined for long
 *         running tasks. Values are from 100 to 1 for percentage, 0 for stage completed and
 *         negative for errors
 */
public int process(String line) {
	return (indirectProcessor == null) ? matcher.apply(line) : indirectProcessor.process(line);
}


@Override
protected void processLine(String line) {
	if (indirectProcessor == null) {
		process(line);
	} else {
		indirectProcessor.processLine(line);
	}
}

}
