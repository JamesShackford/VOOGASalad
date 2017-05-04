package player.launchers;

import java.util.ResourceBundle;

import authoring.components.ComponentMaker;
import data.Game;
import engine.game.gameloop.Scorebar;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import player.MediaManager;
import player.menus.EndGameMenu;
import polyglot.Polyglot;

/**
 * This is the Full version of the Player, the primary differences being the
 * MediaManager and the control bar at the top of the screen. The control bar
 * allows the user to play, pause, restart, save, and exit the game. The
 * MediaMananger helps makes it possible to save game progress and have
 * background music for the game. Additionally, this version of the Player will
 * save high scores when the game is won.
 * 
 * @author Jay Doherty
 * @author Jesse
 */
public class FullPlayer extends AbstractPlayer {

	private static final int CONTROLS_HEIGHT = 42;

	private Button playButton;
	private boolean isPaused;
	private ImageView playImage;
	private ImageView pauseImage;

	private MediaManager mediaManager;

	public FullPlayer(Stage primaryStage, Game game, MediaManager mediaManager, Polyglot polyglot,
			ResourceBundle IOResources, boolean firstTimeLoading) {
		super(primaryStage, game, polyglot, IOResources, firstTimeLoading);
		this.mediaManager = mediaManager;
		this.buildControlBar();
		this.playSong();

		this.togglePlayPause(true);
	}

	private void buildControlBar() {
		ComponentMaker factory = this.getFactory();

		pauseImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream(this.getResources().getString("PausePath"))));
		playImage = new ImageView(
				new Image(getClass().getClassLoader().getResourceAsStream(this.getResources().getString("PlayPath"))));
		playButton = factory.makeImageButton("PauseButtonText", pauseImage, e -> this.togglePlayPause(isPaused), true);
		playButton.setPrefHeight(CONTROLS_HEIGHT);

		ImageView restartImage = new ImageView(new Image(
				getClass().getClassLoader().getResourceAsStream(this.getResources().getString("RestartPath"))));
		Button restartButton = factory.makeImageButton("RestartButtonText", restartImage, e -> this.restart(), true);
		restartButton.setPrefHeight(CONTROLS_HEIGHT);

		Button saveButton = factory.makeButton("SaveButtonText", e -> this.save(), true);
		saveButton.setPrefHeight(CONTROLS_HEIGHT);

		Button exitButton = factory.makeButton("ExitButtonText", e -> this.exit(), true);
		exitButton.setPrefHeight(CONTROLS_HEIGHT);

		ToolBar toolbar = new ToolBar(playButton, restartButton, saveButton, exitButton);
		this.setTop(toolbar);
	}

	private void togglePlayPause(boolean play) {
		if (play) {
			isPaused = false;
			playButton.textProperty().bind(this.getPolyglot().get("PauseButtonText"));
			playButton.setGraphic(this.pauseImage);
			this.getRunningGameLoop().startTimeline();
			mediaManager.playSong();

		} else {
			isPaused = true;
			playButton.textProperty().bind(this.getPolyglot().get("PlayButtonText"));
			playButton.setGraphic(this.playImage);
			this.getRunningGameLoop().pauseTimeline();
			mediaManager.pauseSong();
		}
	}

	private void restart() {
		this.getRunningGameLoop().pauseTimeline();
		this.buildGameView(true);
		this.buildControlBar();
		this.togglePlayPause(true);

		getRunningGameLoop().getLevelManager().clearUnlockedLevels();
		getRunningGameLoop().getLevelManager().addUnlockedLevel(1);
	}

	private void playSong() {
		mediaManager.playSong();
	}

	private void save() {
		Game savedGame = this.getGame().clone();
		savedGame.setLevels(this.getRunningGameLoop().getLevelManager().getLevels().getListRepresentation());
		savedGame.setLevels(savedGame.cloneLevels());
		savedGame.setNumberOfLives(this.getRunningGameLoop().getScorebar().getLives());
		savedGame.setUnlockedLevels(this.getRunningGameLoop().getLevelManager().getUnlockedLevelNumbers());
		mediaManager.saveGame(savedGame);
	}

	protected void exit() {
		super.exit();
		mediaManager.pauseSong();
	}

	@Override
	public void endGame(Scorebar scorebar) {
		this.getStage().setScene(new EndGameMenu(this.getStage(), this.getGame(), mediaManager, this.getPolyglot(),
				this.getResources(), scorebar).createScene());
		this.getStage().centerOnScreen();
	}
}
