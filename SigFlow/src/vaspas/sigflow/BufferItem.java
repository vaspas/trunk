package vaspas.sigflow;

public class BufferItem {

	public int getAvailable() {
		return Length - From;
	}

	public int From;
	public Object Data;
	public int Length;

}
