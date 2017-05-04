package authoring.panel.info;

import authoring.Workspace;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import polyglot.Case;
import utils.views.View;

public class InfoPanel extends View {
	private Workspace workspace;
	private HTMLEditor editor;

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
