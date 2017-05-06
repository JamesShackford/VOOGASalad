package authoring.canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single layer of entities. A layer is simply a list of
 * EntityViews.
 * 
 * @author jimmy and Mina Mungekar
 *
 */
public class Layer
{
	private List<EntityView> entities;

	/*
	 * Make a new Layer, which represents a list of EntityViews.
	 */
	public Layer()
	{
		entities = new ArrayList<EntityView>();
	}

	public Layer(String s)
	{
		entities = new ArrayList<EntityView>();
	}

	/**
	 * Get the list of EntityViews that is represented by this Layer.
	 * 
	 * @return list of EntityViews.
	 */
	public List<EntityView> getEntities()
	{
		return entities;
	}

	/**
	 * Set the list of EntityViews that is represented by this Layer.
	 * 
	 * @param entities
	 *            List of EntityViews that this Layer will now describe
	 */
	public void setEntities(List<EntityView> entities)
	{
		this.entities = entities;
	}

	/**
	 * Add the given EntityView to the layer.
	 * 
	 * @param entity
	 */
	public void addEntity(EntityView entity)
	{
		this.entities.add(entity);
	}

	public List<EntityView> getSelectedEntities()
	{
		List<EntityView> selectedEntities = new ArrayList<EntityView>();
		for (EntityView entity : entities) {
			if (entity.isSelected()) {
				selectedEntities.add(entity);
			}
		}
		return selectedEntities;
	}
}