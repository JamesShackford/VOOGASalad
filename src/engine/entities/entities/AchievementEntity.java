package engine.entities.entities;

import engine.Parameter;
import engine.entities.Entity;
import engine.events.Event;
import engine.events.additional_events.FinishAchievementEvent;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Entity for achievements. Properties of achievements are very similar to those
 * of entities, so achievements were made to be a type of entity. Events owned
 * by this achievement should include finish achievement event. Other events
 * shouldn't have any actions associated with them.
 * 
 * @author nikita
 * @author Jesse
 * @author Mina
 */
public class AchievementEntity extends Entity {

	@Override
	protected void setupDefaultParameters() {
		addParam(new Parameter(getResource("Description"), String.class, ""));
		this.setImagePath(getClass().getClassLoader().getResource("resources/images/camera.png").toExternalForm());
		addAdditionalEventClass(FinishAchievementEvent.class);
	}

	/**
	 * Achievements don't move.
	 */
	@Override
	protected void move() {
	}

	/**
	 * Get the how close the achievement is to being completed
	 * 
	 * @return percent of the achievement that has been completed
	 */
	public SimpleDoubleProperty getPercent() {
		double completed = 0, total = 0;
		for (Event event : getEvents()) {
			if (!(event instanceof FinishAchievementEvent)) {
				total += (int) event.getParam(getResource("HowOftenToTrigger"));
				completed += event.getNumberTimesTriggered()
						.get() <= (int) event.getParam(getResource("HowOftenToTrigger"))
								? event.getNumberTimesTriggered().get()
								: (int) event.getParam(getResource("HowOftenToTrigger"));
			}
		}
		return new SimpleDoubleProperty(completed / total);
	}

}
