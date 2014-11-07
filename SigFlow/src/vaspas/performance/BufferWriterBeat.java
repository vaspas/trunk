package vaspas.performance;

import vaspas.sigflow.dataflow.BufferState;
import vaspas.sigflow.dataflow.SignalWriter;
import vaspas.sigflow.dataflow.SignalWriterDecorator;

public class BufferWriterBeat implements SignalWriterDecorator, Beat {

	public BufferWriterBeat()
    {
        Load = 0.5f;
    }

    private SignalWriter _internal;


    public float Load;

    private void OnImpulse() {
		Runnable r = _impulse;
		if (r != null)
			r.run();
	}
    
    @Override
    public void Write(Object data, int length)
    {
        _internal.Write(data,length);

        if(!(_internal instanceof BufferState))
        	return;
        
        BufferState bs = (BufferState)_internal;

        if(Load>((float)bs.count()/bs.getMaxCapacity()))
            OnImpulse();
    }

    private Runnable _impulse;
	@Override
	public void connect(Runnable runnable) {
		_impulse=runnable;
		
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
