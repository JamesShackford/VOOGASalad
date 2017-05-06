package authoring.menu;

import authoring.Workspace;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import utils.views.View;

/**
 * 
 * A class encapsulating the Workspace's MenuBar.
 * 
 * It provides very little functionality: the creation of actual Menus is
 * delegated to subclasses of WorkspaceMenu in the setupMenus() method.
 * 
 * @author Elliott Bolzan
 *
 */
public class WorkspaceMenuBar extends View {

	private Workspace workspace;
	private MenuBar bar;

	/**
	 * Creates a WorkspaceMenuBar.
	 * 
	 * @param workspace
	 *            the Workspace that owns this MenuBar.
	 */
	public WorkspaceMenuBar(Workspace workspace) {
		this.workspace = workspace;
		setupView();
		setupMenus();
	}

	private void setupView() {
		this.bar = new MenuBar();
		VBox box = new VBox(bar);
		box.setPadding(new Insets(15, 0, 0, 0));
		setCenter(box);
	}

	private void setupMenus() {
		Menu gameMenu = new GameMenu(workspace);
		Menu editMenu = new EditMenu(workspace);
		Menu settingsMenu = new SettingsMenu(workspace);
		Menu serverMenu = new ServerMenu(workspace);
		Menu helpMenu = new HelpMenu(workspace);
		bar.getMenus().addAll(gameMenu, editMenu, settingsMenu, serverMenu, helpMenu);
	}

}
