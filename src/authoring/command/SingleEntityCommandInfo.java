package authoring.command;

/**
 * Info needed to describe a command performed on a single entity in the game
 * authoring environment. This information can be sent over a network to
 * communicate a command to be performed on a single entity.
 * 
 * @author jimmy
 *
 */
public abstract class SingleEntityCommandInfo extends EntityCommandInfo
{
	private static final long serialVersionUID = 7742404661749847649L;
	private long entityId;

	public SingleEntityCommandInfo(String entityName, long entityId)
	{
		super(entityName);
		this.entityId = entityId;
	}

	public long getEntityId()
	{
		return entityId;
	}

}
