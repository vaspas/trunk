import vaspas.messagedispatcher.*;
import vaspas.messagedispatcher.listeners.*;

@RegisterMessage(Listener=AsyncMessageListener.class, Message=ConcreteMessage.class)
@RegisterMessage(Listener=AsyncMessageListener.class, Message=ConcreteMessage2.class)
public class AsyncConcreteListener implements AsyncMessageListener{
	
	public <T> void AsyncListen(T message, MessageObserver obs)
	{
		System.out.println("async listen object "+message.getClass().toString());
	}
}
