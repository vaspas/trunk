package vaspas.messagedispatcher;

import java.util.LinkedList;

public class CheckCompleteMessageObserver implements MessageObserver {

	public CheckCompleteMessageObserver()
    {
        _decreseAction = () -> 
        {
        	System.out.println("finishaction " + _myMessage);
            _counter=new java.util.concurrent.atomic.AtomicInteger(_counter).decrementAndGet();
            
            CheckComplete();
        };
    }

    private int _counter;

    private final Runnable _decreseAction;
    

    public Runnable RegisterAction(Object sender, String name)
    {
    	System.out.println("startaction "+_myMessage);
        
        _counter=new java.util.concurrent.atomic.AtomicInteger(_counter).incrementAndGet();

        return _decreseAction;
    }

    private final Object _sync=new Object();
    private boolean _completed;

    public void CheckComplete()
    {
    	synchronized (_sync)
        {
            if (_completed)
                return;
            
            if (_counter != 0)
                return;

            if (_internalObservers.size() != 0)
                return;
            
            OnComplete.run();
            _completed = true;

            System.out.println("complete " + _myMessage);
        }
    }

    private final LinkedList<MessageObserver> _internalObservers = new LinkedList<MessageObserver>();

    private String _myMessage="nullMessage";

    public MessageObserver CreateSubObserver(Object message, Object sender)
    {
    	CheckCompleteMessageObserver obs = new CheckCompleteMessageObserver();
    	obs._sender=sender;
    	obs._myMessage=message.toString();

    	System.out.println("create " + message);

    	synchronized (_sync)
    	{
            _internalObservers.add(obs);
    	}

        obs.OnComplete =
            () ->
                {
                	synchronized (_sync)
                	{
                        _internalObservers.remove(obs);
                	}
                    CheckComplete();
                };

        return obs;
    }

    public Runnable OnComplete;

    private Object _sender;
    public Object getSender()
    {
    	return _sender;
    }
	
}
