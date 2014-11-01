import java.util.concurrent.Executors;

import vaspas.messagedispatcher.MessagesDispatcher;



public class ExperimentConsole {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
			System.out.println("start");
			
			String a1="123";
			String a2="0123";
			String a3=a2.substring(1, 4);
						
			System.out.println(a1==a3);
			System.out.println(a1.equals(a3));
			
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
