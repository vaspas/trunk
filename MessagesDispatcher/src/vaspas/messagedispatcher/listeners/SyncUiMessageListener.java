package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface SyncUiMessageListener extends MessageListener
{
	<T> void SyncUiListen(T message, MessageObserver observer);
}

