package authoring;

import java.io.File;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;

import authoring.canvas.*;
import authoring.command.*;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import player.launchers.BasicPlayer;
import polyglot.Polyglot;
import utils.views.View;

/**
 * 
 * The container for the Game Authoring Environment.
 * 
 * Displays a SplitPane, which contains the Panel and the Canvas. Serves as an
 * intermediary between the default Entities, the Panel, and the Canvas.
 * 
 * Holds references to the Polyglot and the ResourceBundle, which can later be
 * accessed from the views this Workspace is composed of. This is done to reduce
 * duplicated code and retain one central reference to the essential objects for
 * the Workspace.
 * 
 * @author Elliott Bolzan (modified by Mina Mungekar, Jimmy Shackford, Jesse
 *         Yue)
 * 
 */
public class Workspace extends View {
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
	 * @param game
	 *            the Game that the Workspace is initialized with.
	 * @param polyglot
	 *            the internationalization information.
	 * @param IOResources
	 *            the ResourceBundle for the application.
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

	/**
	 * @return a reference to the Workspace's Networking information.
	 */
	public Networking getNetworking() {
		return networking;
	}

	/**
	 * @return the Workspace's Panel.
	 */
	public Panel getPanel() {
		return panel;
	}

	/**
	 * @return the Workspace's LeveEditor.
	 */
	public LevelEditor getLevelEditor() {
		return levelEditor;
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
		panel.setupDragToAddEntity();
	}

	/**
	 * Called when a Packet is received from the Networking instance. If the
	 * Packet is of the appropriate type, the default Entities are updated.
	 * 
	 * @param packet
	 *            the Packet that was received.
	 */
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

	/**
	 * Execute a Command that can later be undone.
	 * 
	 * @param command
	 *            the Command to be executed.
	 */
	public void execute(UndoableCommand command) {
		command.execute();
		undoStack.push(command);
		redoStack.clear();
	}

	/**
	 * Undo the previous Command.
	 */
	public void undo() {
		if (undoStack.size() > 0) {
			UndoableCommand undoCommand = undoStack.pop();
			undoCommand.unexecute();
			redoStack.push(undoCommand);
		}
	}

	/**
	 * Redo the previous Command.
	 */
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
		selectLoadedLevel(levelEditor.getCurrentLevel().getLayerCount());
	}

	/**
	 * Save the Game to disk. A DirectoryChooser is presented to the user; the
	 * Game's construction is finalized; and a call to GameData is made to save
	 * the Game.
	 * 
	 * This implementation is delegated to an overloaded, save(String title),
	 * method. This method presents a TextInputDialog, and only calls
	 * save(String title) if input is provided by the user.
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

	/**
	 * When a new level is selected by the user, the LevelEditor alerts the
	 * workspace, which, in turn, alerts the panel to the change being made. The
	 * combobox displaying layer names is subsequently updated.
	 * 
	 * @param newLevelNum
	 *            the number of the new level.
	 */
	public void selectExistingLevel(String oldLevel, String newLevel) {
		panel.selectExistingLevel(oldLevel, newLevel);
	}

	/**
	 * Selects a Level that is already loaded, referencing by its number.
	 * 
	 * @param layerCount
	 */
	public void selectLoadedLevel(int number) {
		panel.selectLoadedLevel(number);
	}


	/**
	 * Update Entities on the Canvas when their default was edited by the user.
	 * 
	 * @param entity
	 *            the Entity to replace the old Entities with.
	 */
	public void updateEntity(Entity entity) {
		levelEditor.updateEntity(entity);
	}

}