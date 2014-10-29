package vaspas.messagedispatcher;

class FakeMessageObserver implements MessageObserver
{
    private final Runnable _fakeAction = ()->{};

    public Runnable RegisterAction(Object sender, String name)
    {
        return _fakeAction;
    }

    public MessageObserver CreateSubObserver(Object message, Object sender)
    {
    	FakeMessageObserver obs=new FakeMessageObserver();
    	obs._sender=sender;
        return obs;
    }

    private Object _sender;
    public Object getSender()
    {
    	return _sender;
    }
}
