package vaspas.sigflow.dataflow;

public class Block implements Channel {
	private Object _block;
	private int _length;

	public boolean ReadTo(Object data, int length) {

		if (getAvailable() != length)
			return false;

		System.arraycopy(_block, 0, data, 0, length);

		_block = null;

		return true;
	}

	public int getAvailable() {

		return _length;

	}

	public int getNextBlockSize() {

		return _length;

	}

	public Object Take() {

		Object t = _block;
		_block = null;
		_length = 0;
		return t;

	}

	public void Put(Object data) {
	}

	public void Write(Object data, int length) {

		_block = data;
		_length = length;

	}

	public void TrySkip(int size) {

		if (_block != null && _length <= size)
			_block = null;

	}
}
