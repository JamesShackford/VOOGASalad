package authoring.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import authoring.Workspace;
import authoring.command.DeleteInfo;
import authoring.command.MoveInfo;
import authoring.command.MultiEntityInfo;
import authoring.command.ResizeInfo;
import engine.entities.Entity;
import engine.entities.entities.BackgroundEntity;
import engine.entities.entities.CameraEntity;
import engine.game.Level;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import utils.views.View;

/**
 * LayerEditor keeps track of a Canvas and all of the layers on the canvas. Each
 * level can be represented by its own LayerEditor, and thus a Level can be
 * created from the entities on a LayerEditor.
 * 
 * @author jimmy (modified by Mina Mungekar and Elliott Bolzan)
 *
 */
public class LayerEditor extends View
{
	private Workspace workspace;
	private Canvas canvas;
	private Map<Integer, Layer> layers;
	private int layerCount;
	private int currLayer;

	private EntityView levelCameraView;
	private EntityView levelBackgroundView;

	/**
	 * Make a new LayerEditor.
	 * 
	 * @param workspace
	 *            Workspace that the LayerEditor will be in.
	 */
	public LayerEditor(Workspace workspace)
	{
		this.workspace = workspace;
		setup();
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	@Override
	public LayerEditor clone()
	{
		LayerEditor newLevel = new LayerEditor(workspace);
		newLevel.setNumLayers(layerCount);
		layers.keySet().stream().forEach(id -> {
			for (EntityView entity : layers.get(id).getEntities()) {
				newLevel.addEntity(entity.getEntity(), entity.getEntity().getX(), entity.getEntity().getY(), id);
			}
			;
		});
		return newLevel;
	}

	/**
	 * Convert all of the Layers/entities currently in the LayerEditor to a
	 * Level object.
	 * 
	 * @return Level object represented by this LayerEditor.
	 */
	public Level getLevel()
	{
		Level thisLevel = new Level();
		for (Layer layer : layers.values()) {
			for (EntityView entity : layer.getEntities()) {
				thisLevel.addEntity(entity.getEntity());
			}
		}
		thisLevel.setCamera((CameraEntity) levelCameraView.getEntity());
		thisLevel.setBackground((BackgroundEntity) levelBackgroundView.getEntity());
		return thisLevel;
	}

	public void selectAll()
	{
		for (Layer layer : layers.values()) {
			for (EntityView entity : layer.getEntities()) {
				entity.setSelected(true);
			}
		}
	}

	/**
	 * Update the Entities in each layer to reflect the default Entity they
	 * correspond to. This method is called when a default Entity is edited by
	 * the user.
	 * 
	 * @param entity
	 *            the Entity to be edited.
	 */
	public void updateEntity(Entity entity)
	{
		for (Layer layer : layers.values()) {
			List<EntityView> concerned = new ArrayList<>();
			for (EntityView entityView : layer.getEntities()) {
				Entity toEdit = entityView.getEntity();
				if (toEdit.getName().equals(entity.getName())) {
					concerned.add(entityView);
				}
			}
			concerned.forEach(entityView -> {
				double currZ = entityView.getEntity().getZ();
				Entity clonedEntity = entity.clone();
				clonedEntity.setZ(currZ);
				entityView.setEntity(clonedEntity);
			});
		}
	}

	/**
	 * Sets the entities/layers of this LayerEditor to those described in the
	 * given Level object.
	 * 
	 * @param level
	 *            Level to be loaded
	 */
	public void loadLevel(Level level)
	{
		this.clear();
		canvas.clear();
		for (Entity entity : level.getEntities()) {
			addEntity(entity, entity.getX(), entity.getY(), (int) entity.getZ());
		}
		selectLayer(1);
	}

	public List<Layer> getLayers()
	{
		return new ArrayList<Layer>(layers.values());
	}

	/**
	 * Returns the layer that is currently selected.
	 * 
	 * @return Currently selected layer.
	 */
	public int getCurrentLayer()
	{
		return currLayer;
	}

	/**
	 * Clear all o the layers/entities currently in the LayerEditor and
	 * reinitialize the LayerEditor.
	 */
	private void clear()
	{
		while (layerCount > 1) {
			removeLayer(layerCount);
		}
		setup();
	}

	/*
	 * Initialize the LayerEditor.
	 */
	private void setup()
	{
		canvas = new Canvas(workspace);
		setCenter(canvas);
		layers = new HashMap<Integer, Layer>();
		layerCount = 0;
		currLayer = 1;
		levelCameraView = new EntityView(new CameraEntity(), canvas, Canvas.TILE_SIZE, 0, 0);
		levelBackgroundView = new EntityView(new BackgroundEntity(), canvas, Canvas.TILE_SIZE, 0, 0);
		addKeyActions();
		newLayer();
	}

	/**
	 * Add all of the key actions to the LayerEditor Backspace --> delete
	 * currently selected entities ctrl + 'C' --> copy currently selected
	 * entities ctrl + 'V' --> paste currently selected entities
	 */
	private void addKeyActions()
	{
		workspace.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.BACK_SPACE)) {
				for (Layer layer : layers.values()) {
					List<EntityView> selectedEntities = layer.getSelectedEntities();
					selectedEntities.forEach(entity -> {
						DeleteInfo removeInfo = new DeleteInfo(entity.getEntity().getName(), entity.getTranslateX(),
								entity.getTranslateY(), (int) entity.getEntity().getZ(), entity.getEntityId());
						workspace.getNetworking().sendIfConnected(removeInfo);
					});
				}
				e.consume();
			}
		});
	}

	/**
	 * Place the entity at the position of the given MouseEvent.
	 * 
	 * @param e
	 *            MouseEvent to place the entity at.
	 */
	public void addEntity(Entity entity, MouseEvent e)
	{
		try {
			addEntity(entity, e.getX(), e.getY(), currLayer);
		} catch (Exception exception) {
			showSelectMessage();
		}
	}

	/**
	 * Add the given entity to the given x, y, and z position. z position =
	 * layer number.
	 * 
	 * @param entity
	 *            Entity to be added to the LayerEditor.
	 * @param x
	 *            x position to place the Entity
	 * @param y
	 *            y position to place the Entity
	 * @param z
	 *            layer to place the Entity in
	 * @return EntityView that was added.
	 */
	public EntityView addEntity(Entity entity, double x, double y, int z)
	{
		EntityView entityView = new EntityView(entity, this.getCanvas(), canvas.getTileSize(), x, y);
		return this.addEntity(entityView, z);
	}

	public EntityView addEntity(EntityView entity, int z)
	{
		canvas.addEntityView(entity);
		this.addEntityToLayer(entity, z);
		attachSelectionListeners(entity);
		addDragDetection(entity);

		entity.getEntity().addEntityToCanvas(canvas, this, entity, z);

		return entity;
	}

	private void addDragDetection(EntityView entity)
	{
		entity.addEventHandler(MouseEvent.DRAG_DETECTED, e -> {
			Rectangle oldBounds = new Rectangle(entity.getTranslateX(), entity.getTranslateY(), entity.getMinWidth(),
					entity.getMinHeight());
			EventHandler<MouseEvent> dragFinishHandler = new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					Rectangle newBounds = new Rectangle(entity.getTranslateX(), entity.getTranslateY(),
							entity.getMinWidth(), entity.getMinHeight());
					double shiftX = newBounds.getX() - oldBounds.getX();
					double shiftY = newBounds.getY() - oldBounds.getY();
					double resizeShiftX = newBounds.getWidth() - oldBounds.getWidth();
					double resizeShiftY = newBounds.getHeight() - oldBounds.getHeight();
					if (oldBounds.getHeight() != newBounds.getHeight()
							|| oldBounds.getWidth() != newBounds.getWidth()) {
						ResizeInfo resizeInfo = makeResize(entity, shiftX, shiftY, resizeShiftX, resizeShiftY);
						if (workspace.getNetworking() != null) {
							workspace.getNetworking().sendIfConnected(resizeInfo);
						}
					} else if (oldBounds.getX() != newBounds.getX() || oldBounds.getY() != newBounds.getY()) {
						List<MoveInfo> moveInfo = new ArrayList<MoveInfo>();
						LayerEditor.this.getLayers().forEach(layer -> {
							layer.getSelectedEntities().forEach(selectedEntity -> {
								moveInfo.add(makeMove(selectedEntity, shiftX, shiftY));
							});
						});
						MultiEntityInfo<MoveInfo> multiMoveInfo = new MultiEntityInfo<MoveInfo>(moveInfo);
						workspace.getNetworking().sendIfConnected(multiMoveInfo);
					}
					entity.removeEventHandler(MouseEvent.MOUSE_RELEASED, this);
				}
			};
			entity.addEventHandler(MouseEvent.MOUSE_RELEASED, dragFinishHandler);
		});
	}

	private MoveInfo makeMove(EntityView entityView, double shiftX, double shiftY)
	{
		return new MoveInfo(entityView.getEntity().getName(), entityView.getEntityId(),
				entityView.getTranslateX() - shiftX, entityView.getTranslateY() - shiftY, entityView.getTranslateX(),
				entityView.getTranslateY());
	}

	private ResizeInfo makeResize(EntityView entityView, double shiftX, double shiftY, double resizeX, double resizeY)
	{
		return new ResizeInfo(entityView.getEntity().getName(), entityView.getEntityId(),
				entityView.getHeight() - resizeY, entityView.getWidth() - resizeX, entityView.getHeight(),
				entityView.getWidth(), entityView.getTranslateX() - shiftX, entityView.getTranslateY() - shiftY,
				entityView.getTranslateX(), entityView.getTranslateY());
	}

	public void addEntityToLayer(EntityView entityView, int z)
	{
		entityView.getEntity().setZ(z);
		setNumLayers(z);
		layers.get(z).addEntity(entityView);
	}

	private void attachSelectionListeners(EntityView entityView)
	{
		entityView.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			if (!e.isShiftDown() && !entityView.isSelected()) {
				for (Layer layer : layers.values()) {
					layer.getSelectedEntities().forEach(ent -> {
						ent.setSelected(false);
					});
				}
			}
			selectEntity(entityView, !entityView.isSelected());
		});
		entityView.setOnMouseDragged(e -> {
			entityView.setSelected(true);
		});
	}

	/**
	 * Set the given EntityViews selected value.
	 * 
	 * @param entity
	 *            EntityView to be selected/deselected.
	 * @param selected
	 *            true if selected, false if deselected
	 */
	private void selectEntity(EntityView entity, boolean selected)
	{
		entity.setSelected(selected);
	}

	/**
	 * Set the upper limit on the number of layers that this LayerEditor has.
	 * 
	 * @param z
	 *            Number for last layer in this LayerEditor
	 */
	private void setNumLayers(int z)
	{
		while (layerCount < z) {
			newLayer();
		}
	}

	/**
	 * Make a new layer
	 */
	public void newLayer()
	{
		layerCount++;
		Layer newLayer = new Layer("Layer" + " " + layerCount);
		layers.put(layerCount, newLayer);
	}

	/**
	 * Select the given layer.
	 * 
	 * @param newLayer
	 *            Layer to be selected.
	 */
	public void selectLayer(int newLayer)
	{
		newLayerSelected(newLayer);
	}

	private void newLayerSelected(int newVal)
	{
		for (Layer layer : layers.values()) {
			for (Node entity : layer.getEntities()) {
				entity.setOpacity(0.3);
				entity.toBack();
			}
		}

		for (Node entity : layers.get(newVal).getEntities()) {
			entity.setOpacity(1);
			entity.toFront();
		}
		currLayer = newVal;
	}

	/**
	 * Select this LayerEditor (select the first layer)
	 */
	public void select()
	{
		this.selectLayer(1);
		addKeyActions();
	}

	/**
	 * Show an error message
	 */
	private void showSelectMessage()
	{
		Alert alert = workspace.getMaker().makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader",
				workspace.getPolyglot().get("SelectAnEntity"));
		alert.show();
	}

	/**
	 * Get the number of layers in this LayerEditor
	 * 
	 * @return Current number of layers.
	 */
	public int getLayerCount()
	{
		return layerCount;
	}

	/**
	 * Delete the given layer from the LayerEditor.
	 * 
	 * @param layer
	 *            Layer to be deleted
	 */
	public void deleteLayer(int layer)
	{
		if (layerCount == 1) {
			Alert alert = workspace.getMaker().makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader",
					workspace.getPolyglot().get("LayerError"));
			alert.showAndWait();
		} else {
			removeLayer(layer);
		}
	}

	/**
	 * Actually delete the given layer
	 * 
	 * @param layer
	 */
	private void removeLayer(int layer)
	{
		if (layers.get(layer).getEntities().size() != 0) {
			layers.get(layer).getEntities().stream().forEach(id -> {
				canvas.removeEntity(id);
			});
		}
		layers.remove(layer);
		List<Integer> changedValues = layers.keySet().stream().filter(elt -> elt > layer).map(elt -> elt - 1)
				.collect(Collectors.toList());
		changedValues.stream().forEach(id -> {
			layers.put(id, layers.get(id + 1));
		});
		layerCount--;
	}

	public EntityView getLevelCamera()
	{
		return levelCameraView;
	}

	public EntityView getLevelBackground()
	{
		return levelBackgroundView;
	}

	public void setLevelCamera(EntityView camera)
	{
		this.levelCameraView = camera;
	}

	public void setLevelBackground(EntityView background)
	{
		this.levelBackgroundView = background;
	}

	public void addEntityListener(Runnable r)
	{
		canvas.addEntityListener(r);
	}
}