package engine.game.gameloop;

import engine.GameInfo;
import engine.entities.Entity;
import engine.game.eventobserver.CollisionObservable;
import engine.game.eventobserver.InputObservable;
import engine.game.eventobserver.TimerObservable;
import engine.graphics.GraphicsEngine;
import javafx.scene.Scene;

/**
 * The purpose of this class is to package the EventObservables together for
 * easy naming and passing as parameter, consistent with the principle of
 * "Parameter Objects" to avoid having constructors/methods accumulate too many
 * parameters. Its functionality includes attaching and detaching Entity
 * observers from the observables, updating the observables, and getters for
 * each observable to be called by Events and Actions. Note that the three
 * EventObservables contained within this class are immutable fields (they do
 * not have setter methods) because the same ones are used for the duration of
 * an entire game (and ObservableBundle is instantiated once per game loop).
 * 
 * Assumes that this class is instantiated once at the beginning of the game
 * loop; that is, once every time a new or saved game is loaded. Dependencies
 * are the three EventObservable classes, and GraphicsEngine. Example of use:
 * 
 * <pre>
 * ObservableBundle observableBundle = new ObservableBundle(gameScene, graphicsEngine);
 * observableBundle.levelObservableSetup(gameInfo);
 * for(Entity entity : collectionOfEntities){
 * 		observableBundle.attach(entity);
 * }
 * observableBundle.updateObservers();
 * ----In KeyPressedEvent---
 * observableBundle.getInputObservable();
 * ---Back in game loop classes---
 * for(Entity entity : collectionOfEntities){
 * 		entity.detach();
 * }
 * </pre>
 * 
 * @author Matthew Barbano
 *
 */
public class ObservableBundle {
	private InputObservable inputObservable;
	private CollisionObservable collisionObservable;
	private TimerObservable timerObservable;

	/**
	 * Instantiates an ObservableBundle by setting its three Observable fields
	 * to new instances. gameScene and graphicsEngine needed for InputObservable
	 * instantiation. Assumes is called at the beginning of the game loop (do
	 * not confuse with levelObservableSetup(): called every time level
	 * restarts).
	 * 
	 * @param gameScene
	 *            the scene for the game in the game loop
	 * @param graphicsEngine
	 *            from the Game Player
	 */

	public ObservableBundle(Scene gameScene, GraphicsEngine graphicsEngine) {
		inputObservable = new InputObservable(gameScene, graphicsEngine);
		collisionObservable = new CollisionObservable();
		timerObservable = new TimerObservable();
	}
	
	/**
	 * Called by Events and Actions that need to access the input observable.
	 * 
	 * @return inputObservable
	 */
	public InputObservable getInputObservable() {
		return inputObservable;
	}

	
	public void setInputObservable(InputObservable inputObservable) {
		this.inputObservable = inputObservable;
	}

	/**
	 * Called by Events and Actions that need to access the collision
	 * observable.
	 * 
	 * @return collisionObservable
	 */
	public CollisionObservable getCollisionObservable() {
		return collisionObservable;
	}

	public void setCollisionObservable(CollisionObservable collisionObservable) {
		this.collisionObservable = collisionObservable;
	}

	/**
	 * Called by Events and Actions that need to access the timer observable.
	 * 
	 * @return timerObservable
	 */
	public TimerObservable getTimerObservable() {
		return timerObservable;
	}

	public void setTimerObservable(TimerObservable timerObservable) {
		this.timerObservable = timerObservable;
	}

	/**
	 * Attaches entity to all the EventObservables contained as fields.
	 * 
	 * @param entity
	 *            to attach
	 */
	public void attachEntityToAll(Entity entity) {
		inputObservable.attach(entity);
		collisionObservable.attach(entity);
		timerObservable.attach(entity);
	}

	/**
	 * Detaches entity from all the EventObservalbes contained as fields.
	 * Assumes that "entity" is contained in the Observers list of all
	 * EventObservables; otherwise the detach() method called will throw an
	 * ObservableException.
	 * 
	 * @param entity
	 *            to detach
	 */
	public void detachEntityFromAll(Entity entity) {
		inputObservable.detach(entity);
		collisionObservable.detach(entity);
		timerObservable.detach(entity);
	}

	/**
	 * Calls setup methods on the EventObservables that are assumed to be called
	 * every time a level restarts from the beginning (hero died, new level,
	 * etc.).
	 * 
	 * @param gameInfo
	 *            from the game loop, for getting the Timer Manager for the
	 *            timer observable
	 */
	public void levelObservableSetup(GameInfo gameInfo) {
		inputObservable.setupInputListeners();
		timerObservable.attachCurrentLevelTimerManager(gameInfo.getScorebar().getTimerManager());
	}

	/**
	 * Calls updateObservers() for all EventObservables contained as fields. To
	 * be called on every iteration of the game loop.
	 */
	public void updateObservers() {
		inputObservable.updateObservers();
		collisionObservable.updateObservers();
		timerObservable.updateObservers();
	}
}
