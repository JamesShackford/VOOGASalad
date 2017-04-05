package engine.game;

import engine.Entity;
import engine.Event;
import engine.events.CollisionEvent;
import engine.events.InputEvent;
import engine.game.eventobserver.CollisionObservable;
import engine.game.eventobserver.InputObservable;
import engine.game.eventobserver.TimerObservable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.util.Duration;

/**
 * Manages the highest level of time flow in the game.
 * 
 * @author Matthew Barbano
 *
 */
public class GameLoop {
	public static final int FRAME_TIME_MILLISECONDS = 20;
	private LevelManager levelManager;
	private Timeline timeline;
	private InputObservable inputObservable;
	private CollisionObservable collisionObservable;
	private TimerObservable timerObservable;

	/**
	 * currentLevelManager is instantiated in the Game Authoring Environment
	 * (probably somewhere in the authoring.settings package)
	 * 
	 * @param currentLevelManager
	 */
	public GameLoop(Scene gameScene, String gameFilename) {
		
		// Setup Observables
		inputObservable = new InputObservable();
		collisionObservable = new CollisionObservable();
		timerObservable = new TimerObservable();

		inputObservable.setupInputListeners(gameScene);

		// Setup levelManager
		levelManager = new LevelManager();
		levelManager.loadAllSavedLevels(gameFilename);

		// Give all Events belonging to Entities in the current Level their
		// corresponding Observables
		for (Entity entity : levelManager.getCurrentLevel().getEntities()) {
			for (Event event : entity.getEvents()) {
				if (event instanceof InputEvent) {
					event.addEventObservable(inputObservable);
				}
				else if (event instanceof CollisionEvent){
					event.addEventObservable(collisionObservable);
				}
				/*
				else if (event instanceof TimerEvent){
					event.addEventObservable(timerObservable);
				}
				*/
			}
		}

		// Attach entities in current level to observables
		for (Entity entity : levelManager.getCurrentLevel().getEntities()) {
			inputObservable.attach(entity);
			collisionObservable.attach(entity);
			timerObservable.attach(entity);
		}

		// Start the GAE's current level
		levelManager.startCurrentLevel();

		setupTimeline();
	}

	private void setupTimeline() {
		// From ExampleBounce.java (will likely upgrade)
		KeyFrame frame = new KeyFrame(Duration.millis(FRAME_TIME_MILLISECONDS), e -> step());
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(frame);
	}

	/**
	 * External Engine API. Proceeds to the next frame of the animation while
	 * the game is playing. Responsibilities include: - loop through all
	 * Entities and call AlwaysEvent's act() (for Actions that occur on every
	 * frame, such as an enemy moving) - for all three types of Observer Events,
	 * call updateObservables() - and more
	 */
	public void startTimeline() {
		timeline.play();
	}

	private void step() {
		levelManager.getCurrentLevel().getTimerManager().tick();   //at beginning of step rather than end because KeyFrames' onFinished execute at END of elapsed time
		
		inputObservable.updateObservers();
		collisionObservable.updateObservers();
		timerObservable.updateObservers();

		for (Entity entity : levelManager.getCurrentLevel().getEntities()) {
			//entity.update();               //TODO <-----------------Comment out when Nikita finishes
		}

		inputObservable.setInputToProcess(false);
		
		printStepData();
		
		if(levelManager.getCurrentLevel().getTimerManager().timeIsUp()){  //OK to do for BOTH TickUp and TickDown timers!
			 System.out.println("Game over - you ran out of time!");
			 timeline.stop();
		}
	}
	
	/**
	 * Temporary, for testing. Will be removed later.
	 */
	private void printStepData(){
		System.out.println(levelManager.getCurrentLevel().getTimerManager());
	}
}
