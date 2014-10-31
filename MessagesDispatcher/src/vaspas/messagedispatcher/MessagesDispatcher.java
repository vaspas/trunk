package vaspas.messagedispatcher;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.*;

import vaspas.messagedispatcher.listeners.*;

public class MessagesDispatcher {

	private final ConcurrentLinkedQueue<WeakReference<MessageListener>> _listeners = new ConcurrentLinkedQueue<WeakReference<MessageListener>>();

	private SynchronizationContext _syncUiContext;

	public void setSyncUiContext(SynchronizationContext sc) {
		_syncUiContext = sc;
	}

	public SynchronizationContext getSyncUiContext() {
		return _syncUiContext;
	}

	private final ExecutorService _threadPool = Executors.newCachedThreadPool();

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

					if (CheckMessageListener(listenerType, ml.getClass(),
							message)) {
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

	private boolean CheckMessageListener(Class<?> requeredListener,
			Class<?> listener, Object message) {

		if (listener.isAnnotationPresent(RegisterMessages.class)) {
			for (RegisterMessage lmt : listener.getAnnotation(
					RegisterMessages.class).value()) {
				if (lmt.Listener() == requeredListener
						&& lmt.Message().isInstance(message))
					return true;
			}
			return false;
		}

		if (listener.isAnnotationPresent(RegisterMessage.class)) {
			RegisterMessage lmt = listener.getAnnotation(RegisterMessage.class);
			if (lmt.Listener() == requeredListener
					&& lmt.Message().isInstance(message))
				return true;
			return false;
		}

		return true;
	}

	public void DistributeAsync(Object message, Object sender) {
		DistributeAsync(message, sender, null);
	}

	public void Distribute(Object message, Object sender) {
		Distribute(message, sender, null);
	}

	public void DistributeAsyncExcludeSender(Object message, Object sender) {
		DistributeAsyncExcludeSender(message, sender, null);
	}

	public void DistributeExcludeSender(Object message, Object sender) {
		DistributeExcludeSender(message, sender, null);
	}

	public void DistributeAsync(Object message, Object sender,
			MessageObserver observer) {
		MessageObserver obs = (observer != null) ? observer
				: _fakeMessageObserver;

		Runnable onComplete = obs.RegisterAction(this, "DistributeAsync<T>");

		_threadPool.execute(() -> {
			DistributeExcludeSender(message, sender, obs);
			onComplete.run();
		});
	}

	public void Distribute(Object message, Object sender,
			MessageObserver observer) {
		MessageObserver obs = (observer != null) ? observer
				: _fakeMessageObserver;

		Distribute(message, false, obs.CreateSubObserver(message, sender));
	}

	public void DistributeAsyncExcludeSender(Object message, Object sender,
			MessageObserver observer) {
		MessageObserver obs = (observer != null) ? observer
				: _fakeMessageObserver;

		Runnable onComplete = obs.RegisterAction(this,
				"DistributeAsyncExcludeSender<T>");

		_threadPool.execute(() -> {
			DistributeExcludeSender(message, sender, obs);
			onComplete.run();
		});
	}

	public void DistributeExcludeSender(Object message, Object sender,
			MessageObserver observer) {
		MessageObserver obs = (observer != null) ? observer
				: _fakeMessageObserver;

		Distribute(message, true, obs.CreateSubObserver(message, sender));
	}

	private void Distribute(Object message, boolean exclude,
			MessageObserver observer) {

		// send message asynchronously
		for (Iterator<AsyncMessageListener> i = GetListenersFor(
				AsyncMessageListener.class, message); i.hasNext();) {

			final AsyncMessageListener l = i.next();

			if (exclude && l == observer.getSender())
				continue;

			Runnable onComplete = observer.RegisterAction(l, "ListenAsync");

			_threadPool.execute(() -> {
				l.AsyncListen(message, observer);
				onComplete.run();
			});
		}

		// send UI message asynchronously
		for (Iterator<AsyncUiMessageListener> i = GetListenersFor(
				AsyncUiMessageListener.class, message); i.hasNext();) {

			final AsyncUiMessageListener l = i.next();

			if (exclude && l == observer.getSender())
				continue;

			Runnable onComplete = observer.RegisterAction(l, "ListenAsyncUi");

			_syncUiContext.Post(() -> {
				l.AsyncUiListen(message, observer);
				onComplete.run();
			});
		}

		DistributeQueue(message, exclude, observer);

		// send message synchronously
		for (Iterator<SyncMessageListener> i = GetListenersFor(
				SyncMessageListener.class, message); i.hasNext();) {

			SyncMessageListener l = i.next();

			if (exclude && l == observer.getSender())
				continue;

			l.SyncListen(message, observer);
		}

		// send UI message synchronously
		for (Iterator<SyncUiMessageListener> i = GetListenersFor(
				SyncUiMessageListener.class, message); i.hasNext();) {

			SyncUiMessageListener l = i.next();

			if (exclude && l == observer.getSender())
				continue;

			_syncUiContext.Send(() -> {
				l.SyncUiListen(message, observer);
			});
		}
	}

	private final LinkedList<MessagesQueue> _queues = new LinkedList<MessagesQueue>();

	private void DistributeQueue(Object message, boolean exclude,
			MessageObserver observer) {
		MessagesQueue mq = null;

		synchronized (_queues) {
			for (MessagesQueue q : _queues) {
				if (q.Message.isInstance(message)) {
					mq = q;
					break;
				}
			}

			if (mq == null) {
				mq = new MessagesQueue();
				mq.Message = message.getClass();
				mq.Queue = new LinkedList<MessageQueueItem>();
				_queues.add(mq);
			}
		}

		boolean startNewThread = false;

		synchronized (mq) {
			startNewThread = mq.Queue.peek() == null;
			MessageQueueItem mqi = new MessageQueueItem();
			mqi.Message = message;
			mqi.Observer = observer;
			mqi.OnComplete = observer.RegisterAction(this, "MessageQueue");
			mqi.ExcludeSender = exclude;
			mq.Queue.add(mqi);
		}

		final MessagesQueue messagesQueue=mq;
		
		if (startNewThread)
			_threadPool
					.execute(() -> {
						while (true) {
							MessageQueueItem m;
							boolean isLast = false;
							synchronized (messagesQueue) {
								m = messagesQueue.Queue.poll();
								isLast = messagesQueue.Queue.peek() == null;
								if(isLast)
									messagesQueue.Queue.add(m);
							}

							//send queue message
							for (Iterator<QueueMessageListener> i = GetListenersFor(
									QueueMessageListener.class, message); i
									.hasNext();) {
								QueueMessageListener l = i.next();
								if (m.ExcludeSender
										&& l == m.Observer.getSender())
									continue;
								l.QueueListen(m.Message, m.Observer);
							}

							// send queue UI message
							for (Iterator<QueueUiMessageListener> i = GetListenersFor(
									QueueUiMessageListener.class, message); i
									.hasNext();) {
								QueueUiMessageListener l = i.next();
								if (m.ExcludeSender
										&& l == m.Observer.getSender())
									continue;
								_syncUiContext.Send(() -> l.QueueUiListen(
										m.Message, m.Observer));
							}

							if (isLast) {
								// send last queue message
								for (Iterator<QueueLastMessageListener> i = GetListenersFor(
										QueueLastMessageListener.class, message); i
										.hasNext();) {
									QueueLastMessageListener l = i.next();
									if (m.ExcludeSender
											&& l == m.Observer.getSender())
										continue;
									l.QueueLastListen(m.Message, m.Observer);
								}
								// send last queue UI message
								for (Iterator<QueueUiLastMessageListener> i = GetListenersFor(
										QueueUiLastMessageListener.class,
										message); i.hasNext();) {
									QueueUiLastMessageListener l = i.next();
									if (m.ExcludeSender
											&& l == m.Observer.getSender())
										continue;
									_syncUiContext.Send(() -> l
											.QueueUiLastListen(m.Message,
													m.Observer));
								}
							}

							synchronized (messagesQueue) {	
								if(isLast)
									messagesQueue.Queue.remove(m);
								m.OnComplete.run();
								if (messagesQueue.Queue.peek() == null)
									return;
							}
						}
					});
	}

}
