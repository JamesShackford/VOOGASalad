package engine.game.gameloop;

import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * TransitionStepStrategy for when the hero wins the level and needs to progress
 * to the next unlocked level via a LevelNextAction.
 * 
 * @author Matthew Barbano
 *
 */
public class NextLevelStepStrategy extends TransitionStepStrategy {
	private static final String RESOURCE_NAME_REGULAR_WIN = "Win";
	private static final String RESOURCE_NAME_LAST_WIN = "WinGame";
	private LevelManager levelManager;
	
	/**
	 * Instantiates via a call to super()
	 * @param levelManager
	 */
	public NextLevelStepStrategy(LevelManager levelManager) {
		super(levelManager.getLevelNumber() == levelManager.getLevels().size() ? RESOURCE_NAME_LAST_WIN
				: RESOURCE_NAME_REGULAR_WIN);
		this.levelManager = levelManager;
	}
	
	/**
	 *  Returns the currnet level number + 1
	 */
	@Override
	protected int nextLevelNumber() {
		return levelManager.getLevelNumber() + 1;
	}
	
	/**
	 * Checks if there is a high score and if so, displays that screen in the Game Player.
	 * 
	 * @return if there is a high score
	 */
	@Override
	protected boolean handleHighscore(GraphicsEngine graphicsEngine) {
		boolean handled = levelManager.getLevelNumber() == levelManager.getLevels().size()
				&& graphicsEngine.getScorebar().isHighscore();
		if (handled) {
			graphicsEngine.endGame();
		}
		return handled;
	}
	
	/**
	 * Unlocks the next level
	 */
	@Override
	protected void modifyUnlockedScreens() {
		levelManager.addUnlockedLevel(levelManager.getLevelNumber() + 1);
	}
	
	/**
	 * Returns a new LevelSelectionStepStrategy with parameter false.
	 */
	@Override
	protected StepStrategy nextStrategyLevelSelectionMode() {
		return new LevelSelectionStepStrategy(false);
	}
}
