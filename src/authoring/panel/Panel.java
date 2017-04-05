package authoring.panel;

import java.util.ArrayList;
import java.util.List;

import authoring.Workspace;
import authoring.panel.chat.Chat;
import authoring.panel.display.EntityDisplay;
import authoring.settings.Settings;
import authoring.utils.Direction;
import authoring.utils.ComponentMaker;
import authoring.views.CollapsibleView;
import authoring.views.View;

public class Panel extends CollapsibleView {

	private Workspace workspace;
	private List<View> subviews;
	private EntityDisplay entityDisplay;
	private Settings settingsPanel;

	/**
	 * Returns a Panel.
	 * 
	 * @param workspace
	 *            the Workspace that owns the Panel.
	 * @param index
	 *            the index of the divider that the Panel collapses to, in the
	 *            SplitPane that owns it.
	 */
	public Panel(Workspace workspace, int index) {
		super(workspace, workspace.getPane(), workspace.getResources().getString("PanelTitle"), index, Direction.LEFT,
				true);
		this.workspace = workspace;
		entityDisplay = new EntityDisplay(workspace);
		settingsPanel = new Settings(workspace);
		createSubviews();
		setup();
	}

	/**
	 * Populate the subviews Lists, which dictates which subviews appear in the
	 * Accordion.
	 */
	private void createSubviews() {
		subviews = new ArrayList<View>();
		subviews.add(entityDisplay);
		subviews.add(new Chat(workspace));
		subviews.add(settingsPanel);
	}

	/**
	 * Create the Accordion and add it to the view.
	 */
	private void setup() {
		ComponentMaker componentMaker = new ComponentMaker(workspace.getResources());
		setCenter(componentMaker.makeAccordion(subviews));
	}
	
	public EntityDisplay getEntityDisplay() {
		return entityDisplay;
	}
	
	public void updateSettings(String newLayer){
		settingsPanel.updateBox(newLayer);
	}

}
