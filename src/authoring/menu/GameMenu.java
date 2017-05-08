package authoring.menu;

import authoring.Workspace;
import javafx.scene.control.MenuItem;

/**
 * This subclass of WorkspaceMenu represents a Game menu. The user is given the
 * following options: saving a game and testing a game.
 * 
 * @author Elliott Bolzan
 *
 */
public class GameMenu extends WorkspaceMenu {

	/**
	 * Creates a GameMenu.
	 * 
	 * @param workspace
	 *            the Workspace that owns the GameMenu.
	 */
	public GameMenu(Workspace workspace) {
		super(workspace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setTitle()
	 */
	@Override
	public void setTitle() {
		setTitle("GameMenu");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setItems()
	 */
	@Override
	public void setItems() {
		MenuItem saveItem = getMaker().makeMenuItem("Save", e -> getWorkspace().save(), true);
		MenuItem testItem = getMaker().makeMenuItem("Test", e -> getWorkspace().test(), true);
		getItems().addAll(saveItem, testItem);
	}

}
