package engine.events.regular_events;

import java.util.List;

import engine.GameInfo;
import engine.Parameter;
import engine.entities.Entity;
import engine.events.Event;
import javafx.scene.input.KeyCode;

/**
 * This event includes a KeyPressEvent and a CollisionEvent and only triggers if
 * both of them are triggered.
 * 
 * @author Jay Doherty
 *
 */
public class KeyPressAndCollisionEvent extends CollisionAllEvent {
	public KeyPressEvent keyPressEvent;
	public KeyPressAndCollisionEvent() {

		/*addParam(new Parameter(getResource("Key"), KeyCode.class, KeyCode.UNDEFINED));
		addParam(new Parameter(getResource("Entity"), String.class, ""));
		addParam(new Parameter(getResource("DetectionDepth"), double.class, 0.0));
		keyPressEvent = new KeyPressEvent();
		collisionEvent = new CollisionAllEvent();*/
		keyPressEvent = new KeyPressEvent();
		addParam(new Parameter(getResource("Key"), KeyCode.class, KeyCode.UNDEFINED));
	}

	@Override
	public void setEntity(Entity entity) {
		super.setEntity(entity);
		keyPressEvent.setEntity(entity);
	}

	@Override
	public void setGameInfo(GameInfo info) {
		super.setGameInfo(info);
		keyPressEvent.setGameInfo(info);
	}

	@Override
	public void setParams(List<Parameter> params) {
		super.setParams(params);
		keyPressEvent.setParams(params);
	}
	
	@Override
	public boolean act() {
		return super.act() && keyPressEvent.act();
	}
}
