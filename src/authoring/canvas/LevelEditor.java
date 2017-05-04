package authoring.canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import authoring.Workspace;
import authoring.command.AddDeleteCommand;
import authoring.command.AddInfo;
import authoring.command.DeleteInfo;
import authoring.command.MoveCommand;
import authoring.command.MoveInfo;
import authoring.command.MultiEntityInfo;
import authoring.command.ResizeCommand;
import authoring.command.ResizeInfo;
import authoring.components.CustomTooltip;
import authoring.networking.Packet;
import engine.entities.Entity;
import engine.game.Level;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import utils.views.View;

/**
 * LevelEditor keeps track of multiple levels and assigns a LayerEditor to each
 * LevelEditor. The LevelEditor displays each Layer through a TabPane structure
 * where each Level has its own tab.
 * 
 * @author jimmy
 *
 */
public class LevelEditor extends View {
	private int PASTE_OFFSET = 25;
	private Workspace workspace;
	private TabPane tabPane;
	private LayerEditor currentLevel;
	private List<EntityView> copiedEntities;
	private List<LayerEditor> levels;
	private int levelCount;

	/**
	 * Make a new LevelEditor
	 * 
	 * @param workspace
	 *            The workspace that the LevelEditor is currently in.
	 */
	public LevelEditor(Workspace workspace) {
		this.workspace = workspace;
		setup();
	}

	/**
	 * Gets the list of all the levels that this LevelEditor currently keeps
	 * contains. This method actually converts the List<Entities> from each
	 * LayerEditor into a Level object.
	 * 
	 * @return List of levels that this LevelEditor keeps track of.
	 */
	public List<Level> getLevels() {
		List<Level> currentLevels = new ArrayList<Level>();
		for (LayerEditor level : levels) {
			currentLevels.add(level.getLevel());
		}
		return currentLevels;
	}

