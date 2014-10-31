import vaspas.messagedispatcher.SynchronizationContext;


public class TestSynchonizationContext implements SynchronizationContext {
public void Send(Runnable action)
{
	System.out.println("SynchronizationContext send");
	action.run();
}
public void Post(Runnable action)
{
	System.out.println("SynchronizationContext post");
	action.run();
}
}
