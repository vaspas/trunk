package vaspas.messagedispatcher;

public interface SynchronizationContext {
void Send(Runnable action);
void Post(Runnable action);
}
