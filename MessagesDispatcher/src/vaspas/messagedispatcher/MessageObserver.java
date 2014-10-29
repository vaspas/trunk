package vaspas.messagedispatcher;

public interface MessageObserver {

	
	/* 
	 * Регистрирует новое действие над сообщением.
	 */
    Runnable RegisterAction(Object owner, String name);

    /*
    * Создает объект наблюдателя для нового сообщения.
    */
    MessageObserver CreateSubObserver(Object message, Object sender);

    /// <summary>
    /// Объект создавший сообщение.
    /// </summary>
    Object getSender();
	
}
