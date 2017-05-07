package engine.game.gameloop;

import engine.GameInfo;
import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * StepStrategy for transition screen displaying messages like "Game Over" or
 * "You won" (read from a properties file) between other screens. Is the
 * superclass of the inheritance hierarchy whose subclasses correspond to
 * individual types of transitions. Note that two game "modes" can be
 * substituted: one with and another without a Level Selection Screen. This
 * class has code for setting the next screen for both modes, though only the
 * former is used by the Authoring Environment currently (could be extended to
 * use both). Assumes that has setup() and step() called from Screen.
 * Dependencies are LevelManager, GraphicsEngine, and GameInfo.
 * 
 * @author Matthew Barbano
 *
 */
public abstract class TransitionStepStrategy implements StepStrategy {
	private static final int FRAME_DURATION = 150;
	private int frameNumber = 1;
	private String imageKey;
	private LevelManager levelManager;
	private GraphicsEngine graphicsEngine;
	private GameInfo info;

	/**
	 * Instantiates this object and sets the imageKey String corresponding to
	 * the text image to show.
	 * 
	 * @param imageKey
	 */
	public TransitionStepStrategy(String imageKey) {
		this.imageKey = imageKey;
	}

	/**
	 * Sets up this StepStrategy by setting its fields to the arguments given,
	 * and calling the Graphics Engine for the Game Player to show the image
	 * specified by the imageKey.
	 */
	@Override
	public void setup(LevelManager levelManager, GraphicsEngine graphicsEngine, GameInfo info) {
		this.levelManager = levelManager;
		this.graphicsEngine = graphicsEngine;
		this.info = info;
		graphicsEngine.showImage(imageKey);
	}

	/**
	 * Executes the appropriate action on every step of the Timeline - in this
	 * case, incrementing frameNumber and testing to see if all of the required
	 * frames have completed. In effect, makes the screen appear for a set time
	 * determined by FRAME_DURATION.
	 */
	@Override
	public void step() {
		if (frameNumber == FRAME_DURATION && levelManager.getLevelSelectionScreenMode()) {
			nextScreenLevelSelectionMode();
		} else if (frameNumber == FRAME_DURATION) {
			nextScreenJustLevelsMode();
		}
		frameNumber++;
	}

	/**
	 * Returns the level number associated with the next screen to display. If
	 * the next screen is a game over, returns -1.
	 * 
	 * @return the next level number
	 */
	protected abstract int nextLevelNumber();

	/**
	 * Calls the high score screen in the Game Player if a high score has been
	 * achieved. If this occurs, returns true. If not appropriate (ex: game
	 * over) or a high score has not been achieved, returns false.
	 * 
	 * @param graphicsEngine
	 * @return if a high score has been achieved
	 */
	protected abstract boolean handleHighscore(GraphicsEngine graphicsEngine);

	/**
	 * Unlocks or locks Levels by calling methods in levelManager based on the
	 * type of transition screen.
	 */
	protected abstract void modifyUnlockedScreens();

	/**
	 * Returns the next StepStrategy to be run with the next Screen upon
	 * completion of this TransitionStepStrategy. Assumes is only called when in
	 * the "mode" WITH the Level Selection Screen; otherwise, next StepStrategy
	 * is always a LevelStepStrategy().
	 * 
	 * @return next StepStrategy
	 */
	protected abstract StepStrategy nextStrategyLevelSelectionMode();

	/**
	 * Stops the current Timeline, calls modifyUnlockedLevels(), and sets up the
	 * next screen, playing its Timeline. Exemplifies the Template Design
	 * Pattern. Called from step() when the transtion screen should terminate.
	 * Assumes is only called when in the "mode" WITH the Level Selection
	 * Screen; otherwise, next StepStrategy is always a LevelStepStrategy().
	 */
	private void nextScreenLevelSelectionMode() {
		stopCurrentTimeline();
		modifyUnlockedScreens();
		if (!handleHighscore(graphicsEngine)) {
			nextScreenAndStrategy(nextStrategyLevelSelectionMode());
		}
	}

	/**
	 * Stops the current Timeline and sets up the next screen, playing its
	 * Timeline. Does NOT modify unlocked levels. Called from step() when the
	 * transtion screen should terminate.
	 * 
	 * Assumes is only called when in the "mode" WITHOUT the Level Selection
	 * Screen; otherwise, next StepStrategy is always a LevelStepStrategy().
	 */
	private void nextScreenJustLevelsMode() {
		stopCurrentTimeline();
		if (!handleHighscore(graphicsEngine) && levelManager.setLevelNumber(nextLevelNumber())) {
			nextScreenAndStrategy(new LevelStepStrategy());
		}
	}

	/**
	 * Helper method to stop the Timeline.
	 */
	private void stopCurrentTimeline() {
		levelManager.getCurrentScreen().getTimeline().stop();
	}

	/**
	 * Helper method to set the next StepStrategy in LevelManager, instantiate
	 * the next screen, and call play() on its Timeline.
	 * 
	 * @param nextStepStrategy
	 */
	private void nextScreenAndStrategy(StepStrategy nextStepStrategy) {
		levelManager.setCurrentStepStrategy(nextStepStrategy);
		Screen nextScreen = new Screen(levelManager, graphicsEngine, info, false);
		nextScreen.getTimeline().play();
	}

}