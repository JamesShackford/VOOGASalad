package authoring.panel.creation;

import authoring.Workspace;
import authoring.panel.creation.editors.EntityEditor;
import authoring.panel.creation.pickers.ActionPicker;
import authoring.panel.creation.pickers.EventPicker;
import authoring.panel.display.EntityDisplay;
import engine.entities.Entity;
import engine.events.Event;
import engine.game.EngineController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.views.View;

/**
 * 
 * This class is displayed when an Entity is being created or edited. It serves
 * as a wrapper for four main parts: the EntityInfo, the EntityEditor, the
 * EventPicker, and the ActionPicker.
 * 
 * @author Elliott Bolzan
 * 
 */
public class EntityMaker extends View {

	private Workspace workspace;
	private EntityDisplay display;
	private Stage stage;
	private SplitPane pane;
	private EngineController engine;

	private EntityInfo entityInfo;
	private EntityEditor entityEditor;
	private EventPicker eventPicker;
	private ActionPicker actionPicker;

	private Event selectedEvent;

	/**
	 * Creates an EntityMaker.
	 * 
	 * @param workspace
	 *            the workspace that pertains to this view.
	 * @param display
	 *            the EntityDisplay which created this editor.
	 * @param entity
	 *            an Entity to edit (could be null).
	 */
	public EntityMaker(Workspace workspace, EntityDisplay display, Entity entity) {
		this.workspace = workspace;
		this.display = display;
		engine = new EngineController();
		if (entity == null) {
			entity = engine.getDefaultEntity();
		}
		setupView(entity);
		setupStage();
	}

	/**
	 * @return the EntityMaker's entity.
	 */
	public Entity getEntity() {
		return entityEditor.getEntity();
	}

	/**
	 * @return the EventPicker.
	 */
	public EventPicker getEventPicker() {
		return eventPicker;
	}

	/**
	 * @return the ActionPicker.
	 */
	public ActionPicker getActionPicker() {
		return actionPicker;
	}

	/**
	 * Change the EntityInfo's save button's interaction information for the
	 * tutorial.
	 * 
	 * @param r
	 *            the new Runnable to be run for the EntityInfo's save button.
	 */
	public void changeSaveHandler(Runnable r) {
		entityInfo.changeSaveHandler(r);
	}

	/**
	 * Change the EntityInfo's image picker's interaction information for the
	 * tutorial.
	 * 
	 * @param r
	 *            the new Runnable to be run for the image picker.
	 */
	public void changeImageHandler(Runnable r) {
		entityInfo.changeImageHandler(r);
	}

	private void setupView(Entity entity) {
		entityEditor = new EntityEditor(workspace, entity.clone(), engine.getAllEntities());
		entityInfo = new EntityInfo(workspace, this);
		eventPicker = new EventPicker(workspace, this);
		actionPicker = new ActionPicker(workspace, this);
		pane = new SplitPane(entityInfo, entityEditor, eventPicker, actionPicker);
		pane.setDividerPositions(0.25, 0.5, 0.75);
		setCenter(pane);
	}

	private void setupStage() {
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.titleProperty().bind(workspace.getPolyglot().get("EntityMakerTitle"));
		stage.setResizable(true);
		stage.setScene(createScene());
		stage.show();
		stage.centerOnScreen();
	}

	private Scene createScene() {
		Scene scene = new Scene(this, 860, 450);
		scene.getStylesheets().add(workspace.getIOResources().getString("StylesheetPath"));
		return scene;
	}

	/**
	 * Closes the EntityMaker.
	 */
	public void dismiss() {
		stage.close();
	}

	/**
	 * Set the currently selected Event.
	 * 
	 * @param event
	 *            the currently selected Event.
	 */
	public void setSelectedEvent(Event event) {
		selectedEvent = event;
		actionPicker.update();
	}

	/**
	 * @return the currently selected Event.
	 */
	public Event getSelectedEvent() {
		return selectedEvent;
	}

	/**
	 * Display an error message in a JavaFX dialog.
	 * 
	 * @param message
	 *            the message to display.
	 */
	public void showMessage(String messageProperty) {
		Alert alert = workspace.getMaker().makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader",
				workspace.getPolyglot().get(messageProperty));
		alert.show();
	}

	/**
	 * Save the Entity to defaults.
	 */
	public void save() {
		if (entityInfo.getName().trim().equals("") || entityInfo.getName().contains(" ")) {
			showMessage("EmptyName");
			return;
		}
		if (display.getCurrentlyEditing() == null
				&& workspace.getDefaults().getNames().contains(entityInfo.getName())) {
			showMessage("DifferentName");
			return;
		}
		getEntity().nameProperty().set(entityInfo.getName());
		getEntity().imagePathProperty().set(entityInfo.getImagePath());
		getEntity().widthProperty().set(entityInfo.getImageWidth());
		getEntity().heightProperty().set(entityInfo.getImageHeight());
		display.addEntity(getEntity());
		dismiss();
	}

}
