/**
 * 
 */
package authoring;

import java.util.ResourceBundle;

import authoring.canvas.Canvas;
import authoring.panel.Panel;
import authoring.settings.Settings;
import authoring.utils.Factory;
import authoring.views.View;
import discussion.Discussion;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/**
 * @author Elliott Bolzan
 *
 */
public class Workspace extends View {

	private ResourceBundle resources;
	private Settings settings;
	private Canvas canvas;
	private Panel panel;
	private SplitPane pane;

	/**
	 * 
	 */
	public Workspace(ResourceBundle resources) {
		super("Workspace");
		this.resources = resources;
		setup();
	}

	/**
	 * Initializes the Workspace's components.
	 */
	private void setup() {
		pane = new SplitPane();
		settings = new Settings(this, 0);
		canvas = new Canvas(this);
		panel = new Panel(this, 1);
		pane.getItems().addAll(settings, canvas, panel);
		pane.setDividerPositions(0.3, 0.75);
		setPadding(new Insets(4));
		setCenter(pane);
	}

	public ResourceBundle getResources() {
		return resources;
	}

	public SplitPane getPane() {
		return pane;
	}

	public void showMessage(String message) {
		Factory factory = new Factory(resources);
		factory.makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader", message).showAndWait();
	}

}
