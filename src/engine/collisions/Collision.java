package engine.collisions;

import engine.entities.Entity;
import utils.math.IntChecker;

/**
 * Collision stores a single collision between two Entities. The information of
 * a Collision is the first and second Entity involved in the collision as well
 * as the direction from which the first Entity collided with the second Entity.
 * 
 * @author Kyle Finke
 * @author Nikita Zemlevskiy
 *
 */
public class Collision implements CollisionInterface {
	private Entity firstEntity;
	private Entity secondEntity;
	private CollisionSide firstRelativeToSecond;
	private double collisionDepth;
	private IntChecker checker;

	/**
	 * Create a new Collision.
	 * 
	 * @param one
	 *            the first entity participating in the collision
	 * @param two
	 *            the second entity participating in the collision
	 * @param side
	 *            the side the collision is taking place on from the perspective
	 *            of the first entity
	 * @param depth
	 *            the amount of pixels which the entities are allowed to overlap
	 *            before a collision happens
	 */
	public Collision(Entity one, Entity two, CollisionSide side, double depth) {
		firstEntity = one;
		secondEntity = two;
		firstRelativeToSecond = side;
		collisionDepth = depth;
		checker = new IntChecker();
	}

	public Entity getFirstEntity() {
		return firstEntity;
	}

	public Entity getSecondEntity() {
		return secondEntity;
	}

	public CollisionSide getCollisionSide() {
		return firstRelativeToSecond;
	}

	public double getCollisionDepth() {
		return collisionDepth;
	}

	/**
	 * Check whether this collision is between two entities with the respective
	 * names
	 * 
	 * @param name1
	 *            name or id of the first entity colliding
	 * @param name2
	 *            name or id of of the second entity colliding
	 * @return whether the names given match the the entities in this collision.
	 */
	public boolean isBetween(String name1, String name2) {
		int id1 = setId(name1);
		int id2 = setId(name2);
		return (firstEntity.getName().equals(name1) && secondEntity.getName().equals(name2))
				|| (id1 != -1 && firstEntity.getId() == id1 && secondEntity.getName().equals(name2))
				|| (id2 != -1 && firstEntity.getName().equals(name1) && secondEntity.getId() == id2)
				|| (id1 != -1 && id2 != -1 && firstEntity.getId() == id1 && secondEntity.getId() == id2);
	}

	private int setId(String str) {
		return checker.check(str) ? Integer.parseInt(str) : -1;
	}
}
