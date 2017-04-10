package engine.events;

import engine.Event;
import engine.Parameter;
import engine.game.eventobserver.InputObservable;
import javafx.scene.input.KeyCode;

public class KeyReleaseEvent extends Event {

	public KeyReleaseEvent(KeyCode keyToListenFor) {
		addParam(new Parameter("Key", KeyCode.class, keyToListenFor));
	}

	@Override	
	public boolean act(){
		if (((InputObservable)getEventObservable()).isKeyReleaseToProcess()){
			if (getParam("Key").equals(((InputObservable) getEventObservable()).getLastPressedKey()))
				return true;
		}
		return false;
	}

}