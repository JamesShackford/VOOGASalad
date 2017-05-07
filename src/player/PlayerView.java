package player;

import java.util.ResourceBundle;

import authoring.components.ComponentMaker;
import javafx.beans.binding.StringBinding;
import javafx.scene.Scene;
import polyglot.Polyglot;
import utils.views.View;

/**
 * @author Elliott Bolzan
 *
 */
public class PlayerView extends View {

	private Polyglot polyglot;
	private ResourceBundle IOResources;
	private ComponentMaker factory;

	/**
	 * @param title
	 */
	public PlayerView(StringBinding title) {
		super(title);
	}

	/**
	 * 
	 */
	public PlayerView(Polyglot polyglot, ResourceBundle IOResources) {
		this.polyglot = polyglot;
		this.IOResources = IOResources;
		this.factory = new ComponentMaker(polyglot, IOResources);
		setMinSize(420, 600);
	}
	
	/**
	 * Displays the scene
	 * @param width
	 * @param height
	 * @return
	 */
	public Scene createScene(double width, double height) {
		Scene scene = new Scene(this, width, height);
		scene.getStylesheets().add(IOResources.getString("StylesheetPath"));
		return scene;
	}

	public Scene createScene() {
		Scene scene = new Scene(this);
		scene.getStylesheets().add(IOResources.getString("StylesheetPath"));
		return scene;
	}

	protected Polyglot getPolyglot() {
		return polyglot;
	}

	protected ResourceBundle getResources() {
		return IOResources;
	}

	protected ComponentMaker getFactory() {
		return factory;
	}
}
