package engine.game.gameloop;

import engine.GameInfo;
import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * The interface for the Strategy Design Pattern whose subclasses can be
 * substituted for different implementations of Screen's step() (and setup()).
 * Assumes that a Screen will hold this StepStrategy, and dependencies are
 * LevelManager, GraphicsEngine, and GameInfo. Example code:
 * 
 * <pre>
 * StepStrategy strategy = new LevelStepStrategy();
 * From Screen's constructor: strategy.setup(levelManager, graphicsEngine, info);
 * In Screen's step(): strategy.step();
 * </pre>
 * 
 * @author Matthew Barbano
 *
 */

public interface StepStrategy {
	/**
	 * Assumed to be called from Screen's constructor to conduct appropriate set
	 * up before this StepStrategy starts stepping. This method should be used
	 * for setup rather than a constructor so the StepStrategy can be
	 * instantiated anywhere, whereas it is more predictable when Screen's
	 * constructor is called, which in turn calls this method.
	 * 
	 * @param levelManager,
	 *            graphicsEngine, info for setting up
	 */

	public void setup(LevelManager levelManager, GraphicsEngine graphicsEngine, GameInfo info);

	/**
	 * Assumed to be called from Screen's step() method on every iteration of
	 * the Timeline. Completes all the Game Engine updates appropriate.
	 */

	public void step();
}