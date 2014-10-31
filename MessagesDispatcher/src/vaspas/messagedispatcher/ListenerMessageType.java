package vaspas.messagedispatcher;
import java.lang.annotation.*;


@Target(value=ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
@Repeatable(ListenerMessageTypes.class)
public @interface ListenerMessageType {
	Class<?> ListenerType(); 
Class<?> MessageType(); 
}

