package engine.game.timer;

/**
 * TickStrategy for counting up on the clock.
 * @author Matthew Barbano
 *
 */
public class TickUp implements TickStrategy {

	@Override
	public int tick(int milliseconds) {
		return milliseconds + TimerManager.MILLISECONDS_PER_FRAME;
	}

	@Override
	public boolean timeIsUp(int milliseconds) {
		return false;
	}

}
