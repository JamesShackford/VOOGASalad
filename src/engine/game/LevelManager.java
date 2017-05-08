package engine.game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import data.Game;
import engine.entities.Entity;
import engine.entities.entities.AchievementEntity;
import engine.game.gameloop.Scorebar;
import engine.game.gameloop.Screen;
import engine.game.gameloop.StepStrategy;
import engine.game.selectiongroup.ListSG;
import engine.game.selectiongroup.SelectionGroup;
import engine.game.timer.TimerManager;

/**
 * Holds all the levels in the current game and allows for game-wide behavior.
 * 
 * IMPORTANT NOTE TO ANYONE USING THIS CLASS: When using the methods in this
 * class, levels are one-indexed (likely the only information anyone else will
 * need to know). If for some reason anyone needs to use SelectionGroup, that is
 * zero-indexed. Note that SelectionGroup was originally intended to be extended
 * to a graph implementation (in addition to a List implementation) and still can
 * be!
 * 
 * @author Matthew Barbano
 *
 */
public class LevelManager {
	private SelectionGroup<Level> levels;
	private SelectionGroup<Level> levelsInInitialState;
	private Set<Integer> unlockedLevelNumbers;
	private int currentLevel;
	private final Game game;
	private Screen currentScreen;
	private StepStrategy currentStepStrategy;
	private boolean levelSelectionScreenMode;
	private Scorebar scorebar;
	
	/**
	 * Instantiates the LevelManager with the fields as arguments set, and all other
	 * fields set to their Java default values, and levelSelectionScreenMode to true (can
	 * be extended to be specified by the Authoring Environment).
	 * 
	 * @param game
	 * @param currentStepStrategy
	 * @param scorebar
	 */
	public LevelManager(Game game, StepStrategy currentStepStrategy, Scorebar scorebar) {
		levels = new ListSG<>();
		levelsInInitialState = new ListSG<>();
		unlockedLevelNumbers = new HashSet<>();
		currentLevel = 1;
		this.game = game;
		this.currentStepStrategy = currentStepStrategy;
		this.levelSelectionScreenMode = true;
		this.scorebar = scorebar;
	}
	
	/**
	 * @return levelSelectionScreenMode
	 */
	public boolean getLevelSelectionScreenMode() {
		return levelSelectionScreenMode;
	}
	
	/**
	 * Sets levelSelectionScreenMode
	 * @param levelSelectionScreenMode
	 */
	public void setLevelSelectionScreenMode(boolean levelSelectionScreenMode) {
		this.levelSelectionScreenMode = levelSelectionScreenMode;
	}
	
	/**
	 * @return currentScreen
	 */
	public Screen getCurrentScreen() {
		return currentScreen;
	}
	
	/**
	 * Sets the currentScreen
	 * @param currentScreen
	 */
	public void setCurrentScreen(Screen currentScreen) {
		this.currentScreen = currentScreen;
	}
	
	/**
	 * Gets the currentStepStrategy
	 * @return
	 */
	public StepStrategy getCurrentStepStrategy() {
		return currentStepStrategy;
	}
	
	/**
	 * Sets the currentStepStrategy
	 * @param currentStepStrategy
	 */
	public void setCurrentStepStrategy(StepStrategy currentStepStrategy) {
		this.currentStepStrategy = currentStepStrategy;
	}

	/**
	 * External Engine API. Needed for authoring. Adds the level specified.
	 * 
	 * @param level
	 * @return
	 */
	public void addLevel(Level level) {
		levels.add(level);
	}

	/**
	 * External Engine API. Needed for gameplay.
	 * 
	 * @return currently active level
	 */
	public Level getCurrentLevel() {
		return levels.get(currentLevel - 1);
	}

	/**
	 * Returns true if in range and successfully set level. Otherwise, returns
	 * false and currentLevel remains unchanged.
	 * 
	 * @param currentLevel
	 * @return
	 */
	public boolean setLevelNumber(int currentLevel) {
		if (levelNumberInGame(currentLevel)) {
			this.currentLevel = currentLevel;
		}
		return levelNumberInGame(currentLevel);
	}
	
	/**
	 * Checks if queriedLevel exists in the game.
	 * @param queriedLevel
	 * @return
	 */
	public boolean levelNumberInGame(int queriedLevel) {
		return queriedLevel >= 1 && queriedLevel <= levels.size();
	}
	
	/**
	 * Number of the current level
	 * @return
	 */
	public int getLevelNumber() {
		return currentLevel;
	}

	/**
	 * Loads all the saved levels to the Game Engine/game loop.
	 * Since never save levels' state during gameplay, can call this method at
	 * any point during game loop to get levels' initial states.
	 */

	public void loadAllSavedLevels(boolean firstTimeLoading) {
		List<Entity> achievements = game.getAchievements();
		List<Level> cloneLevels = game.cloneLevels();
		cloneLevels.forEach(s -> s.addEntities(achievements));
		levelsInInitialState.addAll(cloneLevels);
		List<Level> tempLevels = game.getLevels();
		tempLevels.forEach(s -> s.addEntities(achievements));
		levels.addAll(game.getLevels());
		scorebar.setTimerManager(new TimerManager(game.getCurrentTime(), game.getClockGoingDown()));
		unlockedLevelNumbers = game.getUnlockedLevels();
		if (!firstTimeLoading) {
			scorebar.setLives(game.getNumberOfLives());
		}
	}
	
	/**
	 * Resets the current level to its initial conditions. For use when the hero
	 * dies and then revives.
	 */
	public void resetCurrentLevel() {
		levels.set(currentLevel - 1, game.cloneLevel(levelsInInitialState.get(currentLevel - 1)));
		game.setAchievements(levels.get(0).getEntities().stream().filter(s -> s instanceof AchievementEntity)
				.collect(Collectors.toList()));
	}
	
	/**
	 * @return levels
	 */
	public SelectionGroup<Level> getLevels() {
		return levels;
	}
	
	/**
	 * Unlocks the level specified
	 * @param currentLevel
	 */
	public void addUnlockedLevel(int currentLevel) {
		if (levelNumberInGame(currentLevel)) {
			unlockedLevelNumbers.add(currentLevel);
		}
	}
	
	/**
	 * Removes all contents in unlockedLevelNumbers
	 */
	public void clearUnlockedLevels() {
		unlockedLevelNumbers.removeAll(unlockedLevelNumbers);
	}
	
	/**
	 * @return unlockedLevelNumbers
	 */
	public Set<Integer> getUnlockedLevelNumbers() {
		return unlockedLevelNumbers;
	}
	
	/**
	 * Returns the game
	 * @return
	 */
	public Game getGame() {
		return game;
	}
}
