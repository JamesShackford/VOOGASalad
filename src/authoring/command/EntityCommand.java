package authoring.command;

import authoring.canvas.EntityView;

/**
 * Represents a command that is performed on a single entity. The execute()
 * method applies to the entity in this class.
 * 
 * @author jimmy
 *
 */
public abstract class EntityCommand implements UndoableCommand
{

	private EntityView entityView;

	public EntityCommand(EntityView entityView)
	{
		this.entityView = entityView;
	}

	public EntityView getEntityView()
	{
		return entityView;
	}

}
