package authoring.menu;

import authoring.Workspace;
import authoring.components.HTMLDisplay;
import authoring.tutorial.AuthoringTutorial;
import javafx.scene.control.MenuItem;

/**
 * This subclass of WorkspaceMenu represents a HelpMenu. The user is given the
 * following options: obtaining a list of key combinations and showing a
 * tutorial.
 * 
 * @author Elliott Bolzan
 *
 */
public class HelpMenu extends WorkspaceMenu {

	/**
	 * Creates a HelpMenu.
	 * 
	 * @param workspace
	 *            the Workspace that owns the HelpMenu.
	 */
	public HelpMenu(Workspace workspace) {
		super(workspace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setTitle()
	 */
	@Override
	protected void setTitle() {
		setTitle("HelpMenu");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setItems()
	 */
	@Override
	protected void setItems() {
		MenuItem keyItem = getMaker().makeMenuItem("KeyCombinations", e -> showKeyCombinations(), true);
		MenuItem tutorialItem = getMaker().makeMenuItem("AuthoringTour", e -> initTutorial(), true);
		getItems().addAll(keyItem, tutorialItem);
	}

	private void showKeyCombinations() {
		HTMLDisplay display = new HTMLDisplay(getIOResources().getString("HelpPath"),
				getPolyglot().get("KeyCombinationsMenuItem"));
		display.show();
	}

	private void initTutorial() {
		new AuthoringTutorial(getWorkspace());
	}

}
