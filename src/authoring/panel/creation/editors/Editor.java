package authoring.panel.creation.editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import authoring.Workspace;
import engine.GameObject;
import engine.Parameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import polyglot.Case;
import utils.views.View;

/**
 * 
 * A superclass for a number of editors in the Authoring Environment. All
 * editors have commonalities: they have a ComboBox from which the user can
 * choose the nature of that which is being edited, and they display the
 * parameters for that which was selected. Finally, they all need to provide a
 * save option. This is why this functionality was regrouped in one superclass.
 * 
 * @author Elliott Bolzan
 *
 */
public abstract class Editor extends View {

	private Workspace workspace;
	private ComboBox<String> comboBox;
	private Label description;
	private List<String> elements;
	private List<Parameter> parameters = new ArrayList<Parameter>();
	private TableView<Parameter> table;
	private boolean showSave;
	private Button saveButton;

	/**
	 * Creates an Editor.
	 * 
	 * @param workspace
	 *            the workspace that owns the Editor.
	 * @param elements
	 *            the elements to display.
	 * @param object
	 *            the object to (optionally) edit.
	 * @param showSave
	 *            whether the save button should be shown.
	 */
	public Editor(Workspace workspace, List<String> elements, GameObject object, boolean showSave) {
		this.workspace = workspace;
		this.elements = elements;
		Collections.sort(this.elements);
		this.showSave = showSave;
		setupView(object);
		if (object != null)
			update(object);
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	private void setupView(GameObject object) {
		setPadding(new Insets(10));
		createChooserBox(object);
		createTable(object);
		createBottom();
	}

	private void createChooserBox(GameObject object) {
		comboBox = new ComboBox<String>();
		comboBox.getItems().addAll(elements);
		comboBox.setOnAction((e) -> selected(comboBox.getSelectionModel().getSelectedItem()));
		comboBox.prefWidthProperty().bind(widthProperty());
		if (object == null) {
			comboBox.setValue(workspace.getPolyglot().get("Type", Case.TITLE).get());
		} else {
			comboBox.setValue(object.getDisplayName());
		}
		description = new Label();
		description.setWrapText(true);
		description.textProperty().addListener(e -> descriptionChanged());
		setTop(new VBox(comboBox, description));
	}

	private void descriptionChanged() {
		description.setPadding(new Insets(description.getText().equals("") ? 0 : 8));
	}

	@SuppressWarnings("unchecked")
	private void createTable(GameObject object) {
		table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		Label placeholder = new Label();
		placeholder.textProperty().bind(workspace.getPolyglot().get("NoParameters"));
		table.setPlaceholder(placeholder);
		table.prefHeightProperty().bind(heightProperty());
		table.setEditable(true);
		table.getStyleClass().add("visible-container");
		ObservableList<Parameter> items = FXCollections.observableArrayList(parameters);
		table.setItems(items);
		table.getColumns().setAll(makeKeyColumn(), makeValueColumn());
		if (object != null) {
			table.setItems(FXCollections.observableArrayList(object.getParams()));
		}
		setCenter(table);
	}

	private void createBottom() {
		VBox box = new VBox(10);
		Label instructions = new Label();
		instructions.setWrapText(true);
		instructions.textProperty().bind(workspace.getPolyglot().get("EditorInstructions"));
		instructions.setPadding(new Insets(5));
		instructions.setTextAlignment(TextAlignment.CENTER);
		if (showSave) {
			saveButton = workspace.getMaker().makeButton("TableEditorSaveButton", e -> save(), true);
			box.getChildren().add(saveButton);
		}
		box.getChildren().add(instructions);
		setBottom(box);
	}

	private TableColumn<Parameter, Object> makeKeyColumn() {
		TableColumn<Parameter, Object> column = new TableColumn<>();
		column.textProperty().bind(workspace.getPolyglot().get("Key"));
		column.setCellValueFactory(new PropertyValueFactory<Parameter, Object>("name"));
		return column;
	}

	private TableColumn<Parameter, Object> makeValueColumn() {
		TableColumn<Parameter, Object> column = new TableColumn<>();
		column.textProperty().bind(workspace.getPolyglot().get("Value"));
		column.setCellValueFactory(new PropertyValueFactory<Parameter, Object>("object"));
		column.setCellFactory(new Callback<TableColumn<Parameter, Object>, TableCell<Parameter, Object>>() {
			@Override
			public TableCell<Parameter, Object> call(TableColumn<Parameter, Object> param) {
				return new EditingCell(workspace);
			}
		});
		column.setOnEditCommit(new EventHandler<CellEditEvent<Parameter, Object>>() {
			@Override
			public void handle(CellEditEvent<Parameter, Object> t) {
				int row = t.getTablePosition().getRow();
				Parameter parameter = (Parameter) t.getTableView().getItems().get(row);
				parameter.setObject(t.getNewValue());
				addToParameters(parameter);
			}
		});
		column.setEditable(true);
		return column;
	}

	protected void update(GameObject object) {
		description.setText(object.getDisplayDescription());
		parameters = object.getParams();
		table.setItems(FXCollections.observableArrayList(parameters));
	}

	private void save() {
		if (!comboBoxHasSelection()) {
			Alert alert = workspace.getMaker().makeAlert(AlertType.ERROR, "ErrorTitle", "ErrorHeader",
					workspace.getPolyglot().get("PickSomething"));
			alert.show();
			return;
		}
		((Stage) getScene().getWindow()).close();
		save(parameters);
	}

	private boolean comboBoxHasSelection() {
		return !comboBox.getValue().equals(workspace.getPolyglot().get("Type").get());
	}

	private void addToParameters(Parameter updatedParameter) {
		parameters.removeIf(item -> item.getName().equals(updatedParameter.getName()));
		parameters.add(updatedParameter);
	}

	/**
	 * Begin the tutorial on the ComboBox. Displays an instruction tooltip and
	 * runs an alternative Runnable.
	 * 
	 * @param instruction
	 *            the instruction to be shown.
	 * @param toRun
	 *            the Runnable to be run on click, determined by the tutorial
	 *            system.
	 */
	public void initTutorialAction(String instruction, Runnable toRun) {
		comboBox.setOnAction((e) -> {
			selected(comboBox.getSelectionModel().getSelectedItem());
			if (comboBox.getValue().equals(instruction)) {
				toRun.run();
			}
		});
	}

	/**
	 * Modify the handler for the Save button.
	 * 
	 * @param toRun
	 *            the action to run when the save button is clicked, determine
	 *            by the tutorial system.
	 */
	public void changeSaveHandler(Runnable toRun) {
		saveButton.setOnAction(e -> {
			save();
			toRun.run();
		});
	}

	/**
	 * Called when the user has selected an element from the ComboBox.
	 * Implementations can have widely varying reactions to this event.
	 * 
	 * @param string
	 *            the element selected by the user.
	 */
	public abstract void selected(String string);

	/**
	 * Called when the user presses the save button.
	 * 
	 * @param data
	 *            the parameters that the user has entered and edited.
	 */
	public abstract void save(List<Parameter> data);

}
