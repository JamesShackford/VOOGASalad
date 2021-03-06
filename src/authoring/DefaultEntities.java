package authoring;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import engine.entities.Entity;
import engine.entities.entities.BackgroundEntity;
import engine.entities.entities.BlockEntity;
import engine.entities.entities.CameraEntity;
import engine.entities.entities.CharacterEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Elliott Bolzan
 *
 *         Stores the user's default Entities (those created by the user, and
 *         those loaded from a Game). Also keeps track of the currently selected
 *         Entity.
 * 
 */
public class DefaultEntities {

	private Workspace workspace;
	private ObservableList<Entity> entities;
	private Entity selected;
	
	/**
	 * Creates DefaultEntities.
	 * @param workspace the workspace that owns DefaultEntities.
	 */
	public DefaultEntities(Workspace workspace) {
		this.workspace = workspace;
		this.entities = FXCollections.observableArrayList(new ArrayList<Entity>());
		if (workspace.getGame().getDefaults().size() == 0) {
			add(new CharacterEntity());
			add(new BlockEntity());
			add(new CameraEntity());
			add(new BackgroundEntity());
		}
	}

	/**
	 * @return the default Entities.
	 */
	public ObservableList<Entity> getEntities() {
		return entities;
	}

	/**
	 * Sets the default Entities to a List of Entities.
	 * @param entities the Entities to be set as defaults.
	 */
	public void setEntities(List<Entity> entities) {
		this.entities.clear();
		this.entities.addAll(entities);
	}

	/**
	 * @return the currently selected Entity.
	 */
	public Entity getSelectedEntity() {
		return selected;
	}

	/**
	 * Sets the selected Entity.
	 * @param entity the new selected Entity.
	 */
	public void setSelectedEntity(Entity entity) {
		this.selected = entity;
	}

	/**
	 * Add an Entity to Defaults.
	 * @param entity the entity to be added.
	 */
	public void add(Entity entity) {
		entities.add(entity);
		updateModel();
	}

	/**
	 * Remove an Entity from the defaults.
	 * @param entity the Entity to be removed.
	 */
	public void remove(Entity entity) {
		entities.remove(entity);
		updateModel();
	}
	
	public void edit(Entity entity) {
		for (Entity old: entities) {
			if (old.getName().equals(entity.getName())) {
				old.widthProperty().set(entity.getWidth());
				old.heightProperty().set(entity.getHeight());
				return;
			}
		}
	}

	/**
	 * 
	 * @return the names of the default Entities.
	 */
	public List<String> getNames() {
		return entities.stream().map(Entity::getName).collect(Collectors.toList());
	}
	
	public Entity getEntity(String name) {
		for (Entity entity: entities) {
			if (entity.getName().equals(name)) {
				return entity;
			}
		}
		return null;
	}

	/**
	 * Update the current Game's default Entities.
	 */
	private void updateModel() {
		workspace.getGame().setDefaults(new ArrayList<Entity>(entities));
	}

	/**
	 * Update the Canvas when a default Entity is updated.
	 * @param entity the Entity that was updated.
	 */
	public void updateCanvas(Entity entity) {
		workspace.updateEntity(entity);
	}

}
