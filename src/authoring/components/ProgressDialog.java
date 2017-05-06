package authoring.components;

import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import polyglot.Polyglot;

/**
 * 
 * Displays a progress bar while a certain task runs in the background. Once the
 * task is completed, the Stage can be retrieved to be closed.
 * 
 * Adapted from:
 * http://stackoverflow.com/questions/29625170/display-popup-with-progressbar-in-javafx.
 * 
 * @author Elliott Bolzan
 * 
 *
 */
public class ProgressDialog {

	private Polyglot polyglot;
	private ResourceBundle IOResources;
	private Stage stage;
	private ProgressBar progressBar = new ProgressBar();

	/**
	 * Returns a ProgressDialog.
	 * 
	 * @param polyglot
	 *            the internationalization information.
	 * @param IOResources
	 *            the ResourceBundle for IO.
	 */
	public ProgressDialog(Polyglot polyglot, ResourceBundle IOResources) {
		this.polyglot = polyglot;
		this.IOResources = IOResources;
		setupStage();
		setupView();
		stage.show();
	}

	private void setupStage() {
		stage = new Stage();
		stage.initStyle(StageStyle.UNIFIED);
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.titleProperty().bind(polyglot.get("ProgressTitle"));
	}

	private void setupView() {
		progressBar.setProgress(-1F);
		progressBar.setPrefSize(Double.MAX_VALUE, 30);
		progressBar.setPadding(new Insets(5));
		HBox.setHgrow(progressBar, Priority.ALWAYS);
		Label info = new Label();
		info.textProperty().bind(polyglot.get("ProgressContent"));
		VBox box = new VBox();
		box.setSpacing(10);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(progressBar, info);
		box.setPadding(new Insets(20));
		Scene scene = new Scene(box, 300, 80);
		scene.getStylesheets().add(IOResources.getString("StylesheetPath"));
		stage.setScene(scene);
	}

	/**
	 * @return the Stage for the dialog.
	 */
	public Stage getDialogStage() {
		return stage;
	}

}
