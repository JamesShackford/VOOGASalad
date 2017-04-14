package authoring.panel;

import java.util.Optional;

import authoring.Workspace;
import authoring.components.ComponentMaker;
import authoring.views.View;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * 
 * @author Mina Mungekar
 *
 */
public class LayerPanel extends View {

	private Workspace workspace;
	private VBox editorContainer;
	private ComboBox<String> myBox;
	private ComponentMaker maker;

	public LayerPanel(Workspace workspace) {
		super(workspace.getResources().getString("LayerPanelTitle"));
		this.workspace = workspace;
		maker = new ComponentMaker(workspace.getResources());
		myBox = new ComboBox<String>();
		myBox.setValue("Layer 1");
		configureEditing();
	}

	private void configureEditing() {
		editorContainer = new VBox();
		editorContainer.setSpacing(Integer.parseInt(workspace.getResources().getString("SettingsSpacing")));
		Button addLayerButton = maker.makeButton("AddLayerButton", e -> workspace.addLayer(), true);
		Button deleteLayerButton = maker.makeButton("DeleteLayerButton", e -> {
			initCloseRequest(e);
			delete();
		}, true);
		initLayerSelector();
		configureVelocitySettings();
		editorContainer.getChildren().addAll(addLayerButton, deleteLayerButton);
		setCenter(editorContainer);
	}

	private void initCloseRequest(Event e) {
		ComponentMaker maker = new ComponentMaker(workspace.getResources());
		String message = workspace.getResources().getString("ConfirmationContent");
		Alert alert = maker.makeAlert(AlertType.CONFIRMATION, "ConfirmationTitle", "ConfirmationHeader", message);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			e.consume();
		}
	}

	private void delete() {
		int layer = Integer.parseInt(myBox.getSelectionModel().getSelectedItem().split(" ")[1]);
		myBox.setValue((layer == 1 ? String.format("Layer %d", layer) : String.format("Layer %d", layer - 1)));
		workspace.deleteLayer(layer);
	}

	private void configureVelocitySettings() {
		Slider velocitySlider = new Slider() {
			{
				setMin(0);
				setMax(100);
				setValue(0);
			}
		};

		HBox sliderBox = new HBox() {
			{
				setSpacing(Integer.parseInt(workspace.getResources().getString("SettingsSpacing")));
				Text t = new Text(workspace.getResources().getString("LayerSpeedPrompt"));
				t.setFill(Color.WHITE);
				getChildren().addAll(t, velocitySlider);
			}
		};

		Button velocityButton = maker.makeButton("SaveLayerSpeed", null, true);
		editorContainer.getChildren().addAll(myBox, sliderBox, velocityButton);
	}

	/**
	 * updateBox is called whenever the LayerEditor adds another layer and needs
	 * to alert the combobox. The name of the new string name is passed back to
	 * the combobox.
	 * 
	 * @param newLayer
	 */

	public void updateBox(String newLayer) {
		myBox.getItems().add(newLayer);
	}

	private void initLayerSelector() {
		myBox.setPromptText(workspace.getResources().getString("LayerBoxPrompt"));
		myBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					workspace.selectLayer(Integer.parseInt(newValue.split(" ")[1]));
				}
			}
		});
	}

	/**
	 * selectLevelBox is called whenever levels are switched and the set of
	 * layers changes. The new level reports its new layer set to the workspace,
	 * which, in turn passes the new layer count to the combobox.
	 * 
	 * @param layerNum
	 */

	public void selectLevelBox(int layerNum) {
		myBox.getItems().clear();
		for (int i = 0; i < layerNum; i++) {
			myBox.getItems().add(String.format("Layer %d", i + 1));
		}
		myBox.setValue(String.format("Layer %d", 1));
	}

}