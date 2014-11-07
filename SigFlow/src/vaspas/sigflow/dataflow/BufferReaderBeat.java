package vaspas.sigflow.dataflow;

import vaspas.performance.Beat;

/// <summary>
/// Источник импульсов с контролем заполнения буфера.
/// </summary>
/// <typeparam name="T"></typeparam>
public class BufferReaderBeat implements SignalReaderDecorator, Beat {
	public BufferReaderBeat() {
		Load = 0.5f;
	}

	private SignalReader _internal;

	@Override
	public void setInternalSignalReader(SignalReader reader) {
		_internal = reader;
	}

	@Override
	public SignalReader getInternalSignalReader() {
		return _internal;
	}

	private Runnable _impulse;

	@Override
	public void connect(Runnable runnable) {
		_impulse = runnable;
	}

	private void OnImpulse() {
		Runnable r = _impulse;
		if (r != null)
			r.run();
	}

	public float Load;

	
	private void Balance() {
		if (!(_internal instanceof BufferState))
			return;

		BufferState bs = (BufferState) _internal;

		if (Load > ((float) bs.count() / bs.getMaxCapacity()))
			OnImpulse();
	}

	@Override
	public Object Take() {
		Balance();

		return _internal.Take();
	}

	@Override
	public void TrySkip(int size) {
		_internal.TrySkip(size);
	}

	@Override
	public boolean ReadTo(Object data, int length) {
		Balance();

		return _internal.ReadTo(data, length);
	}

	@Override
	public int getAvailable() {
		Balance();

		return _internal.getAvailable();
	}

	@Override
	public int getNextBlockSize() {
		return _internal.getNextBlockSize();
	}

	@Override
	public void Put(Object data) {
		_internal.Put(data);

	}

}
