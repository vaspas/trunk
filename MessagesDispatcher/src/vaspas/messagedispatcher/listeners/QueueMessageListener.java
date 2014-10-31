package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface QueueMessageListener extends MessageListener
{
	<T> void QueueListen(T message, MessageObserver observer);
}

