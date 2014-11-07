package vaspas.sigflow.dataflow;
public interface SignalReader
    {
        
		/*
	 	* ������ ������ � ������. ����� ������ ������ �������� �� ������.
	 	*/
        boolean ReadTo(Object data,int length);

        
        /*
         * ���-�� ���������� ��������.
         */
        int getAvailable();
        
        /*
         * ���������� ������ ����� ��� ������, ��� -1 ���� ��� ������.
         */
        int getNextBlockSize();
        
        /*
         * ��������� ����� ������. ����� ���������� ������ �������� �� ������.
         */
        Object Take();

        /*
         * �������� ������� ������ ������ ������.
         */
        void Put(Object data);
        
        /*
         * ���������� ������, �� ����������� ����� �������� ��������� ������.
         */
        void TrySkip(int size);
    }