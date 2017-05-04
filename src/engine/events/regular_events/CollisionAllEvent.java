package engine.events.regular_events;

import engine.Parameter;
import engine.collisions.Collision;
import engine.collisions.CollisionSide;
import engine.entities.Entity;
import engine.events.Event;

/**
 * Stores a Collision that is associated with a certain Entity. Whenever that
 * collision occurs, the Actions associated with the CollisionEvent will run.
 * 
 * @author Kyle Finke
 *
 */
public class CollisionAllEvent extends Event {
	private CollisionSide collisionSide;

	public CollisionAllEvent() {
		addParam(new Parameter(getResource("Entity1"), String.class, getResource("ThisEntity")));
		addParam(new Parameter(getResource("Entity2"), String.class, ""));
		addParam(new Parameter(getResource("DetectionDepth"), double.class, 0.0));
		this.collisionSide = CollisionSide.ALL;
	}

	/**
	 * Sets the primary Collision associated with the CollisionEvent for the
	 * associated Entity.
	 * 
	 * @param collision
	 */
	protected void setCollisionSide(CollisionSide collisionSide) {
		this.collisionSide = collisionSide;
	}

	/**
	 * Checks the list of Collisions for the current step of the game against
	 * the Collision contained in this CollisionEvent. If any Collision in the
	 * list of all Collisions is equal to the one contained in this
	 * CollisionEvent, the event returns true. Otherwise, it returns false.
	 */
	@Override
	public boolean act() {
		for (Collision collision : getEntity().getGameInfo().getObservableBundle().getCollisionObservable().getCollisions()) {
			String param1 = ((String) getParam(getResource("Entity1"))).equals(getResource("ThisEntity")) ? getEntity().getId() + "" : (String) getParam(getResource("Entity1"));
			if (collision.isBetween(param1, (String) getParam(getResource("Entity2")))
					&& (this.collisionSide == CollisionSide.ALL || this.collisionSide.equals(collision.getCollisionSide()))
					&& collision.getCollisionDepth() > (double) getParam(getResource("DetectionDepth"))) {
				return true;
			}
		}
		return false;
	}
}