	/**
	 * Sets the levels of this LevelEditor to those described in the given List
	 * of Levels.
	 * 
	 * @param levels
	 *            List of levels to load for this LevelEditor.
	 */
	public void loadGame(List<Level> levels) {
		setup();
		for (Level level : levels) {
			tabPane.getSelectionModel().select(0);
			currentLevel.loadLevel(level);
			if (levelCount < levels.size()) {
				tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab());
			}
		}
	}

	/**
	 * Update the Entities in each Level to match their default Entity.
	 * 
	 * @param entity
	 *            the default Entity to match.
	 */
	public void updateEntity(Entity entity) {
		for (LayerEditor layerEditor : levels) {
			layerEditor.updateEntity(entity);
		}
	}

	/**
	 * Initialize the LevelEditor.
	 */
	private void setup() {
		levelCount = 0;
		levels = new ArrayList<LayerEditor>();
		tabPane = new TabPane();
		copiedEntities = new ArrayList<EntityView>();
		tabPane.getTabs().add(newTab());
		tabPane.getTabs().add(makePlusTab());
		setCenter(tabPane);
		setPadding(new Insets(0, 10, 0, 5));
	}

	/**
	 * Make a new tab (level) for the LevelEditor.
	 * 
	 * @return Tab Tab representing the new level
	 */
	private Tab newTab() {
		Tab tab = new Tab();
		levelCount++;
		tab.setText(String.format("Level %d", levelCount));
		if (levelCount > 1) {
			LayerEditor oldLevel = currentLevel;
			currentLevel = oldLevel.clone();
		} else {
			currentLevel = new LayerEditor(workspace);
		}
		levels.add(currentLevel);
		tab.setContent(currentLevel);
		tab.setOnCloseRequest(e -> {
			closeRequest(e);
			levels.remove(currentLevel);
		});
		return tab;
	}

	public void copy() {
		copiedEntities.clear();
		for (Layer layer : currentLevel.getLayers()) {
			copiedEntities.addAll(layer.getSelectedEntities());
		}
	}

	public void paste() {
		for (Layer layer : currentLevel.getLayers()) {
			layer.getSelectedEntities().forEach(entity -> entity.setSelected(false));
		}
		List<AddInfo> totalAddInfo = new ArrayList<AddInfo>();
		for (EntityView entity : copiedEntities) {
			AddInfo addInfo = new AddInfo(entity.getEntity().getName(), entity.getTranslateX() + PASTE_OFFSET,
					entity.getTranslateY() + PASTE_OFFSET, (int) entity.getEntity().getZ());
			totalAddInfo.add(addInfo);
		}
		MultiEntityInfo<AddInfo> multiAddInfo = new MultiEntityInfo<AddInfo>(totalAddInfo);
		workspace.getNetworking().sendIfConnected(multiAddInfo);
	}

	public void sendToFront() {
		for (Layer layer : currentLevel.getLayers()) {
			/*
			 * List<EntityView> selected = layer.getSelectedEntities();
			 * layer.getEntities().removeAll(selected);
			 * System.out.println(selected.toString());
			 * layer.getEntities().addAll(0, selected);
			 */
			layer.getSelectedEntities().forEach(entity -> entity.toFront());
		}
	}

	public void sendToBack() {
		for (Layer layer : currentLevel.getLayers()) {
			/*
			 * List<EntityView> selected = layer.getSelectedEntities();
			 * layer.getEntities().removeAll(selected);
			 * System.out.println(selected.toString());
			 * layer.getEntities().addAll(layer.getEntities().size() - 1,
			 * selected);
			 */
			layer.getSelectedEntities().forEach(entity -> entity.toBack());
		}
	}

	public EntityView getEntity(long entityId) {
		for (LayerEditor level : levels) {
			for (Layer layer : level.getLayers()) {
				for (EntityView entity : layer.getEntities()) {
					if (entity.getEntityId() == entityId) {
						return entity;
					}
				}
			}
		}
		return null;
	}

	public void received(Packet packet) {
		if (packet instanceof MultiEntityInfo) {
			MultiEntityInfo<?> multiInfo = (MultiEntityInfo<?>) packet;
			if (multiInfo.getInfo() != null) {
				multiInfo.getInfo().forEach(e -> {
					LevelEditor.this.received(e);
				});
			}
		}

		if (packet instanceof AddInfo) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					AddInfo addInfo = (AddInfo) packet;
					double x = addInfo.getX();
					double y = addInfo.getY();
					long entityId = addInfo.getEntityId();
					Entity entity = workspace.getDefaults().getEntity(addInfo.getEntityName()).clone();
					entity.setZ(addInfo.getZ());
					entity.setId(entity.generateId());
					EntityView newEntity = new EntityView(entity, entityId, getCurrentLevel().getCanvas(),
							getCurrentLevel().getCanvas().getTileSize(), x, y);
					AddDeleteCommand addCommand = new AddDeleteCommand(newEntity, LevelEditor.this, true);
					workspace.execute(addCommand);

				}
			});
		} else if (packet instanceof DeleteInfo) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					DeleteInfo deleteInfo = (DeleteInfo) packet;
					EntityView deletedEntity = LevelEditor.this.getEntity(deleteInfo.getEntityId());
					AddDeleteCommand deleteCommand = new AddDeleteCommand(deletedEntity, LevelEditor.this, false);
					workspace.execute(deleteCommand);
				}

			});
		} else if (packet instanceof MoveInfo) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					MoveInfo moveInfo = (MoveInfo) packet;
					EntityView movedEntity = LevelEditor.this.getEntity(moveInfo.getEntityId());
					if (movedEntity != null) {
						MoveCommand moveCommand = new MoveCommand(movedEntity, moveInfo);
						workspace.execute(moveCommand);
					}
				}

			});
		} else if (packet instanceof ResizeInfo) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ResizeInfo resizeInfo = (ResizeInfo) packet;
					EntityView resizedEntity = LevelEditor.this.getEntity(resizeInfo.getEntityId());
					if (resizedEntity != null) {
						ResizeCommand resizeCommand = new ResizeCommand(resizedEntity, resizeInfo);
						workspace.execute(resizeCommand);
					}
				}

			});
		}
	}

	/**
	 * Make a close confirmation request. This is created whenever the user
	 * tries to exit out of a level.
	 * 
	 * @param e
	 *            Event that close confirmation request is attached to
	 */
	private void closeRequest(Event e) {
		Alert alert = workspace.getMaker().makeAlert(AlertType.CONFIRMATION, "ConfirmationTitle", "ConfirmationHeader",
				workspace.getPolyglot().get("ConfirmationContent"));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			e.consume();
		}
	}

	/**
	 * Gets the LayerEditor for the currently selected level
	 * 
	 * @return LayerEditor describing the currently selected level
	 */
	public LayerEditor getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * Make a plus tab which adds a new Level tab whenever it is pressed
	 * 
	 * @return Plus tab
	 */
	private Tab makePlusTab() {
		Tab plusTab = new Tab("+");
		plusTab.setTooltip(new CustomTooltip(workspace.getPolyglot().get("AddLevel")));
		plusTab.setClosable(false);
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
				if (newTab.getText().equals("+")) {
					tabPane.getTabs().add(tabPane.getTabs().size() - 1, newTab());
					workspace.selectExistingLevel(oldTab.getText(), newTab.getText());
					tabPane.getSelectionModel().select(tabPane.getTabs().size() - 2);
					workspace.selectExistingLevel(newTab.getText(),
							tabPane.getSelectionModel().getSelectedItem().getText());
				} else if (!newTab.getText().equals("+") && !oldTab.getText().equals("+")) {
					currentLevel = (LayerEditor) tabPane.getSelectionModel().getSelectedItem().getContent();
					workspace.selectExistingLevel(oldTab.getText(), newTab.getText());
					currentLevel.select();
				}
			}

		});
		return plusTab;
	}

}