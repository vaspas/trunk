package vaspas.testmodules;

import vaspas.sigflow.dataflow.*;
import vaspas.sigflow.module.*;

public class WriteArrayIntToConsoleModule implements ExecuteModule {
	
	public SignalReader In;
	
	public ExecuteResult Execute()
	{
		Object data=In.Take();
		if(data==null)
			return ExecuteResult.Workless;

		
		for(int i=0;i<((int[])data).length;i++)
			System.out.print(((int[])data)[i]+" ");
		System.out.println();
		
		return ExecuteResult.Worked;
	}
	
}
