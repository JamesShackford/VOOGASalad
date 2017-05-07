package engine.game.gameloop;

import java.util.List;
import java.util.stream.Collectors;

import engine.GameInfo;
import engine.actions.Action;
import engine.entities.Entity;
import engine.entities.entities.CameraEntity;
import engine.events.Event;
import engine.events.regular_events.InsideCameraRegionEvent;
import engine.game.LevelManager;
import engine.graphics.GraphicsEngine;

/**
 * Subclass of StepStrategy substituting for step() (and setup()) when a Level
 * should be displayed. Also sets up the next screen as appropriate on step()'s
 * last iteration. See StepStrategy interfaces's Javadoc for more. NOTE: Nikita
 * added an optimization for faster processing, but this class is mostly Matthew's
 * code.
 * 
 * @author Matthew Barbano, Nikita
 * @see StepStrategy
 *
 */

public class LevelStepStrategy implements StepStrategy
{
	private LevelManager levelManager;
	private GraphicsEngine graphicsEngine;
	private GameInfo info;
	private boolean screenFinished;
	private StepStrategy nextStepStrategy;

	/**
	 * Functionality executed when timeline for Screen with this
	 * LevelStepStrategy is step up; only executed once. Assumed to be called
	 * from Screen's constructor, when a level needs to be reinitialized (i.e.
	 * hero dies, start a new level, etc.).
	 */ 

	@Override
	public void setup(LevelManager levelManager, GraphicsEngine graphicsEngine, GameInfo info)
	{
		this.levelManager = levelManager;
		this.graphicsEngine = graphicsEngine;
		this.info = info;
		this.screenFinished = false;
		levelManager.resetCurrentLevel();
		info.getScorebar().resetTimerManager();
		setupGameView();
		addInfoToEntities();
	}
	
	/**
	 * Helper for setting up the camera.
	 */

	public void flagScreenFinished(StepStrategy nextStepStrategy)
	{
		this.screenFinished = true;
		this.nextStepStrategy = nextStepStrategy;
	}

	/**
	 * Helper grouping all the observable logic in this class for setup.
	 */

	public boolean screenFinished()
	{
		return screenFinished;
	}

	/**
	 * Assumed to be called from TimelineManipulator to communicate to
	 * step() to change screens, this sets 
	 * screenFinished to true and the nextStepStrategy field to the
	 * parameter. Need boolean flag rather than ending step() immediately
	 * since the rest of the Actions need to finish executing for that step.
	 * 
	 * @param nextStepStrategy  new StepStrategy
	 */

	@Override
	public void step()
	{
		InsideCameraRegionEvent event = new InsideCameraRegionEvent();
		List<Entity> observersTemp = info.getObservableBundle().getCollisionObservable().getObservers();
		info.getObservableBundle().getCollisionObservable()
				.setObservers(info.getObservableBundle().getCollisionObservable().getObservers().stream().filter(s -> {
					event.setEntity(s);
					return event.act();
				}).collect(Collectors.toList()));
		info.getObservableBundle().updateObservers();
		levelManager.getCurrentLevel().getEntities().stream().filter(s -> {
			event.setEntity(s);
			return event.act();
		}).forEach(e -> e.update());
		info.setEntitiesNeverUpdatedFalse();
		info.getObservableBundle().getCollisionObservable().getCollisions().clear();
		info.getObservableBundle().getInputObservable().setInputToProcess(false);
		graphicsEngine.updateFrame();
		if (screenFinished) {
			levelManager.setCurrentStepStrategy(nextStepStrategy);
			Screen nextScreen = new Screen(levelManager, graphicsEngine, info, false);
			nextScreen.getTimeline().play();
		}
		info.getObservableBundle().getCollisionObservable().setObservers(observersTemp);
	}

	/**
	 * Assumed to be called from Actions that change the screen displayed.
	 * 
	 * @return screenFinished
	 */

	private void addInfoToEntities()
	{
		info.getObservableBundle().levelObservableSetup(info);
		for (Entity entity : levelManager.getCurrentLevel().getEntities()) {
			entity.setGameInfo(info);
			info.getObservableBundle().attachEntityToAll(entity);
			for (Event event : entity.getEvents()) {
				event.setGameInfo(info);
				for (Action action : event.getActions()) {
					action.setGameInfo(info);
				}
			}
		}
	}

	/**
	 * Called on every iteration of the Timeline. Sets Entites to only function 
	 * if on camera (since some games were lagging), updates the frame in the 
	 * graphics engine, and checks if should move on to next StepStrategy. Note: Ticks the clock, 
	 * so need at beginning of step(), not end, because onFinished of Timeline called at
	 * END of time elapsed. 
	 * 
	 */
	private void setupGameView()
	{
		CameraEntity levelCamera = levelManager.getCurrentLevel().getCamera();
		levelManager.getCurrentLevel().getEntities().add(levelCamera);
		graphicsEngine.setupLevel(levelManager.getCurrentLevel());
	}
}