package vaspas.sigflow.dataflow;

public interface SignalWriterDecorator extends SignalWriter{

	void setInternalSignalReader(SignalWriter reader);
	SignalWriter getInternalSignalWriter();
	
}
