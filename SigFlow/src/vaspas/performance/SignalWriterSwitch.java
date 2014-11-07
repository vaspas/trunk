package vaspas.performance;

import vaspas.sigflow.dataflow.SignalWriter;
import vaspas.sigflow.dataflow.SignalWriterDecorator;

public class SignalWriterSwitch implements SignalWriterDecorator {

	private SignalWriter _internal;

    public boolean Enable;


	@Override
	public void Write(Object data, int length) {
		if(Enable)
            _internal.Write(data,length);		
	}

	@Override
	public void setInternalSignalWriter(SignalWriter writer) {
		_internal=writer;
		
	}

	@Override
	public SignalWriter getInternalSignalWriter() {
		
		return _internal;
	}
	
}
