package authoring.menu;

import java.util.ResourceBundle;

import authoring.Workspace;
import authoring.components.ComponentMaker;
import javafx.scene.control.Menu;
import polyglot.Case;
import polyglot.Polyglot;

/**
 * 
 * This abstract class represents a Menu that appears in the WorkspaceMenu.
 * 
 * It houses a reference to the Workspace that owns it, and provides getters for
 * the ResourceBundle and the Polyglot. Additionally, it provides a convenience
 * setTitle(String property) method that reduces duplicated code in subclasses.
 * This method binds the Menu's string property to a polyglot property.
 * 
 * @author Elliott Bolzan
 *
 */
public abstract class WorkspaceMenu extends Menu {

	private Workspace workspace;

	/**
	 * Creates a WorkspaceMenu.
	 * 
	 * @param workspace
	 *            the Workspace that owns this WorkspaceMenu.
	 */
	public WorkspaceMenu(Workspace workspace) {
		this.workspace = workspace;
		setTitle();
		setItems();
	}

	protected Polyglot getPolyglot() {
		return workspace.getPolyglot();
	}

	protected ResourceBundle getIOResources() {
		return workspace.getIOResources();
	}

	protected ComponentMaker getMaker() {
		return workspace.getMaker();
	}

	protected Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * Binds the Menu's text property to a Polyglot key.
	 * 
	 * @param property
	 *            the Polyglot key.
	 */
	protected void setTitle(String property) {
		textProperty().bind(getPolyglot().get(property, Case.TITLE));
	}

	/**
	 * A method whose contents set the title of the MenuItem. Each subclass will
	 * implement this method, and call the setTitle(String property) with the
	 * appropriate property. This sequence is designed to reduce duplicated code
	 * in the WorkspaceMenu subclasses.
	 */
	protected abstract void setTitle();

	/**
	 * Sets the Menu's MenuItems. Because each Menu must have MenuItems, each
	 * subclass must override this method. This method is called from
	 * WorkspaceMenu's constructor, after setTitle() is called.
	 */
	protected abstract void setItems();

}
