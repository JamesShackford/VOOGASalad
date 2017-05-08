package authoring.panel.creation.editors;

import java.util.List;

import authoring.Workspace;
import authoring.panel.creation.pickers.ActionPicker;
import engine.Parameter;
import engine.actions.Action;
import engine.game.EngineController;

/**
 * 
 * An extension of Editor, specifically design to edit Actions. Allows the user
 * to consult and set values for a list of parameters for each Action. Gives the
 * user the option to save his or her progress in creating an Action.
 * 
 * @author Elliott Bolzan
 *
 */
public class ActionEditor extends Editor {

	private ActionPicker picker;
	private EngineController engine = new EngineController();
	private Action action;

	/**
	 * Creates an ActionEditor.
	 * 
	 * @param workspace
	 *            the workspace that owns this Editor.
	 * @param picker
	 *            the picker that owns this Editor.
	 * @param action
	 *            the action that this Editor is to edit (or null).
	 * @param elements
	 *            the elements that this Editor is to display in the ComboBox.
	 */
	public ActionEditor(Workspace workspace, ActionPicker picker, Action action, List<String> elements) {
		super(workspace, elements, action, true);
		this.picker = picker;
		this.action = action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.panel.creation.editors.Editor#selected(java.lang.String)
	 */
	@Override
	public void selected(String string) {
		try {
			action = engine.createAction(string);
		} catch (Exception e) {
			getWorkspace().getMaker().showFailure();
		}
		action.setEntity(picker.getEntityMaker().getEntity());
		update(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.panel.creation.editors.Editor#save(java.util.List)
	 */
	@Override
	public void save(List<Parameter> data) {
		action.setParams(data);
		picker.add(action);
	}

}
