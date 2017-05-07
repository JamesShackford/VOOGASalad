package engine.game.timer;

/**
 * Tick Strategy for counting down on the clock.
 * 
 * @author Matthew Barbano
 *
 */
public class TickDown implements TickStrategy {

	@Override
	public int tick(int milliseconds) {
		return milliseconds - TimerManager.MILLISECONDS_PER_FRAME;
	}

	@Override
	public boolean timeIsUp(int milliseconds) {
		return milliseconds <= 0;
	}

}
