package authoring.command;

/**
 * Info needed to describe the resizing of an entity in the game authoring
 * environment. This information can be sent over a network to communicate a
 * resizeCommand to another client.
 * 
 * @author jimmy
 *
 */
public class ResizeInfo extends SingleEntityCommandInfo
{

	private static final long serialVersionUID = 1199205996330909954L;
	private double oldHeight;
	private double oldWidth;
	private double newHeight;
	private double newWidth;

	private double oldX;
	private double oldY;
	private double newX;
	private double newY;

	public ResizeInfo(String entityName, long entityId, double oldHeight, double oldWidth, double newHeight,
			double newWidth, double oldX, double oldY, double newX, double newY)
	{
		super(entityName, entityId);
		this.oldHeight = oldHeight;
		this.oldWidth = oldWidth;
		this.newHeight = newHeight;
		this.newWidth = newWidth;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}

	public double getOldHeight()
	{
		return oldHeight;
	}

	public double getOldWidth()
	{
		return oldWidth;
	}

	public double getNewHeight()
	{
		return newHeight;
	}

	public double getNewWidth()
	{
		return newWidth;
	}

	public double getOldX()
	{
		return oldX;
	}

	public double getOldY()
	{
		return oldY;
	}

	public double getNewX()
	{
		return newX;
	}

	public double getNewY()
	{
		return newY;
	}

}
