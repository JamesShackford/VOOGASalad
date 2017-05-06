package authoring.menu;

import authoring.Workspace;
import authoring.components.HTMLDisplay;
import authoring.tutorial.AuthoringTutorial;
import javafx.scene.control.MenuItem;

/**
 * @author Elliott Bolzan and Mina Mungekar
 *
 */
public class HelpMenu extends WorkspaceMenu {

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
