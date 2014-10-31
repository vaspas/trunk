package vaspas.messagedispatcher;

public class MessageQueueItem {
	public Object Message;
    public boolean ExcludeSender;
    public MessageObserver Observer;
    public Runnable OnComplete;
}
