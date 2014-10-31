import java.util.concurrent.Executors;

import vaspas.messagedispatcher.MessagesDispatcher;



public class ExperimentConsole {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
			System.out.println("start");
			
			
			ConcreteListener l=new ConcreteListener();
			AsyncConcreteListener al=new AsyncConcreteListener(); 
			
			//System.out.println(l.getClass()==ConcreteListener.class);
			
			MessagesDispatcher md=new MessagesDispatcher();
			
			md.Connect(l);
			md.Connect(al);
						
			md.Distribute(new ConcreteMessage());
			md.Distribute(new ConcreteMessage2());
			md.Distribute(new ConcreteMessage3());
			
			Thread.sleep(5000);
			
			System.out.println("finish");
	}

}
