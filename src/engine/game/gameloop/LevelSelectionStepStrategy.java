package engine.game.gameloop;

import engine.GameInfo;
import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * Subclass of StepStrategy implementing step() when a screen of buttons for
 * selecting which level to play should be displayed.
 * 
 * @author Matthew Barbano
 *
 */
public class LevelSelectionStepStrategy implements StepStrategy {
	private LevelManager levelManager;
	private GraphicsEngine graphicsEngine;
	private GameInfo info;
	private boolean firstPass;
	
	/**
	 * Instantiates with firstPass set to parameter.
	 * @param firstPass
	 */
	public LevelSelectionStepStrategy(boolean firstPass) {
		this.firstPass = firstPass;
	}
	
	/**
	 * Sets up the field, displays level selection screen, and blanks the scorebar
	 * if firstPass is true.
	 */
	@Override
	public void setup(LevelManager levelManager, GraphicsEngine graphicsEngine, GameInfo info) {
		this.levelManager = levelManager;
		this.graphicsEngine = graphicsEngine;
		this.info = info;
		graphicsEngine.displayLevelSelectionScreen(levelManager, this);
		graphicsEngine.blankScorebar(firstPass);
	}
	
	/**
	 * Does nothing (Null Object Design Pattern).
	 */
	@Override
	public void step() {
	}
	
	/**
	 * Progresses to the selected next level.
	 * @param levelNumber
	 */
	public void moveToLevelScreen(int levelNumber) {
		levelManager.getCurrentScreen().getTimeline().stop();
		boolean hasSelectedLevel = levelManager.setLevelNumber(levelNumber);
		if (hasSelectedLevel) {
			StepStrategy nextStepStrategy = new LevelStepStrategy();
			levelManager.setCurrentStepStrategy(nextStepStrategy);
			Screen nextScreen = new Screen(levelManager, graphicsEngine, info, false);
			nextScreen.getTimeline().play();
		}
	}
}
