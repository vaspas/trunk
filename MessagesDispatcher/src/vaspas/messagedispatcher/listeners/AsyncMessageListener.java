package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface AsyncMessageListener extends MessageListener
{
	<T> void AsyncListen(T message, MessageObserver observer);
}

