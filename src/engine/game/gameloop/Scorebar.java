package engine.game.gameloop;

import data.Game;
import engine.entities.entities.CharacterEntity;
import engine.game.LevelManager;
import engine.game.timer.TimerManager;

/**
 * Contains information displayed on the Scorebar.
 * 
 * @author Jay Doherty
 * @author Matthew Barbano
 *
 */
public class Scorebar {
	private LevelManager levelManager;
	private TimerManager timerManager;
	private int score;
	private Game game;
	private int lives;
	private int initialLives;
	
	/**
	 * Instantiates with all fields set to dummy values, except game.
	 * @param game
	 */
	public Scorebar(Game game) {
		this.timerManager = null;
		this.game = game;
		score = 0;
		levelManager = new LevelManager(game, null, null);
	}
	
	/**
	 * Sets the number of lives appropriately.
	 * @param levelManager
	 * @param firstTimeLoading
	 */
	public void setupLives(LevelManager levelManager, boolean firstTimeLoading) {
		levelManager.getCurrentLevel().getEntities().stream().filter(s -> s instanceof CharacterEntity)
				.forEach(s -> initialLives = s.getLives());
		if (firstTimeLoading)
			lives = initialLives;
	}
	
	/**
	 * 
	 * @return lives
	 */
	public int getLives() {
		return lives;
	}
	
	/**
	 * Sets number of lives to lives
	 * @param lives
	 */
	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * Sets lives to the initial number default
	 */
	public void resetLives() {
		lives = initialLives;
	}

	/**
	 * Sets levelManager to levelManager
	 * @param levelManager
	 */
	public void setLevelManager(LevelManager levelManager) {
		this.levelManager = levelManager;
	}
	
	/**
	 * Restarts the timer from its initial time
	 */
	public void resetTimerManager() {
		timerManager.reset();
	}
	
	/**
	 * Returns the time as a nicely formatted String
	 * @return
	 */
	public String getTime() {
		return timerManager.toString();
	}
	
	/**
	 * Returns the time in milliseconds
	 * @return
	 */
	public int getTimeValue() {
		return timerManager.getMilliseconds();
	}
	
	/**
	 * Returns the TimerManager
	 * @return
	 */
	public TimerManager getTimerManager() {
		return timerManager;
	}
	
	/**
	 * Sets the TimerManager (needed as progress to new levels)
	 * @param timerManager
	 */
	public void setTimerManager(TimerManager timerManager) {
		this.timerManager = timerManager;
	}

	/**
	 * Returns the score
	 * @return
	 */
	public String getScore() {
		return convertScore(score);
	}
	
	/**
	 * Sets the score
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * 
	 * @param scoreChange
	 *            amount of points by which the current score will change
	 */
	public void updateScore(int scoreChange) {
		this.score += scoreChange;
	}
	
	/**
	 * Returns the current level number
	 * @return
	 */
	public int getLevel() {
		return levelManager.getLevelNumber();
	}
	
	/**
	 * Saves the final score
	 * @param name
	 */
	public void saveFinalScore(String name) {
		game.setHighscores(getScore(), getTime(), getTimeValue(), name);
	}
	
	/**
	 * Converts the score to a nicely formatted String
	 * @param score
	 * @return
	 */
	private String convertScore(int score) {
		return String.format("%06d", score);
	}

	/**
	 * Checks if the score is a highscore and returns a boolean accordingly
	 * @return
	 */
	public boolean isHighscore() {
		return game.isHighscore(getScore(), getTimeValue(), 9);
	}
}
