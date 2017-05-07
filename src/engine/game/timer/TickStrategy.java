package engine.game.timer;

/**
 * The Strategy substituted for whether the clock counts up or down. Strategy Design Pattern.
 * 
 * @author Matthew Barbano
 *
 */
public interface TickStrategy {
	/**
	 * Increments the time by the number of milliseconds specified.
	 * @param milliseconds
	 * @return
	 */
	public int tick(int milliseconds);
	
	/**
	 * Returns whether "timeIsUp". Always false for tickUp, returns true if time <= 0 for tickDown.
	 * @param milliseconds
	 * @return
	 */
	public boolean timeIsUp(int milliseconds);
}
