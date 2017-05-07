package authoring.panel.info;

import authoring.Workspace;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import polyglot.Case;
import utils.views.View;

/**
 * 
 * A class representing a view that serves to edit the game's instructions.
 * These instructions will later be shown to the user from the Player.
 * 
 * When the user presses the Save button, the Workspace's reference to Game is
 * updated.
 * 
 * @author Jesse Yue
 *
 */
public class InfoPanel extends View {
	private Workspace workspace;
	private HTMLEditor editor;

	/**
	 * Creates an InfoPanel.
	 * 
	 * @param workspace
	 *            the Workspace that owns this InfoPanel.
	 */
	public InfoPanel(Workspace workspace) {
		super(workspace.getPolyglot().get("InfoTitle", Case.TITLE));
		this.workspace = workspace;
		setup();
	}

	private void setup() {
		getStyleClass().add("background");
		VBox container = new VBox(10);
		setCenter(container);
		editor = new HTMLEditor();
		editor.setHtmlText(workspace.getGame().getInfo());
		Button save = workspace.getMaker().makeButton("SaveButtonEditor", e -> this.save(), true);
		container.getChildren().addAll(editor, save);
	}

	private void save() {
		workspace.getGame().setInfo(editor.getHtmlText());
	}

}
