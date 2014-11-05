package vaspas.sigflow;

public class BufferInt extends Buffer {

	@Override
	protected java.lang.Object CreateArray(int length) {

		return new int[length];
	}
}
