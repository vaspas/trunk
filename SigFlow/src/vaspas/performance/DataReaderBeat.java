package vaspas.performance;

import vaspas.sigflow.dataflow.SignalReader;
import vaspas.sigflow.dataflow.SignalReaderDecorator;

public class DataReaderBeat implements SignalReaderDecorator, Beat 
{

	private SignalReader _internal;

    
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
    public Object Take()
    {
        OnImpulse();

        return _internal.Take();
    }


    @Override
    public void TrySkip(int size)
    {
        _internal.TrySkip(size);
    }

	@Override
	public boolean ReadTo(Object data, int length) {
		OnImpulse();

        return _internal.ReadTo(data,length);
	}

	@Override
	public int getAvailable() {
		int res = _internal.getAvailable();

        if (res==0)
            OnImpulse();

        return res;
	}

	@Override
	public int getNextBlockSize() {
		int res = _internal.getNextBlockSize();
        if (res==0)
            OnImpulse();
        return res;
	}

	@Override
	public void Put(Object data) {
		_internal.Put(data);
		
	}


	@Override
	public void setInternalSignalReader(SignalReader reader) {
		_internal=reader;
		
	}

	@Override
	public SignalReader getInternalSignalReader() {
		
		return _internal;
	}
	
}
