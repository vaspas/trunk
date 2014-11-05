package vaspas.sigflow.module;

public interface MasterModule extends Module {
	boolean Start();

    void BeforeStop();
    void AfterStop();
}
