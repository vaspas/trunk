package vaspas.sigflow;
public interface SignalReader
    {
        
		/*
	 	* Чтение данных в массив. После чтения данные исчезают из канала.
	 	*/
        boolean ReadTo(Object data,int length);

        
        /*
         * Кол-во хранящихся отсчетов.
         */
        int getAvailable();
        
        /*
         * Возвращает размер блока для чтения, или -1 если нет блоков.
         */
        int getNextBlockSize();
        
        /*
         * Получение блока данных. После выполнения данные исчезают из канала.
         */
        Object Take();

        /*
         * Положить обратно взятый массив данных.
         */
        void Put(Object data);
        
        /*
         * Пропустить данные, не обязательно будет пропущен указанный размер.
         */
        void TrySkip(int size);
    }