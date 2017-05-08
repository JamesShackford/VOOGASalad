package engine.game.gameloop;

import data.Game;
import engine.GameInfo;
import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Manages the highest level of time flow and Timeline logic in the game. The client class for the
 * game loop. Constructor called from the Game Player whenever a new game needs 
 * to be setup, and startTimeline() when ready for the Game Player to start the game animation.
 * See method descriptions for assumptions; dependencies include ObservableBundle, Scorebar
 * TimelineManipulator, LevelManager, and GraphicsEngine. Example of use:
 * 
 * <pre>
 * GameLoop loop = new GameLoop();
 * loop.startTimeline();
 * </pre>
 * 
 * @author Matthew Barbano
 *
 */

public class GameLoop {
	private ObservableBundle observableBundle;
	private Scorebar scorebar;
	private TimelineManipulator timelineManipulator;
	private LevelManager levelManager;
	private GraphicsEngine graphicsEngine;
	
	/**
	 * Sets up fields and objects at the beginning of every game. Note that this is called
	 * every time an entire game is loaded from the Game Player, whether it be a new game
	 * or a saved game. Sets up the Observable Bundle, LevelManager (if a new game, sets the 
	 * unlocked levels to be only the first; if saved, sets it to be from the saved game), GameInfo,
	 * first Screen, and TimelineManipulator.
	 * 
	 * @param gameScene
	 * @param game
	 * @param graphicsEngine
	 * @param firstTimeLoading
	 */
	public GameLoop(Scene gameScene, Game game, GraphicsEngine graphicsEngine, boolean firstTimeLoading) {
		this.graphicsEngine = graphicsEngine;
		scorebar = graphicsEngine.getScorebar();
		observableBundle = new ObservableBundle(gameScene, graphicsEngine);

		levelManager = new LevelManager(game, new LevelStepStrategy(), scorebar);
		levelManager.loadAllSavedLevels(firstTimeLoading);
		if (levelManager.getLevels().size() > 0) {
			levelManager.addUnlockedLevel(1);
		}
		setupFirstStrategy();
		timelineManipulator = new TimelineManipulator(levelManager);
		GameInfo info = new GameInfo(this);
		Screen firstScreen = new Screen(levelManager, graphicsEngine, info, true);
		levelManager.setCurrentScreen(firstScreen);
		timelineManipulator.setInfo(info);
		graphicsEngine.getScorebar().setLevelManager(levelManager);
		scorebar.setupLives(levelManager, firstTimeLoading);
	}

	private void setupFirstStrategy() {
		StepStrategy firstStrategy = levelManager.getLevelSelectionScreenMode() ? new LevelSelectionStepStrategy(true)
				: new LevelStepStrategy();
		levelManager.setCurrentStepStrategy(firstStrategy);
	}
	
	/**
	 * Called by the Game Player when initialization is complete and animation is to be played,
	 * or when the game should be unpaused.
	 */
	public void startTimeline() {
		levelManager.getCurrentScreen().start();
	}
	
	/**
	 * Called by the Game Player when the pause button is pressed.
	 */
	public void pauseTimeline() {
		levelManager.getCurrentScreen().pause();
	}
	
	/**
	 * @return game view from graphics engine
	 */
	public Pane getGameView() {
		return graphicsEngine.getView();
	}
	
	/**
	 * @return ObservableBundle
	 */
	public ObservableBundle getObservableBundle() {
		return observableBundle;
	}
	
	/**
	 * @return scorebar for current game
	 */
	public Scorebar getScorebar() {
		return scorebar;
	}
	
	/**
	 * @return timelineManipulator for current game (helper class)
	 */
	public TimelineManipulator timelineManipulator() {
		return timelineManipulator;
	}
	
	/**
	 * @return levelManager for current game
	 */
	public LevelManager getLevelManager() {
		return levelManager;
	}
	
	/**
	 * @return graphicsEngine for current game
	 */
	public GraphicsEngine getGraphicsEngine() {
		return graphicsEngine;
	}
}
