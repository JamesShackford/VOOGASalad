package starter;

import java.util.List;
import java.util.ResourceBundle;

import authoring.components.ComponentMaker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import polyglot.Case;
import polyglot.Polyglot;
import polyglot.PolyglotException;

/**
 * 
 * A class representing a view that allows the user to select the language for
 * the application.
 * 
 * Once the user selects a language, a ProgressDialog is shown while the
 * language is switched, on a different Thread. Once the switch is completed,
 * the Stage is automatically closed and the user is returned to the StartMenu.
 * 
 * @author Elliott Bolzan
 * 
 */
public class LanguagePicker {

	private Polyglot polyglot;
	private ResourceBundle resources;
	private List<String> languages;
	private Stage stage;

	/**
	 * Creates a LanguagePicker.
	 * 
	 * @param polyglot
	 *            the internationalization information for the application.
	 * @param resources
	 *            the ResourceBundle for the application.
	 * @param languages
	 *            the list of languages that can be switched to.
	 */
	public LanguagePicker(Polyglot polyglot, ResourceBundle resources, List<String> languages) {
		this.polyglot = polyglot;
		this.resources = resources;
		this.languages = languages;
		setupStage();
		setupView();
		stage.show();
	}
	
	/**
	 * @return the Stage for this LanguagePicker.
	 */
	public Stage getDialogStage() {
		return stage;
	}

	private void setupStage() {
		stage = new Stage();
		stage.initStyle(StageStyle.UNIFIED);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.titleProperty().bind(polyglot.get("LanguagePickerTitle", Case.TITLE));
	}

	private void setupView() {
		Label info = new Label();
		info.setWrapText(true);
		info.setMinHeight(40);
		info.setTextAlignment(TextAlignment.CENTER);
		info.textProperty().bind(polyglot.get("PickYourLanguage"));
		VBox box = new VBox(10);
		box.setAlignment(Pos.CENTER);
		ListView<String> list = makeListView();
		box.getChildren().addAll(info, list);
		box.setPadding(new Insets(20));
		Scene scene = new Scene(box, 300, 400);
		scene.getStylesheets().add(resources.getString("StylesheetPath"));
		stage.setScene(scene);
	}
	
	private ListView<String> makeListView() {
		ListView<String> list = new ListView<String>();
		list.getStyleClass().add("visible-container");
		list.setEditable(false);
		list.setItems(FXCollections.observableArrayList(languages));
		list.setOnMouseClicked(e -> selected(list.getSelectionModel().getSelectedItem()));
		return list;
	}

	private void selected(String language) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws PolyglotException {
				polyglot.setLanguage(language);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.close();
					}
				});
				return null;
			}
		};
		ComponentMaker maker = new ComponentMaker(polyglot, resources);
		maker.showProgressForTask(task, true);
	}

}
