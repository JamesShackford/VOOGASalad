package authoring;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;

import authoring.canvas.LevelEditor;
import authoring.command.AddInfo;
import authoring.command.EntityListInfo;
import authoring.command.UndoableCommand;
import authoring.components.ComponentMaker;
import authoring.menu.WorkspaceMenuBar;
import authoring.networking.Networking;
import authoring.networking.Packet;
import authoring.panel.Panel;
import data.Game;
import data.GameData;
import engine.entities.Entity;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import player.launchers.BasicPlayer;
import polyglot.Polyglot;
import utils.views.View;

/**
 * 
 * The container for the Game Authoring Environment. Displays a SplitPane, which
 * contains the Panel and the Canvas. Serves as an intermediary between the
 * default Entities, the Panel, and the Canvas.
 * 
 * @author Elliott Bolzan (modified by Mina Mungekar, Jimmy Shackford, Jesse
 *         Yue)
 * 
 */
public class Workspace extends View
{
	private Polyglot polyglot;
	private ResourceBundle IOResources;
	private ComponentMaker maker;
	private GameData data;
	private SplitPane pane;
	private LevelEditor levelEditor;
	private Panel panel;
	private Game game;
	private DefaultEntities defaults;
	private Networking networking;
	private Stack<UndoableCommand> undoStack;
	private Stack<UndoableCommand> redoStack;

	/**
	 * Creates the Workspace.
	 * 
	 * @param IOResources
	 *            the ResourceBundle that pertains to this Workspace.
	 * @param path
	 *            the path of the Game to be loaded.
	 */
	public Workspace(Game game, Polyglot polyglot, ResourceBundle IOResources) {
		this.game = game;
		this.polyglot = polyglot;
		this.IOResources = IOResources;
		setup();
		load();
	}

	/**
	 * @return the Workspace's current Game.
	 */
	public Game getGame() {
		return game;
	}

	public Networking getNetworking() {
		return networking;
	}

	public Panel getPanel() {
		return panel;
	}

	/**
	 * Initializes the Workspace's components.
	 */
	private void setup() {
		undoStack = new Stack<UndoableCommand>();
		redoStack = new Stack<UndoableCommand>();
		networking = new Networking(this);
		data = new GameData();
		maker = new ComponentMaker(polyglot, IOResources);
		defaults = new DefaultEntities(this);
		pane = new SplitPane();
		panel = new Panel(this, 0);
		levelEditor = new LevelEditor(this);
		pane.getItems().addAll(panel, levelEditor);
		pane.setDividerPositions(0.27);
		pane.getStyleClass().add("workspace-pane");
		setCenter(pane);
		setTop(new WorkspaceMenuBar(this));
		setupDragToAddEntity();
	}

