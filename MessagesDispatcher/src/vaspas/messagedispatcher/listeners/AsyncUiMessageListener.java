package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface AsyncUiMessageListener extends MessageListener
{
	<T> void AsyncUiListen(T message, MessageObserver observer);
}

