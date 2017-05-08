package player.menus;

import java.util.ResourceBundle;

import data.Game;
import data.GameData;
import exceptions.InvalidGameException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import player.MediaManager;
import player.launchers.FullPlayer;
import polyglot.Case;
import polyglot.Polyglot;

/**
 * Menu from which user launches the game
 * 
 * @author Jesse
 *
 */
public class LoadMenu extends AbstractMenu {

	private ObservableList<String> saveStates;
	private ObservableList<Tile> saveTiles;
	private Tile playTile;

	public LoadMenu(Stage stage, Game game, MediaManager mediaManager, Polyglot polyglot, ResourceBundle IOResources) {
		super(stage, game, mediaManager, null, polyglot, IOResources);
		saveStates = mediaManager.getSaves();
		mediaManager.setSaves(saveStates);
		saveTiles = FXCollections.observableArrayList();
		saveTiles.add(playTile);
		setupSaveTiles(stage, mediaManager);
		addSaveTiles(true, saveTiles);
		setupScene(stage);
	}

	private void setupSaveTiles(Stage stage, MediaManager mediaManager) {
		for (int i = 0; i < mediaManager.getSaves().size(); i++) {
			final int j = i;
			Tile game = new Tile(getPolyglot().get(Integer.toString(i + 1), Case.TITLE), "blue",
					e -> loadSaveState(stage, saveStates.get(j)));
			saveTiles.add(game);
		}
	}

	private void loadNewGame(Stage stage) {
		new FullPlayer(stage, this.getGame(), this.getMediaManager(), this.getPolyglot(), this.getResources(), true);
	}

	private void loadSaveState(Stage stage, String saveName) {
		GameData data = new GameData();
		try {
			Game game = data.loadGameState(this.getMediaManager().getGamePath(), saveName);
			MediaManager mediaManager = new MediaManager(game, this.getMediaManager().getGamePath());
			new FullPlayer(stage, game, mediaManager, this.getPolyglot(), this.getResources(), false);
		} catch (Exception e) {
			new InvalidGameException();
		}
	}

	private void setupScene(Stage stage) {

		saveStates.addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c) {
				if (saveStates.size() < 11) {
					Tile save = new Tile(getPolyglot().get(Integer.toString(saveStates.size()), Case.TITLE), "blue",
							e -> loadSaveState(stage, saveStates.get(saveStates.size() - 1)));
					saveTiles.add(save);
					addSaveTiles(true, saveTiles);
				} else {
					ReplaceSaveMenu replacer = new ReplaceSaveMenu(getPolyglot(), getResources());
					replacer.display(e -> {
						int index = replacer.getButtonID();
						// Changes button action to load new save
						saveTiles.get(index).setOnAction(e1 -> {
							loadSaveState(stage, saveStates.get(saveStates.size() - 1));
						});
						replacer.close();
					});
				}

			}
		});
	}

	@Override
	public void addElements() {
		playTile = new Tile(getPolyglot().get("NewGameButton", Case.TITLE), "red", e -> loadNewGame(getStage()));
		addTiles(true, playTile);
	}
}
