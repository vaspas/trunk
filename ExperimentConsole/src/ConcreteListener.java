import vaspas.messagedispatcher.*;
import vaspas.messagedispatcher.listeners.*;

@RegisterMessage(Listener=SyncMessageListener.class, Message=ConcreteMessage.class)
@RegisterMessage(Listener=SyncMessageListener.class, Message=ConcreteMessage2.class)
@RegisterMessage(Listener=QueueUiMessageListener.class, Message=ConcreteMessage3.class)
@RegisterMessage(Listener=QueueUiLastMessageListener.class, Message=ConcreteMessage3.class)
public class ConcreteListener implements SyncMessageListener,QueueUiLastMessageListener,QueueUiMessageListener{
	
	public <T> void SyncListen(T message, MessageObserver obs)
	{
		System.out.println("sync listen object "+message.getClass().toString());
	}
	
	public <T> void QueueUiLastListen(T message, MessageObserver obs)
	{
		System.out.println("sync listen UI object "+message.getClass().toString());
	}
	
	public <T> void QueueUiListen(T message, MessageObserver obs)
	{
		System.out.println("async listen UI object "+message.getClass().toString());
	}
}
