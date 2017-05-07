package authoring.components;

import javafx.beans.binding.StringBinding;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * 
 * This class serves to display an HTML-formatted file. It needs to be provided
 * with a file path to an HTML file and with a StringBinding representing the
 * window's title.
 * 
 * @author Elliott Bolzan
 *
 */
public class HTMLDisplay extends Stage {

	/**
	 * Creates an HTMLDisplay.
	 * 
	 * @param filePath
	 *            the path to the HTML file to be displayed.
	 * @param title
	 *            the title of the HTMLDisplay.
	 */
	public HTMLDisplay(String filePath, StringBinding title) {
		WebView browser = new WebView();
		WebEngine webEngine = browser.getEngine();
		webEngine.load(getClass().getClassLoader().getResource(filePath).toExternalForm());
		Scene scene = new Scene(browser, 400, 500);
		titleProperty().bind(title);
		setScene(scene);
		setResizable(false);
	}

}
