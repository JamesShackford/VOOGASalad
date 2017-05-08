package authoring.command;

import java.util.ArrayList;
import java.util.List;

import authoring.networking.Packet;
import engine.entities.Entity;

/**
 * Info needed to represent a list of Entities in the game authoring
 * environment. This information can be sent over a network to communicate an
 * information about multiple Entities to another client.
 * 
 * @author jimmy
 *
 */
public class EntityListInfo extends Packet
{

	private static final long serialVersionUID = 1199205996330909954L;
	private List<EntityInfo> entityInfo;

	public EntityListInfo(List<? extends Entity> addedSublist)
	{
		entityInfo = new ArrayList<EntityInfo>();
		addedSublist.forEach(e -> {
			entityInfo.add(new EntityInfo(e.clone()));
		});
	}

	public List<Entity> getEntities()
	{
		List<Entity> returned = new ArrayList<Entity>();
		entityInfo.forEach(e -> {
			returned.add(e.getEntity());
		});
		return returned;
	}
}
