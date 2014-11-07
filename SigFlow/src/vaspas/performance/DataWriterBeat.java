package vaspas.performance;

import vaspas.sigflow.dataflow.SignalWriter;
import vaspas.sigflow.dataflow.SignalWriterDecorator;

public class DataWriterBeat implements SignalWriterDecorator, Beat 
{

	private SignalWriter _internal;
	
	private void OnImpulse() {
		Runnable r = _impulse;
		if (r != null)
			r.run();
	}
    
    private Runnable _impulse;
   	@Override
   	public void connect(Runnable runnable) {
   		_impulse=runnable;
   		
   	}

     
@Override
    public void Write(Object data, int length)
    {
        _internal.Write(data,length);

        OnImpulse();
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
