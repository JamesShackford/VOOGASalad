package engine.actions.regular_actions;

import engine.Parameter;
import engine.actions.Action;

public class SetVisibleAction extends Action {

	public SetVisibleAction() {
		addParam(new Parameter("Visible", boolean.class, true));
	}

	@Override
	public void act() {
		getEntity().setIsVisible((boolean) getParam("Visible"));
	}
}