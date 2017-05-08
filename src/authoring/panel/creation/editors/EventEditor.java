package authoring.panel.creation.editors;

import java.util.List;

import authoring.Workspace;
import authoring.panel.creation.pickers.EventPicker;
import engine.Parameter;
import engine.events.Event;
import engine.game.EngineController;

/**
 * 
 * An extension of Editor, specifically design to edit Events. Allows the user
 * to consult and set values for a list of parameters for each Event. Gives the
 * user the option to save his or her progress in creating an Event.
 * 
 * @author Elliott Bolzan
 *
 */
public class EventEditor extends Editor {

	private EventPicker picker;
	private EngineController engine = new EngineController();
	private Event event;

	/**
	 * Creates an EventEditor.
	 * 
	 * @param workspace
	 *            the workspace that owns the Editor.
	 * @param picker
	 *            the picker that owns the Editor.
	 * @param event
	 *            the Event (optionally null) to be edited.
	 * @param elements
	 *            the elements to be displayed in the ComboBox as options.
	 */
	public EventEditor(Workspace workspace, EventPicker picker, Event event, List<String> elements) {
		super(workspace, elements, event, true);
		this.picker = picker;
		this.event = event;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.panel.creation.editors.Editor#selected(java.lang.String)
	 */
	@Override
	public void selected(String string) {
		try {
			event = engine.createEvent(string);
		} catch (Exception e) {
			getWorkspace().getMaker().showFailure();
		}
		event.setEntity(picker.getEntityMaker().getEntity());
		update(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.panel.creation.editors.Editor#save(java.util.List)
	 */
	@Override
	public void save(List<Parameter> data) {
		event.setParams(data);
		picker.add(event);
	}

}
