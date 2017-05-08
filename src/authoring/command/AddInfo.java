package authoring.command;

import java.util.Random;

/**
 * Info needed to add an entity to the game authoring environment. This
 * information can be sent over a network to communicate an addCommand to
 * another client.
 * 
 * @author jimmy
 *
 */
public class AddInfo extends PositionInfo
{
	private static final long serialVersionUID = 3622141588281702380L;

	public AddInfo(String entityName, double xPos, double yPos, int zPos)
	{
		super(entityName, xPos, yPos, zPos, new Random().nextLong());
	}

	public AddInfo(String entityName, double xPos, double yPos, int zPos, long entityId)
	{
		super(entityName, xPos, yPos, zPos, entityId);
	}

}
