package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface QueueLastMessageListener extends MessageListener
{
	<T> void QueueLastListen(T message, MessageObserver observer);
}

