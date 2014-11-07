package vaspas.testmodules;

import vaspas.sigflow.dataflow.SignalWriter;
import vaspas.sigflow.module.*;

public class GenerateArrayIntModule implements ExecuteModule {

	public int DataLength;
	
	public SignalWriter Out;
	
	public ExecuteResult Execute()
	{
		int[] data=new int[DataLength];
		
		for(int i=0;i<DataLength;i++)
		{
			data[i]=i;
		}	
		
		Out.Write(data, DataLength);
		
		
		return ExecuteResult.Independent;
	}
	
}
