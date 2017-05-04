package engine.actions.regular_actions;

import engine.Parameter;
import engine.actions.Action;

/**
 * Changes the y acceleration of the Entity associated with this Action.
 * 
 * @author Kyle Finke
 *
 */
public class SetYAccelerationAction extends Action {

	public SetYAccelerationAction() {
		addParam(new Parameter(getResource("YAcceleration"), double.class, 0.0));
	}

	@Override
	public void act() {
		getEntity().setYAcceleration((double) getParam(getResource("YAcceleration")));
	}

}
