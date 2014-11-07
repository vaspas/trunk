package vaspas.performance;

import java.util.LinkedList;

public class BeatsOr implements Beat {

	private final LinkedList<Beat> _beats = new LinkedList<Beat>();

	private Runnable _impulse;
    public void connect(Runnable impulse)
    {
    	_impulse=impulse;
    }

    public void Add(Beat beat)
    {
        if (_beats.contains(beat))
            return;

        beat.connect(()->OnImpulse());
        _beats.add(beat);
    }

    public boolean Remove(Beat beat)
    {
        if (!_beats.contains(beat))
            return false;

        beat.connect(null);

        _beats.remove(beat);

        return true;
    }

    private void OnImpulse()
    {
    	Runnable r=_impulse;
    	if(r!=null)
    		r.run();
    }
    

    public boolean Contains(Beat beat)
    {
        return _beats.contains(beat);
    }

    public void Clear()
    {
        _beats.forEach(b -> b.connect(null));

        _beats.clear();
    }

    public int size()
    {
        return _beats.size(); 
    }	
}

