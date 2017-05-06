package authoring;

import java.util.ResourceBundle;

import data.Game;
import javafx.scene.Scene;
import javafx.stage.Stage;
import polyglot.Polyglot;

/**
 * 
 * The most high-level class in the Authoring Environment. Creates the Stage for
 * the Environment, and loads the Workspace into it.
 * 
 * @author Elliott Bolzan
 *
 */
public class AuthoringEnvironment {

	private ResourceBundle IOResources;
	private Polyglot polyglot;
	private Game game;
	private Workspace workspace;

	/**
	 * Creates an AuthoringEnvironment to edit an existing Game.
	 * 
	 * @param game
	 *            the Game that the Environment is loaded with.
	 * @param polyglot
	 *            the internationalization information for the application.
	 * @param IOResources
	 *            a ResourceBundle from which paths can be loaded.
	 */
	public AuthoringEnvironment(Game game, Polyglot polyglot, ResourceBundle IOResources) {
		this.game = game;
		this.polyglot = polyglot;
		this.IOResources = IOResources;
		setupStage();
	}

	/**
	 * Creates a new AuthoringEnvironment.
	 */
	public AuthoringEnvironment(Polyglot polyglot, ResourceBundle IOResources) {
		this(new Game(), polyglot, IOResources);
	}

	private void setupStage() {
		Stage stage = new Stage();
		stage.titleProperty().bind(polyglot.get("AuthoringEnvironmentTitle"));
		stage.setMinWidth(600);
		stage.setMinHeight(300);
		stage.setScene(createScene());
		stage.show();
		stage.setOnCloseRequest(e -> {
			workspace.getNetworking().close();
		});
	}

	private Scene createScene() {
		workspace = new Workspace(game, polyglot, IOResources);
		Scene scene = new Scene(workspace, 1000, 600);
		scene.getStylesheets().add(IOResources.getString("StylesheetPath"));
		return scene;
	}

}
