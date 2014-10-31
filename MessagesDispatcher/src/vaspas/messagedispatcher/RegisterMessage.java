package vaspas.messagedispatcher;
import java.lang.annotation.*;


@Target(value=ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
@Repeatable(RegisterMessages.class)
public @interface RegisterMessage {
	Class<?> Listener(); 
Class<?> Message(); 
}

