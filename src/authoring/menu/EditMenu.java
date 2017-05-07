package authoring.menu;

import authoring.Workspace;
import javafx.scene.control.MenuItem;

/**
 * 
 * This subclass of WorkspaceMenu represents the Edit menu. From this Menu, the
 * user can access the following options: copying, pasting, selecting all of the
 * Entities on the Canvas, undoing, redoing, sending to back, and sending to
 * front.
 * 
 * @author Elliott Bolzan
 *
 */
public class EditMenu extends WorkspaceMenu {

	/**
	 * Creates an EditMenu.
	 * 
	 * @param workspace
	 *            the Workspace that owns this Menu.
	 */
	public EditMenu(Workspace workspace) {
		super(workspace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setTitle()
	 */
	@Override
	protected void setTitle() {
		setTitle("EditMenu");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setItems()
	 */
	@Override
	protected void setItems() {
		MenuItem copyItem = getMaker().makeMenuItem("Copy", e -> getWorkspace().getLevelEditor().copy(), true);
		MenuItem pasteItem = getMaker().makeMenuItem("Paste", e -> getWorkspace().getLevelEditor().paste(), true);
		MenuItem selectItem = getMaker().makeMenuItem("SelectAll",
				e -> getWorkspace().getLevelEditor().getCurrentLevel().selectAll(), true);
		MenuItem undoItem = getMaker().makeMenuItem("Undo", e -> getWorkspace().undo(), true);
		MenuItem redoItem = getMaker().makeMenuItem("Redo", e -> getWorkspace().redo(), true);
		MenuItem frontItem = getMaker().makeMenuItem("SendToFront", e -> getWorkspace().getLevelEditor().sendToFront(),
				true);
		MenuItem backItem = getMaker().makeMenuItem("SendToBack", e -> getWorkspace().getLevelEditor().sendToBack(),
				true);
		getItems().addAll(copyItem, pasteItem, selectItem, undoItem, redoItem, frontItem, backItem);
	}

}
