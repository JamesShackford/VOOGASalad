package authoring.command;

import authoring.networking.Packet;

/**
 * Info needed to represent an command performed on an entity in the game
 * authoring environment. This information can be sent over a network to
 * communicate information about the entity to another client.
 * 
 * @author jimmy
 *
 */
public abstract class EntityCommandInfo extends Packet
{
	private static final long serialVersionUID = -8684242471160075071L;

	private String entityName;

	public EntityCommandInfo(String entityName)
	{
		this.entityName = entityName;
	}

	public String getEntityName()
	{
		return entityName;
	}
}
