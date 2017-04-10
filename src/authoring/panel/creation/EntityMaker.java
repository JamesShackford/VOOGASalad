package authoring.panel.creation;

import authoring.Workspace;
import authoring.components.ComponentMaker;
import authoring.panel.creation.editors.EntityEditor;
import authoring.panel.creation.pickers.*;
import authoring.panel.display.EntityDisplay;
import authoring.views.ConcreteView;
import authoring.views.View;
import engine.Entity;
import engine.Event;
import engine.game.EngineController;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Elliott Bolzan
 *
 */
public class EntityMaker {
	
	private Workspace workspace;
	private EntityDisplay display;
	private Stage stage;
	private View view;
	private SplitPane pane;
	private Entity entity;
	private EngineController engine;
	
	private EntityInfo entityInfo;
	private EntityEditor entityEditor;
	private EventPicker eventPicker;
	private ActionPicker actionPicker;
	
	private Event selectedEvent;

	/**
	 * 
	 */
	public EntityMaker(Workspace workspace, EntityDisplay display, Entity entity) {
		this.workspace = workspace;
		this.display = display;
		this.entity = entity;
		engine = new EngineController();
		if (this.entity == null) {
			this.entity = engine.getDefaultEntity();
		}
		setupView();
		setupStage();
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	private void setupView() {
		view = new ConcreteView(workspace.getResources().getString("EntityMakerTitle"));
		entityInfo = new EntityInfo(workspace, this);
		entityEditor = new EntityEditor(workspace, entity, engine.getAllEntities());
		eventPicker = new EventPicker(workspace, this);
		actionPicker = new ActionPicker(workspace, this);
		pane = new SplitPane(entityInfo, entityEditor, eventPicker, actionPicker);
		pane.setDividerPositions(0.25, 0.5, 0.75);
		view.setCenter(pane);
	}
	
	private void setupStage() {
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(workspace.getResources().getString("EntityMakerTitle"));
		stage.setResizable(true);
		stage.setScene(createScene());
		stage.show();
		stage.centerOnScreen();
	}
	
	private Scene createScene() {
		Scene scene = new Scene(view, 800, 300);
		scene.getStylesheets().add(workspace.getResources().getString("StylesheetPath"));
		return scene;
	}
	
	public void dismiss() {
		stage.close();
	}
	
	public void setSelectedEvent(Event event) {
		selectedEvent = event;
		actionPicker.update();
	}
	
	public Event getSelectedEvent() {
		return selectedEvent;
	}
	
	public void showMessage(String message) {
		ComponentMaker maker = new ComponentMaker(workspace.getResources());
		maker.makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader", message).show();
	}
	
	public void save() {
		if (entityInfo.getName().trim().equals("")) {
			showMessage(workspace.getResources().getString("EmptyName"));
			return;
		}
		entity.nameProperty().set(entityInfo.getName());
		entity.imagePathProperty().set(entityInfo.getImagePath());
		display.addEntity(entity);
		dismiss();
	}
	
}
