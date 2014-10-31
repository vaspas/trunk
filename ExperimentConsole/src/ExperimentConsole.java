import java.util.concurrent.Executors;

import vaspas.messagedispatcher.MessagesDispatcher;



public class ExperimentConsole {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
			System.out.println("start");
			
			
			ConcreteListener l=new ConcreteListener();
			AsyncConcreteListener al=new AsyncConcreteListener(); 
						
			MessagesDispatcher md=new MessagesDispatcher();
			md.setSyncUiContext(new TestSynchonizationContext());
			
			md.Connect(l);
			md.Connect(al);
						
			md.Distribute(new ConcreteMessage(),new Object());
			md.Distribute(new ConcreteMessage2(),new Object());
			md.Distribute(new ConcreteMessage3(),new Object());
			
			Thread.sleep(5000);
			
			System.out.println("finish");
	}

}
