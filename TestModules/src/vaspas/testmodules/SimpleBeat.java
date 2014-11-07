package vaspas.testmodules;

import vaspas.performance.Beat;

public class SimpleBeat implements Beat {

	public void Impulse()
	{
		Runnable impulse=_impulse;
		
		if(impulse!=null)
			impulse.run();
	}
	
	private Runnable _impulse;
	
	public void connect(Runnable impulse)
	{
		_impulse=impulse;
	}
	
}
