package vaspas.sigflow.dataflow;

public interface SignalWriterDecorator extends SignalWriter{

	void setInternalSignalWriter(SignalWriter writer);
	SignalWriter getInternalSignalWriter();
	
}
