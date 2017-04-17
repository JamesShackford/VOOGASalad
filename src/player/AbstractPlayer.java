package player;

import java.io.File;
import java.util.ResourceBundle;

import engine.game.gameloop.GameLoop;
import game_data.Game;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public abstract class AbstractPlayer extends BorderPane {

	private ResourceBundle resources = ResourceBundle.getBundle("resources/Player");
	private String stylesheetPath = resources.getString("StylesheetPath");
	private MediaPlayer songPlayer;
	
	private Game game;
	private GameLoop gameLoop;
	private Stage stage;
	private Scene scene;
	private String path;

	public AbstractPlayer(Game game, String dataFolderPath) {
		this.game = game;
		path = dataFolderPath;
		playSong();
		buildStage();
		buildGameView();
		gameLoop.startTimeline();
	}

	private void buildStage() {
		stage = new Stage();
		stage.setTitle(resources.getString("PlayerTitle"));
		stage.setMinWidth(600);
		stage.setMinHeight(600);
		stage.setOnCloseRequest(e -> this.exit());

		scene = new Scene(this, 600, 600);
		scene.getStylesheets().add(stylesheetPath);

		stage.setScene(scene);
		stage.show();
	}

	protected void buildGameView() {
		if (!path.equals("")) {
			gameLoop = new GameLoop(scene, game);
			Overlay scorebar = gameLoop.getGameScorebar();
			StackPane pane = new StackPane();
			pane.getChildren().addAll(gameLoop.getGameView(), scorebar.display());
			this.setCenter(pane);
		}
	}
	
	private void playSong() {
		try {
			String path = game.getSongPath();
			String uriString = new File(path).toURI().toString();
			songPlayer = new MediaPlayer(new Media(uriString));
			songPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			songPlayer.play();
		} catch (Exception e) {

		}
	}
	
	protected void exit() {
		if (!path.equals("")) {
			gameLoop.pauseTimeline();
			if (songPlayer != null)
				songPlayer.pause();
		}
		stage.close();
	}
	
	protected GameLoop getRunningGameLoop() {
		return this.gameLoop;
	}
}