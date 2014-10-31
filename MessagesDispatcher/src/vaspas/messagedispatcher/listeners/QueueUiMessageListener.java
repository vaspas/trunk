package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface QueueUiMessageListener extends MessageListener
{
	<T> void QueueUiListen(T message, MessageObserver observer);
}

