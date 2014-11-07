package vaspas.performance;

import java.util.Iterator;
import java.util.LinkedList;

import vaspas.sigflow.module.*;

public class Performer {

	public Performer() {
		ThreadPriority = Thread.NORM_PRIORITY;
	}

	private final LinkedList<Module> _modules = new LinkedList<Module>();

	public void AddModule(Module module) {
		// случай если поток обработки не запущен
		// выполняем в текущем потоке
		if (_actions == null) {
			_modules.add(module);
			return;
		}

		// случай если поток обработки запущен
		// выполняем в потоке обработки
		synchronized (_actions) {
			_actions.add(() -> {
				_modules.add(module);

				if (module instanceof MasterModule)
					((MasterModule) module).Start();
			});
		}
	}

	public void RemoveModule(Module module) {
		// случай если поток обработки не запущен
		// выполняем в текущем потоке
		if (_actions == null) {
			_modules.remove(module);
			return;
		}

		// случай если поток обработки запущен
		// выполняем в потоке обработки
		synchronized (_actions) {
			_actions.add(() -> {
				_modules.remove(module);

				if (module instanceof MasterModule) {
					((MasterModule) module).BeforeStop();
					((MasterModule) module).AfterStop();
				}
			});
		}
	}

	public boolean Contains(Module module) {
		// случай если поток обработки не запущен
		if (_actions == null)
			return _modules.contains(module);

		// случай если поток обработки запущен
		synchronized (_actions) {
			return _modules.contains(module);
		}
	}

	private LinkedList<Runnable> _actions;

	public void ChangeModule(Module oldModule, Module newModule) {
		// случай если поток обработки не запущен
		// выполняем в текущем потоке
		if (_actions == null) {
			_modules.add(_modules.indexOf(oldModule), newModule);
			_modules.remove(oldModule);

			return;
		}

		// случай если поток обработки запущен
		// выполняем в потоке обработки
		synchronized (_actions) {
			_actions.add(() -> {
				_modules.add(_modules.indexOf(oldModule), newModule);
				_modules.remove(oldModule);

				if (oldModule instanceof MasterModule) {
					((MasterModule) oldModule).BeforeStop();
					((MasterModule) oldModule).AfterStop();
				}

				if (newModule instanceof MasterModule)
					((MasterModule) newModule).Start();
			});
		}
	}

	public vaspas.performance.Beat Beat;

	public int ThreadPriority;

	private final Object _event = new Object();
	private volatile boolean _beatImpluse;

	private Thread _thread;

	private volatile boolean _threadTerminated;

	public boolean Start() {
		_actions = new LinkedList<Runnable>();

		_beatImpluse = false;

		_thread = new Thread(() -> ThreadFunc());
		_thread.setPriority(ThreadPriority);
		_thread.setName("SigproPatternPerformerThread");
		_thread.setDaemon(true);
		_threadTerminated = false;
		_thread.start();

		boolean started = true;
		Iterator<Module> iter = _modules.descendingIterator();
		while (iter.hasNext()) {
			Module m = iter.next();
			if (!(m instanceof MasterModule))
				continue;

			if (!((MasterModule) m).Start())
				started = false;
		}

		Beat.connect(() -> {
			_beatImpluse = true;
			synchronized (_event) {
				_event.notify();
			}
		});
		return started;
	}

	public void Stop() {
		// ждем завершения выполнения действий
		synchronized (_actions) {
			_actions.forEach(a -> a.run());
			_actions.clear();

			_modules.forEach(m -> {
				if (m instanceof MasterModule)
					((MasterModule) m).BeforeStop();
			});
		}

		_threadTerminated = true;
		synchronized (_event) {
			_event.notify();
		}
		try {
			_thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		_modules.forEach(m -> {
			if (m instanceof MasterModule)
				((MasterModule) m).AfterStop();
		});

		_actions = null;
		Beat.connect(null);
	}

	private void ThreadFunc() {
		boolean[] executeFlags = new boolean[0];

		while (!_threadTerminated) {
			// выполняем действия в процессе работы
			synchronized (_actions) {
				_actions.forEach(a -> a.run());
				_actions.clear();
			}

			// ждем события для очередной обработки
			try {
				if (!_beatImpluse)
					synchronized (_event) {
						_event.wait();
					}

				_beatImpluse = false;
			} catch (InterruptedException e) {

			}

			// проверяем флаг, возможно это завершение работы
			if (_threadTerminated)
				continue;

			if (executeFlags.length < _modules.size())
				executeFlags = new boolean[_modules.size()];

			// далее выполняем обработку
			boolean executeNext = false;
			int i = 0;
			for (Iterator<Module> iter = _modules.iterator(); iter.hasNext(); i++) {
				Module m = iter.next();

				if (!(m instanceof ExecuteModule)) {
					executeFlags[i] = false;
					continue;
				}

				ExecuteResult r = ((ExecuteModule) m).Execute();

				executeFlags[i] = r != ExecuteResult.Independent;

				if (r == ExecuteResult.Worked)
					executeNext = true;
			}

			while (executeNext) {
				executeNext = false;
				i = 0;
				for (Iterator<Module> iter = _modules.iterator(); iter
						.hasNext(); i++) {
					Module m = iter.next();

					if (!executeFlags[i])
						continue;

					if (((ExecuteModule) m).Execute() == ExecuteResult.Worked)
						executeNext = true;
				}
			}
		}
	}

}
