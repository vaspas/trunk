package vaspas.messagedisptcher.listeners;

import vaspas.messagedispatcher.MessageObserver;


public interface MessageListener<T>
{
    void Listen(T message, MessageObserver observer);
}
