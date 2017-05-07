package authoring.menu;

import authoring.Workspace;
import javafx.scene.control.MenuItem;

/**
 * This subclass of WorkspaceMenu represents a Server menu. The user is given
 * the following options in this Menu: obtaining an IP address, starting a
 * server, or joining a server. If the user has already joined a server or
 * created one, he or she is given the option to disconnect from the server.
 * 
 * @author Elliott Bolzan
 *
 */
public class ServerMenu extends WorkspaceMenu {

	/**
	 * Creates a ServerMenu.
	 * 
	 * @param workspace
	 *            the Workspace that owns the ServerMenu.
	 */
	public ServerMenu(Workspace workspace) {
		super(workspace);
		setOnShowing(e -> serverMenuWillShow());
		serverMenuWillShow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setTitle()
	 */
	@Override
	protected void setTitle() {
		setTitle("ServerMenu");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see authoring.menu.WorkspaceMenu#setItems()
	 */
	@Override
	protected void setItems() {
	}

	/**
	 * Creates the Menu's items when it is about to be shown. Determines whether
	 * to show connecting and creating options, or the disconnecting option.
	 */
	private void serverMenuWillShow() {
		MenuItem IPItem = getMaker().makeMenuItem("IP", e -> getWorkspace().getNetworking().showIP(), true);
		MenuItem startItem = getMaker().makeMenuItem("StartServer", e -> getWorkspace().getNetworking().start(), true);
		MenuItem joinItem = getMaker().makeMenuItem("JoinServer", e -> getWorkspace().getNetworking().join(), true);
		MenuItem disconnectItem = getMaker().makeMenuItem("Disconnect", e -> getWorkspace().getNetworking().close(),
				true);
		getItems().clear();
		if (getWorkspace().getNetworking().isConnected()) {
			getItems().addAll(IPItem, disconnectItem);
		} else {
			getItems().addAll(IPItem, startItem, joinItem);
		}
	}

}
