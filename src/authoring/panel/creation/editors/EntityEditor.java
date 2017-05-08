package authoring.panel.creation.editors;

import java.util.List;

import authoring.Workspace;
import engine.Parameter;
import engine.entities.Entity;
import engine.events.Event;
import engine.game.EngineController;

/**
 * 
 * An extension of Editor, specifically design to edit Entities. Allows the user
 * to consult and set values for a list of parameters for each Entity. Gives the
 * user the option to save his or her progress in creating an Entity.
 * 
 * @author Elliott Bolzan
 *
 */
public class EntityEditor extends Editor {

	private EngineController engine;
	private Entity entity;

	/**
	 * Creates an EntityEditor.
	 * 
	 * @param workspace
	 *            the workspace that owns this Editor.
	 * @param picker
	 *            the picker that owns this Editor.
	 * @param entity
	 *            the entity that this Editor is to edit (or null).
	 * @param elements
	 *            the elements that this Editor is to display in the ComboBox.
	 */
	public EntityEditor(Workspace workspace, Entity entity, List<String> elements) {
		super(workspace, elements, entity, false);
		this.entity = entity;
		engine = new EngineController();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.panel.creation.editors.Editor#selected(java.lang.String)
	 */
	@Override
	public void selected(String string) {
		List<Event> events = entity.getEvents();
		try {
			entity = engine.createEntity(string);
		} catch (Exception e) {
			getWorkspace().getMaker().showFailure();
		}
		entity.setEvents(events);
		update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.panel.creation.editors.Editor#save(java.util.List)
	 */
	@Override
	public void save(List<Parameter> data) {
	}

	/**
	 * @return the Entity being edited.
	 */
	public Entity getEntity() {
		return entity;
	}

}
