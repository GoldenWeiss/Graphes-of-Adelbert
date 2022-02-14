package view.childInterface;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public abstract class RegisterableChild {
	public abstract boolean closed();
	public abstract void setOnCloseRequest(EventHandler<WindowEvent> value);
	public abstract void requestFocus();
}
