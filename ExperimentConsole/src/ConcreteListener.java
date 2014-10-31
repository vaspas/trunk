import vaspas.messagedispatcher.*;
import vaspas.messagedispatcher.listeners.SyncMessageListener;

@ListenerMessageType(ListenerType=SyncMessageListener.class, MessageType=ConcreteMessage.class)
@ListenerMessageType(ListenerType=SyncMessageListener.class, MessageType=ConcreteMessage2.class)
public class ConcreteListener implements SyncMessageListener{
	
	public <T> void SyncListen(T message, MessageObserver obs)
	{
		System.out.println("listen object "+message.getClass().toString());
	}
}
