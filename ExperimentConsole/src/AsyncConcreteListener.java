import vaspas.messagedispatcher.*;
import vaspas.messagedispatcher.listeners.*;

@ListenerMessageType(ListenerType=AsyncMessageListener.class, MessageType=ConcreteMessage.class)
@ListenerMessageType(ListenerType=AsyncMessageListener.class, MessageType=ConcreteMessage2.class)
public class AsyncConcreteListener implements AsyncMessageListener{
	
	public <T> void AsyncListen(T message, MessageObserver obs)
	{
		System.out.println("async listen object "+message.getClass().toString());
	}
}
