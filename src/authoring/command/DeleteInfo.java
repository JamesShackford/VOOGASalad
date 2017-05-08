package authoring.command;

/**
 * Info needed to delete an entity to the game authoring environment. This
 * information can be sent over a network to communicate an deleteCommand to
 * another client.
 * 
 * @author jimmy
 *
 */
public class DeleteInfo extends PositionInfo
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6162686959807670736L;

	public DeleteInfo(String entityName, double xPos, double yPos, int zPos, long entityId)
	{
		super(entityName, xPos, yPos, zPos, entityId);
	}

}
