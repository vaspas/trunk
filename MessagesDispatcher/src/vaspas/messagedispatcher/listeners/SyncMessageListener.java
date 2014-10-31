package vaspas.messagedispatcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface SyncMessageListener extends MessageListener
{
	<T> void SyncListen(T message, MessageObserver observer);
}

