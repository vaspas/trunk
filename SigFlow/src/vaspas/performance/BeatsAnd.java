package vaspas.performance;

import java.util.LinkedList;
import java.util.Optional;

public class BeatsAnd implements Beat {

	private final LinkedList<Item> _items=new LinkedList<Item>();

    class Item
    {
        public Beat Beat;
        public boolean IsActive;
        public Runnable Impulse;
    }

    private Runnable _impulse;
    public void connect(Runnable impulse)
    {
    	_impulse=impulse;
    }
    
    public Integer CountToImpulse;

    public void Add(Beat beat)
    {
    	
        if (!_items.stream().anyMatch(i->i.Beat==beat))
            return;

        Item item = new Item();
        item.Beat=beat;

        item.Impulse = () ->
                           {
                               item.IsActive = true;
                               Check();
                           };
        synchronized(_items){
            _items.add(item);}
        beat.connect(item.Impulse);
    }

    public boolean Remove(Beat beat)
    {
        Optional<Item> item = _items.stream().filter(i -> i.Beat == beat).findFirst();

        if(!item.isPresent())
            return false;

        item.get().Beat.connect(null);

        synchronized (_items){
            _items.remove(item.get());}

        return true;
    }
    
    private void Check()
    {
        synchronized (_items)
        {
            if (CountToImpulse == null && !_items.stream().allMatch(i -> i.IsActive))
                return;

            if (CountToImpulse != null && _items.stream().filter(i -> i.IsActive).count() < CountToImpulse)
                return;

            _items.forEach(i -> i.IsActive = false);
        }

        OnImpulse();
    }

    private void OnImpulse()
    {
    	Runnable r=_impulse;
    	if(r!=null)
    		r.run();
    }

    public boolean Contains(Beat beat)
    {
        return _items.stream().anyMatch(i -> i.Beat == beat);
    }

    public void Clear()
    {
    	synchronized(_items)
    	{
    		_items.forEach(i->i.Beat.connect(null));
    		_items.clear();
    	}
    }

    public int getCount()
    {
        return _items.size(); 
    }

	
}
