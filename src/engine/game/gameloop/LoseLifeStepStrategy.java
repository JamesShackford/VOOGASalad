package engine.game.gameloop;

import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * TransitionStepStrategy subclass for when the hero loses a life (but does not
 * have a game over). See TransitionStepStrategy for more information.
 * 
 * @author Matthew Barbano
 * @see TransitionStepStrategy
 */
public class LoseLifeStepStrategy extends TransitionStepStrategy {
	private static final String RESOURCE_NAME = "LivesLeft";
	private LevelManager levelManager;

	/**
	 * Instantiates the superclass with parameter RESOURCE_NAME corresponding to
	 * the text displayed's key in Strings.properties.
	 */
	public LoseLifeStepStrategy(LevelManager levelManager) {
		super(RESOURCE_NAME);
		this.levelManager = levelManager;
	}

	/**
	 * Returns the current level number, since losing a life reinitializes the
	 * current level.
	 * 
	 * @return current level number
	 */
	@Override
	protected int nextLevelNumber() {
		return levelManager.getLevelNumber();
	}

	/**
	 * Returns false, since it does not make sense to have a high score upon
	 * losing a life.
	 * 
	 * @return false
	 */
	@Override
	protected boolean handleHighscore(GraphicsEngine graphicsEngine) {
		return false;
	}

	/**
	 * Intentionally left blank as part of the Null Object Design Pattern, since
	 * the unlocked levels do not change when the hero dies (assuming not game
	 * over).
	 */

	@Override
	protected void modifyUnlockedScreens() {
	}

	/**
	 * Returns a new LevelStepStrategy, since the next screen to appear with be
	 * the same reinitialized level.
	 */

	@Override
	protected StepStrategy nextStrategyLevelSelectionMode() {
		return new LevelStepStrategy();
	}
}
