package vaspas.sigflow.dataflow;

public interface BufferState {

	int getMaxCapacity();

    boolean isOverflow();

    void dropOverflow();

    int count();

    void connectOnOverflow();	
}
