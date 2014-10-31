package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface QueueUiLastMessageListener extends MessageListener
{
	<T> void QueueUiLastListen(T message, MessageObserver observer);
}