	public void received(Packet packet) {
		if (packet instanceof EntityListInfo) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					defaults.getEntities().clear();
					defaults.getEntities().addAll(((EntityListInfo) packet).getEntities());
					((EntityListInfo) packet).getEntities().forEach(e -> updateEntity(e));
				}
			});
		}
	}

	private void setupDragToAddEntity() {
		panel.getEntityDisplay().getList().setOnDragDetected(e -> {
			Entity addedEntity = panel.getEntityDisplay().getList().getSelectionModel().getSelectedItem();
			addedEntity.setId(addedEntity.generateId());
			Image image = new Image(addedEntity.getImagePath());
			panel.setCursor(new ImageCursor(image, 0, 0));
			panel.getEntityDisplay().getList().setOnMouseReleased(e2 -> {
				Point2D canvasPoint = levelEditor.getCurrentLevel().getCanvas().getExpandablePane()
						.screenToLocal(new Point2D(e2.getScreenX(), e2.getScreenY()));
				Point2D workspacePoint = Workspace.this.screenToLocal(new Point2D(e2.getScreenX(), e2.getScreenY()));
				Bounds canvasBounds = levelEditor.getCurrentLevel().getCanvas()
						.localToScene(levelEditor.getCurrentLevel().getCanvas().getBoundsInLocal());
				// only add entity to canvas if the mouse intersects the canvas.
				if (canvasBounds.intersects(workspacePoint.getX(), workspacePoint.getY(), 0, 0)) {
					AddInfo addInfo = new AddInfo(addedEntity.getName(), canvasPoint.getX(), canvasPoint.getY(),
							getLevelEditor().getCurrentLevel().getCurrentLayer());
					deselectAllEntities();
					getNetworking().sendIfConnected(addInfo);
				}
				panel.setCursor(Cursor.DEFAULT);
			});
		});
	}

	private void deselectAllEntities() {
		getLevelEditor().getCurrentLevel().getLayers().forEach(layer -> {
			layer.getSelectedEntities().forEach(selectedEntity -> {
				selectedEntity.setSelected(false);
			});
		});
	}

	public void execute(UndoableCommand command) {
		command.execute();
		undoStack.push(command);
		redoStack.clear();
	}

	public void undo() {
		if (undoStack.size() > 0) {
			UndoableCommand undoCommand = undoStack.pop();
			undoCommand.unexecute();
			redoStack.push(undoCommand);
		}
	}

	public void redo() {
		if (redoStack.size() > 0) {
			UndoableCommand redoCommand = redoStack.pop();
			redoCommand.execute();
			undoStack.push(redoCommand);
		}
	}

	private void load() {
		levelEditor.loadGame(game.getLevels());
		defaults.setEntities(game.getDefaults());
		this.selectLoadedLevel(levelEditor.getCurrentLevel().getLayerCount());
	}

	/**
	 * Save the Game to disk. A DirectoryChooser is presented to the user; the
	 * Game's construction is finalized; and a call to GameData is made to save
	 * the Game.
	 */
	public void save() {
		TextInputDialog dialog = maker.makeTextInputDialog("SaveTitle", "SaveHeader", "SaveLabel", game.getName());
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> save(name));
	}

	private void save(String title) {
		game.setName(title);
		String path = askForOutputPath();
		if (!path.equals("")) {
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() throws InterruptedException {
					createGame();
					data.saveGame(game, path);
					return null;
				}
			};
			maker.showProgressForTask(task, true);
		}

	}

	private String askForOutputPath() {
		String directory = new File(IOResources.getString("GamesPath")).getAbsolutePath();
		DirectoryChooser chooser = maker.makeDirectoryChooser(directory, "GameSaverTitle");
		File selectedDirectory = chooser.showDialog(getScene().getWindow());
		if (selectedDirectory != null) {
			return selectedDirectory.getAbsolutePath();
		}
		return "";
	}

	/**
	 * Test the Game that is currently being designed.
	 * 
	 * @param game
	 *            the Game to test.
	 * 
	 */
	public void test() {
		createGame();
		Stage stage = new Stage();
		new BasicPlayer(stage, game.clone(), polyglot, IOResources, true);
		stage.show();
	}

	private void createGame() {
		game.setLevels(levelEditor.getLevels());
		game.setDefaults(defaults.getEntities());
	}

	public ComponentMaker getMaker() {
		return maker;
	}

	public Polyglot getPolyglot() {
		return polyglot;
	}

	/**
	 * @return the ResourceBundle for this View's descendants.
	 */
	public ResourceBundle getIOResources() {
		return IOResources;
	}

	/**
	 * @return the SplitPane governing this View.
	 */
	public SplitPane getPane() {
		return pane;
	}

	/**
	 * @return the default Entities the user has created.
	 */
	public DefaultEntities getDefaults() {
		return defaults;
	}

	/**
	 * @return the selected Entity.
	 */
	public Entity getSelectedEntity() {
		return defaults.getSelectedEntity();
	}

	/**
	 * addLayer is called from the panel whenever the user selects the option to
	 * add another layer. The workspace instructs the levelEditor to create
	 * another layer.
	 * 
	 */
	public void addLayer() {
		levelEditor.getCurrentLevel().newLayer();
	}

	/**
	 * When the user selects a new layer from the layer-selecting ComboBox, it
	 * alerts the workspace, which, in turn, instructs the LevelEditor to switch
	 * layers.
	 * 
	 * @param number
	 *            the layer's identifier.
	 */
	public void selectLayer(int number) {
		levelEditor.getCurrentLevel().selectLayer(number);
	}

	/**
	 * When a new level is selected by the user, the LevelEditor alerts the
	 * workspace, which, in turn, alerts the panel to the change being made. The
	 * combobox displaying layer names is subsequently updated.
	 * 
	 * @param newLevelNum
	 *            the number of the new level.
	 */
	public void selectExistingLevel(String oldLevel, String newLevel) {
		panel.selectExistingLevelBox(oldLevel, newLevel);
	}

	public void selectLoadedLevel(List<String> nameList) {
		panel.selectLoadedLevelBox(nameList);
	}

	public void selectLoadedLevel(int layerCount) {
		panel.selectLoadedLevelBox(layerCount);
	}

	public LevelEditor getLevelEditor() {
		return levelEditor;
	}

	/**
	 * When the user instructs the layer panel to delete a layer, the workspace
	 * alerts the levelEditor, telling it to delete a layer of its current
	 * level.
	 * 
	 * @param layer
	 *            the identifier of the layer to be deleted.
	 */
	public void deleteLayer(int layer) {
		levelEditor.getCurrentLevel().deleteLayer(layer);
	}

	/**
	 * Update Entities on the Canvas when their default was edited by the user.
	 * 
	 * @param entity
	 *            the Entity to replace the old Entities with.
	 */
	public void updateEntity(Entity entity)
	{
		levelEditor.updateEntity(entity);
	}

}