package vaspas.messagedispatcher;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.*;

import vaspas.messagedispatcher.listeners.*;

public class MessagesDispatcher {

	private final ConcurrentLinkedQueue<WeakReference<MessageListener>> _listeners = new ConcurrentLinkedQueue<WeakReference<MessageListener>>();

	private final ExecutorService _threadpool = Executors.newCachedThreadPool();

	private final FakeMessageObserver _fakeMessageObserver = new FakeMessageObserver();

	public void Connect(MessageListener listener) {
		_listeners.add(new WeakReference<MessageListener>(listener));
	}

	private <T extends MessageListener> Iterator<T> GetListenersFor(
			Class<T> listenerType, Object message) {

		_listeners.removeIf(wr -> wr.get() == null);

		Iterator<WeakReference<MessageListener>> iter = _listeners.iterator();

		return new Iterator<T>() {
			T _next;

			public boolean hasNext() {
				_next = null;

				while (iter.hasNext()) {

					MessageListener ml = iter.next().get();

					if (ml == null || !listenerType.isInstance(ml))
						continue;

					if (CheckMessageListener(ml, message)) {
						_next = listenerType.cast(ml);
						break;
					}

					continue;
				}

				return _next != null;
			}

			public T next() {
				return _next;
			}
		};
	}

	private boolean CheckMessageListener(MessageListener listener,
			Object message) {
		Class<?> lc = listener.getClass();

		if (listener.getClass().isAnnotationPresent(ListenerMessageTypes.class)) {
			for (ListenerMessageType lmt : lc.getAnnotation(
					ListenerMessageTypes.class).value()) {
				if (lmt.ListenerType().isInstance(listener)
						&& lmt.MessageType().isInstance(message))
					return true;
			}
		}

		if (lc.isAnnotationPresent(ListenerMessageType.class)) {
			ListenerMessageType lmt = lc
					.getAnnotation(ListenerMessageType.class);
			if (lmt.ListenerType().isInstance(listener)
					&& lmt.MessageType().isInstance(message))
				return true;
		}

		return false;

	}

	public void Distribute(Object message)
	{
		Distribute(message, false, _fakeMessageObserver);
	}
	
	private void Distribute(Object message, boolean exclude,
			MessageObserver observer) {

		// асинхронные команды
		for (Iterator<AsyncMessageListener> i = GetListenersFor(
				AsyncMessageListener.class, message); i.hasNext();) {
			final AsyncMessageListener l = i.next();

			if (exclude && l == observer.getSender())
				continue;

			Runnable onComplete = observer.RegisterAction(l, "ListenAsync");

			_threadpool.submit(() -> {
				l.AsyncListen(message, observer);
				onComplete.run();
			});
		}

		// синхронные команды
		for (Iterator<SyncMessageListener> i = GetListenersFor(
				SyncMessageListener.class, message); i.hasNext();) {
			SyncMessageListener l = i.next();

			if (exclude && l == observer.getSender())
				continue;
			l.SyncListen(message, observer);
		}
	}
}
