

import vaspas.performance.Performer;
import vaspas.sigflow.dataflow.*;
import vaspas.testmodules.*;



public class ExperimentConsole {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			System.out.println("start");
			
			/*String a1="123";
			String a2="0123";
			String a3=a2.substring(1, 4);
						
			System.out.println(a1==a3);
			System.out.println(a1.equals(a3));*/
			
			/*ConcreteListener l=new ConcreteListener();
			AsyncConcreteListener al=new AsyncConcreteListener(); 
						
			MessagesDispatcher md=new MessagesDispatcher();
			md.setSyncUiContext(new TestSynchonizationContext());
			
			md.Connect(l);
			md.Connect(al);
						
			md.Distribute(new ConcreteMessage(),new Object());
			md.Distribute(new ConcreteMessage2(),new Object());
			md.Distribute(new ConcreteMessage3(),new Object());*/
			
			//CyclicBarrier b=new CyclicBarrier(1);
			
			
			GenerateArrayIntModule m1=new GenerateArrayIntModule(); 
			m1.DataLength=10;
			WriteArrayIntToConsoleModule m2=new WriteArrayIntToConsoleModule();
			
			Block b=new Block();
			m1.Out=b;
			m2.In=b;
			
			SimpleBeat beat=new SimpleBeat();
			
			Performer p=new Performer();
			p.Beat=beat;
			p.AddModule(m1);
			p.AddModule(m2);
			
			p.Start();
			
			for(int i=0;i<100;i++)
			{
			beat.Impulse();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			}
			
			
			
			/*BufferInt bi=new BufferInt();
			
			bi.Write(new int[10], 10);
			bi.Write(new int[10], 10);
			
			System.out.println("available "+bi.getAvailable());
			System.out.println("next block "+bi.getNextBlockSize());
			
			bi.Take();			
			System.out.println("available "+bi.getAvailable());
			System.out.println("next block "+bi.getNextBlockSize());
			
			bi.Take();			
			System.out.println("available "+bi.getAvailable());
			System.out.println("next block "+bi.getNextBlockSize());*/
			
			//Thread.sleep(5000);
			
			System.out.println("finish");
	}

}
