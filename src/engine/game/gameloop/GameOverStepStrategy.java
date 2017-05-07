package engine.game.gameloop;

import engine.GameInfo;
import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * TranstionStepStrategy for when a game over occurs.
 * 
 * @author Matthew Barbano
 *
 */
public class GameOverStepStrategy extends TransitionStepStrategy {
	private static final String RESOURCE_NAME = "GameOver";

	private LevelManager levelManager;
	private GameInfo info;
	
	/**
	 * Instantiates by calling super()
	 * @param levelManager
	 * @param info
	 */
	public GameOverStepStrategy(LevelManager levelManager, GameInfo info) {
		super(RESOURCE_NAME);
		this.levelManager = levelManager;
		this.info = info;
	}
	
	/**
	 * Returns -1 since game overs do not progress to a new level.
	 */
	@Override
	protected int nextLevelNumber() {
		return -1;
	}
	
	/**
	 * Returns false since game overs do not lead to high scores.
	 */
	@Override
	protected boolean handleHighscore(GraphicsEngine graphicsEngine) {
		return false;
	}
	
	/**
	 * Re-locks all levels except the first, and resets lives to the maximum.
	 */
	@Override
	protected void modifyUnlockedScreens() {
		levelManager.clearUnlockedLevels();
		levelManager.addUnlockedLevel(1);
		info.getScorebar().resetLives();
	}
	
	/**
	 * Returns a new LevelSelectionStepStrategy.
	 */
	@Override
	protected StepStrategy nextStrategyLevelSelectionMode() {
		return new LevelSelectionStepStrategy(false);
	}
}
