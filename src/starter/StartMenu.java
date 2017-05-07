package starter;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.ResourceBundle;

import authoring.AuthoringEnvironment;
import authoring.components.ComponentMaker;
import data.Game;
import data.GameData;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import player.MediaManager;
import player.menus.MainMenu;
import polyglot.Case;
import polyglot.Polyglot;
import polyglot.PolyglotException;

/**
 * 
 * The first class to be launched for the Application.
 * 
 * Initializes the polyglot and the ResourceBundle, two items that are then
 * shared with the rest of the Application.
 * 
 * Displays a MenuBar with options for the user: a Game menu, in which one can
 * create, edit, or play games, and a Language menu, in which one can switch the
 * Application's language.
 * 
 * @author Elliott Bolzan
 *
 */
public class StartMenu extends BorderPane {

	private Stage stage;
	private Polyglot polyglot;
	private ResourceBundle IOResources = ResourceBundle.getBundle("resources/IO");
	private ComponentMaker maker;
	private List<String> languages;

	/**
	 * Create a StartMenu.
	 * 
	 * @param primaryStage
	 *            the Stage for the Application.
	 */
	public StartMenu(Stage primaryStage) {
		this.stage = primaryStage;
		try {
			this.polyglot = new Polyglot(IOResources.getString("Google_API_KEY"), "resources/Strings");
			this.languages = polyglot.languages();
		} catch (PolyglotException e) {
			System.out.println("There probably is no Internet connection.");
		}
		this.maker = new ComponentMaker(polyglot, IOResources);
		this.setIcon();
		this.buildStage();
	}

	private void setIcon() {
		String iconPath = IOResources.getString("IconPath");
		URL path = getClass().getResource(iconPath);
		if (isOSX()) {
			new OSXIconLoader(path);
		} else {
			this.stage.getIcons().add(new Image(iconPath));
		}
	}

	private void buildStage() {
		stage.titleProperty().bind(polyglot.get("StartMenuTitle", Case.TITLE));
		stage.setMinWidth(380);
		stage.setMinHeight(300);
		stage.setOnCloseRequest(e -> System.exit(0));
		stage.setScene(this.buildScene());
		this.buildView();
		stage.show();
	}

	private Scene buildScene() {
		Scene scene = new Scene(this, 380, 300);
		scene.getStylesheets().addAll(IOResources.getString("StylesheetPath"), IOResources.getString("ToolStylePath"));
		return scene;
	}

	private void buildView() {
		this.setTop(createMenu());
		this.setCenter(createLogo());
	}

	private ImageView createLogo() {
		ImageView imageView = new ImageView(new Image(IOResources.getString("LogoPath")));
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(300);
		RotateTransition rt = new RotateTransition(Duration.millis(600), imageView);
		rt.setByAngle(360);
		rt.setCycleCount(2);
		rt.setAutoReverse(true);
		playIn(1, e -> rt.play());
		return imageView;
	}

	private VBox createMenu() {
		MenuBar menuBar = new MenuBar();
		Menu menuFile = maker.makeMenu("GameMenu");
		MenuItem newItem = maker.makeMenuItem("New", e -> newGame(), true);
		MenuItem editItem = maker.makeMenuItem("Edit", e -> editGame(), true);
		MenuItem playItem = maker.makeMenuItem("Play", e -> playGame(), true);
		menuFile.getItems().addAll(newItem, editItem, playItem);
		Menu languageMenu = makeLanguageMenu();
		menuBar.getMenus().addAll(menuFile, languageMenu);
		menuBar.setOpacity(0);
		FadeTransition ft = new FadeTransition(Duration.millis(300), menuBar);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		playIn(3.5, e -> ft.play());
		VBox box = new VBox(menuBar);
		box.setPadding(new Insets(15, 0, 0, 0));
		return box;
	}

	private void playIn(double seconds, EventHandler<ActionEvent> handler) {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(seconds), handler));
		timeline.play();
	}

	private String chooseGame() {
		FileChooser chooser = maker.makeFileChooser(
				System.getProperty("user.dir") + IOResources.getString("DefaultDirectory"),
				IOResources.getString("ZIPChooserFilter"), IOResources.getString("ZIPChooserExtension"));

		File selectedDirectory = chooser.showOpenDialog(stage);
		if (selectedDirectory == null) {
			return "";
		} else {
			return selectedDirectory.getAbsolutePath();
		}
	}

	private Game createGame(String path) {
		try {
			GameData gameData = new GameData();
			return gameData.loadGame(path);
		} catch (Exception e) {
			// Thread this.
			Alert alert = maker.makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader", polyglot.get("NotAGame").get());
			alert.show();
			return null;
		}
	}

	private void newGame() {
		new AuthoringEnvironment(polyglot, IOResources);
	}

	private void editGame() {
		String path = chooseGame();
		if (!path.equals("")) {
			Game game = createGame(path);
			if (game != null) {
				new AuthoringEnvironment(game, polyglot, IOResources);
			}
		}
	}

	private void playGame() {
		String path = chooseGame();
		if (!path.equals("")) {
			Game game = createGame(path);
			if (game != null) {
				new MainMenu(game, new MediaManager(game, path), polyglot, IOResources);
			}
		}
	}

	private Menu makeLanguageMenu() {
		Menu languageMenu = maker.makeMenu("LanguageMenu");
		MenuItem pickLanguage = maker.makeMenuItem("PickLanguage", e -> checkForInternet(), true);
		languageMenu.getItems().add(pickLanguage);
		return languageMenu;
	}

	private void checkForInternet() {
		try {
			URLConnection connection = new URL("http://www.google.com").openConnection();
			connection.connect();
			new LanguagePicker(polyglot, IOResources, languages);
		} catch (Exception e) {
			Alert alert = maker.makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader", polyglot.get("NoInternet"));
			alert.show();
		}
	}

	private boolean isOSX() {
		return System.getProperty("os.name").equals("Mac OS X");
	}

}
