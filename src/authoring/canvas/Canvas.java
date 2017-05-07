package authoring.canvas;

import java.util.ArrayList;
import java.util.List;

import authoring.Workspace;
import engine.entities.Entity;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import utils.views.View;

/**
 * 
 * The Canvas is where you can drag Entities to create your own level. It can
 * contain multiple layers for backgrounds and foregrounds (layers). The layers
 * are mostly used to specify the order in which entities . The Canvas is
 * located in the center of the workspace. The top-left corner of the canvas is
 * set to be the origin (0,0) of the coordinate system.
 * 
 * @author jimmy
 *
 */
public class Canvas extends View {
	public static final int TILE_SIZE = 25;

	private List<EntityView> entities;
	private ObservableList<EntityView> entityList;

	private Workspace workspace;

	private ZoomablePane zoomablePane;
	private ExpandablePane expandablePane;

	public Canvas(Workspace workspace) {
		super(workspace.getPolyglot().get("CanvasTitle"));
		this.workspace = workspace;
		setup();
	}

	public ExpandablePane getExpandablePane() {
		return expandablePane;
	}

	public ZoomablePane getZoomablePane() {
		return zoomablePane;
	}

	/**
	 * Remove all of the entities from within the canvas.
	 */
	public void clear() {
		setup();
	}

	/**
	 * Set the given eventHandler to the onMouseClickedProperty of the pane
	 * portion of the canvas.
	 * 
	 * @param eventHandler
	 *            EventHandler that determines what happens when the mouse is
	 *            clicked on the pane of the Canvas.
	 */
	public void setPaneOnMouseClicked(EventHandler<? super MouseEvent> eventHandler) {
		expandablePane.setOnMouseClicked(eventHandler);
	}

	/**
	 * Set the given eventHandler to the onMouseDraggedProperty of the pane
	 * portion of the canvas.
	 * 
	 * @param eventHandler
	 *            EventHandler that determines what happens when the mouse is
	 *            dragged on the pane of the Canvas.
	 */
	public void setPaneOnMouseDragged(EventHandler<? super MouseEvent> eventHandler) {
		expandablePane.setOnMouseDragged(eventHandler);
	}

	/**
	 * Set up the canvas (set all of its entities and displays to the default
	 * ones).
	 */
	private void setup() {
		entities = new ArrayList<EntityView>();
		entityList = FXCollections.observableList(new ArrayList<EntityView>(entities));
		setupZoomExpandPane();

		VBox layout = new VBox();
		layout.getChildren().setAll(zoomablePane);
		VBox.setVgrow(zoomablePane, Priority.ALWAYS);
		this.setCenter(zoomablePane);
	}

	private void setupZoomExpandPane() {
		final Group group = new Group();
		expandablePane = new ExpandablePane();
		group.getChildren().add(expandablePane);
		zoomablePane = new ZoomablePane(group);
	}

	/**
	 * Returns the size of each tile in the Canvas.
	 * 
	 * @return canvas tile size.
	 */
	public int getTileSize() {
		return TILE_SIZE;
	}

	public List<EntityView> getSelectedEntities() {
		List<EntityView> selected = new ArrayList<EntityView>();
		entities.forEach(e -> {
			if (e.isSelected()) {
				selected.add(e);
			}
		});
		return selected;
	}

	/**
	 * Add an entity to the top-left corner of the canvas. This method makes a
	 * clone of the given Entity and creates an actual EntityView from it ((The
	 * EntityView is what is actually displayed in the Canvas.)
	 * 
	 * @param entity
	 *            Entity to be added to the canvas.
	 * @return EntityView that is displayed in the Canvas.
	 */
	public EntityView addEntity(Entity entity) {
		return this.addEntity(entity, 0, 0);
	}

	/**
	 * Add an entity to the given x and y position of the canvas. This method
	 * makes a clone of the given Entity and creates an actual EntityView from
	 * it (the EntityView is what is actually displayed in the Canvas)
	 * 
	 * @param entity
	 *            Entity to be added
	 * @param x
	 *            x Position
	 * @param y
	 *            y position
	 * @return EntityView that is displayed in the Canvas.
	 */
	public EntityView addEntity(Entity entity, double x, double y) {
		EntityView newEntity = new EntityView(entity, this, TILE_SIZE, x, y);
		newEntity.setTranslateX(GridUtil.getTiledCoordinate(x, TILE_SIZE));
		newEntity.setTranslateY(GridUtil.getTiledCoordinate(y, TILE_SIZE));
		this.addEntityView(newEntity);

		return newEntity;
	}

	public void addEntityView(EntityView entity) {
		entities.add(entity);
		entityList.add(entity);
		expandablePane.addEntity(entity, entity.getTranslateX(), entity.getTranslateY());
	}

	/**
	 * Remove the given EntityView from the Canvas.
	 * 
	 * @param entity
	 *            EntityView to be removed from the Canvas.
	 */
	public void removeEntity(EntityView entity) {
		entities.remove(entity);
		expandablePane.getChildren().remove(entity);
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void addEntityListener(Runnable r) {
		entityList.addListener((ListChangeListener.Change<? extends EntityView> c) -> {
			r.run();
		});

	}

}