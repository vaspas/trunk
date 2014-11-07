package vaspas.sigflow.dataflow;

public interface SignalReaderDecorator extends SignalReader{

	void setInternalSignalReader(SignalReader reader);
	SignalReader getInternalSignalReader();
	
}
