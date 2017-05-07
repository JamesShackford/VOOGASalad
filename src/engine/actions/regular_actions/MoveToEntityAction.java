package engine.actions.regular_actions;

import java.util.Collection;

import engine.Parameter;
import engine.actions.Action;
import engine.entities.Entity;

public class MoveToEntityAction extends Action{
	public MoveToEntityAction(){
		this.addParam(new Parameter(getResource("Entity"), String.class, ""));
	}

	@Override
	public void act() {
		if (getParameterEntity(getResource("Entity")) == null)
			return;
		
		getEntity().setX(getParameterEntity("Entity").getX());
		getEntity().setY(getParameterEntity("Entity").getY());
		getEntity().setZ(getParameterEntity("Entity").getZ());
		
	}
	
	private Entity getParameterEntity(String parameterName) {
		Collection<Entity> entities = this.getEntity().getGameInfo().getLevelManager().getCurrentLevel().getEntities();
		for (Entity entity : entities) {
			if (((String) getParam(parameterName)).equals(entity.getName())) {
				return entity;
			}
		}
		return null;
	}

}
