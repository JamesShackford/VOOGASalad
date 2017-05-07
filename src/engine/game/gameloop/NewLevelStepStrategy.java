package engine.game.gameloop;

import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * TransitionStepStrategy for when the hero wins the level and needs
 * to progress to the next unlocked level via a LevelSwitchAction.
 * 
 * @author Matthew Barbano
 *
 */
public class NewLevelStepStrategy extends TransitionStepStrategy {
	private static final String RESOURCE_NAME_REGULAR_WIN = "Win";
	private static final String RESOURCE_NAME_LAST_WIN = "WinGame";
	private LevelManager levelManager;
	private int newLevel;
	
	/**
	 * Instantiates via a call to super().
	 * @param levelManager
	 * @param newLevel
	 */
	public NewLevelStepStrategy(LevelManager levelManager, int newLevel) {
		super(levelManager.getLevelNumber() == levelManager.getLevels().size() ? RESOURCE_NAME_LAST_WIN : RESOURCE_NAME_REGULAR_WIN);
		this.levelManager = levelManager;
		this.newLevel = newLevel;
	}
	
	/**
	 * Returns the new level specified by the Action.
	 */
	@Override
	protected int nextLevelNumber() {
		return newLevel;
	}
	
	/**
	 * Checks if there is a high score and if so, displays that screen in the Game Player.
	 * 
	 * @return if there is a high score
	 */
	@Override
	protected boolean handleHighscore(GraphicsEngine graphicsEngine) {
		boolean handled = levelManager.getLevelNumber() == levelManager.getLevels().size() && graphicsEngine.getScorebar().isHighscore();
		if(handled){
			graphicsEngine.endGame();
		}
		return handled;
	}
	
	/**
	 * Unlocks the new level specified by the Action.
	 */
	@Override
	protected void modifyUnlockedScreens() {
		levelManager.addUnlockedLevel(newLevel);
	}
	
	/**
	 * Returns a new LevelSelectionStepStrategy with parameter false.
	 */
	@Override
	protected StepStrategy nextStrategyLevelSelectionMode() {
		return new LevelSelectionStepStrategy(false);
	}
}