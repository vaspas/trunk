package vaspas.messagedispatcher;

public interface MessageObserver {

	
	/* 
	 * ������������ ����� �������� ��� ����������.
	 */
    Runnable RegisterAction(Object owner, String name);

    /*
    * ������� ������ ����������� ��� ������ ���������.
    */
    MessageObserver CreateSubObserver(Object message, Object sender);

    /// <summary>
    /// ������ ��������� ���������.
    /// </summary>
    Object getSender();
	
}
