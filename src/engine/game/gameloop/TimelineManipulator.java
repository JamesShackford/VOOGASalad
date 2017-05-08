package engine.game.gameloop;

import engine.GameInfo;
import engine.game.LevelManager;

/**
 * Allows Game Objects (Entities, Events, Actions) to manipulate the timeline,
 * including ending levels (DieAction, NextLevelAction), and pausing the
 * timeline. Helper class.
 * 
 * @author Matthew Barbano
 *
 */

public class TimelineManipulator {
	private LevelManager levelManager;
	private GameInfo info;
	
	/**
	 * Sets levelManager to argument.
	 * @param levelManager
	 */
	public TimelineManipulator(LevelManager levelManager) {
		this.levelManager = levelManager;
	}
	
	/**
	 * Sets info
	 * @param info
	 */
	public void setInfo(GameInfo info) {
		this.info = info;
	}
	
	/**
	 * Starts the current screen
	 */
	public void start() {
		levelManager.getCurrentScreen().start();
	}
	
	/**
	 * Pauses the current screen
	 */
	public void pause() {
		levelManager.getCurrentScreen().pause();
	}
	
	/**
	 * Starts any new level in the game. Called from LevelSwitchAction.
	 * @param newLevel
	 */
	public void startNewLevel(int newLevel) {
		levelManager.getCurrentLevel().getEntities().stream()
				.forEach(s -> info.getObservableBundle().detachEntityFromAll(s));
		moveToNextScreen(new NewLevelStepStrategy(levelManager, newLevel));
	}

	/**
	 * Logic for ending this level screen when won the level. IMPORTANT: Called
	 * from LevelNextAction (and can be called from other Actions), NOT from
	 * step(). Stops this screen's timeline, instantiates the next screen with a
	 * NextLevelStepStrategy, and starts that timeline. Although this method
	 * uses a Timeline, it is specific to Level Screens, so I put it here in
	 * LevelStepStrategy rather than in Screen.
	 */

	public void startNextLevel() {
		levelManager.getCurrentLevel().getEntities().stream()
				.forEach(s -> info.getObservableBundle().detachEntityFromAll(s));
		moveToNextScreen(new NextLevelStepStrategy(levelManager));
	}

	/**
	 * Logic for ending this level screen when you die. IMPORTANT: Called from
	 * DieAction (and can be called from other Actions), NOT from step(). Stops
	 * this screen's timeline, instantiates the next screen with a
	 * TransitionStepStrategy appropriate to whether there is a gameOver, and
	 * starts that timeline. Although this method uses a Timeline, it is
	 * specific to Level Screens, so I put it here in LevelStepStrategy rather
	 * than in Screen.
	 * 
	 * @param gameOver
	 */

	public void die(boolean gameOver) {
		levelManager.getCurrentLevel().getEntities().stream()
				.forEach(s -> info.getObservableBundle().detachEntityFromAll(s));
		StepStrategy nextStepStrategy;
		if (gameOver)
			nextStepStrategy = new GameOverStepStrategy(levelManager, info);
		else
			nextStepStrategy = new LoseLifeStepStrategy(levelManager);
		moveToNextScreen(nextStepStrategy);
	}
	
	/**
	 * Stops the current timeline and flags that the screen is finished.
	 * @param nextStepStrategy
	 */
	private void moveToNextScreen(StepStrategy nextStepStrategy) {
		levelManager.getCurrentScreen().getTimeline().stop();
		((LevelStepStrategy) levelManager.getCurrentStepStrategy()).flagScreenFinished(nextStepStrategy);
	}
}
