package authoring.panel.creation;

import java.io.File;

import authoring.Workspace;
import authoring.components.thumbnail.FixedThumbnail;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utils.views.View;

/**
 * This class serves to display and let the user edit the Entity's basic
 * information. As of now, it encapsulates a Thumbnail for the Entity's image,
 * its name, its scale, and a button letting the user save his or her changes.
 * 
 * @author Elliott Bolzan
 *
 */
public class EntityInfo extends View {

	private Workspace workspace;
	private EntityMaker editor;
	private TextField nameField;
	private FixedThumbnail thumbnail;

	private Slider widthSlider;
	private Slider heightSlider;
	private ToggleButton link;
	private boolean linked;
	private Button saveButton;
	private Button pickButton;

	/**
	 * Creates an EntityInfo.
	 * 
	 * @param workspace
	 *            the workspace for this view.
	 * @param editor
	 *            the EntityMaker which created the EntityInfo.
	 */
	public EntityInfo(Workspace workspace, EntityMaker editor) {
		this.workspace = workspace;
		this.editor = editor;
		setup();
	}

	/**
	 * @return the Entity's name.
	 */
	public String getName() {
		return nameField.getText();
	}

	/**
	 * @return the Entity's image path.
	 */
	public String getImagePath() {
		return thumbnail.getImagePath();
	}

	/**
	 * @return the Entity's image width, as modified by the scale-adjustment.
	 */
	public double getImageWidth() {
		return widthSlider.getValue();
	}

	/**
	 * @return the Entity's image height, as modified by the scale-adjustment.
	 */
	public double getImageHeight() {
		return heightSlider.getValue();
	}

	private void setup() {
		VBox box = new VBox(20);
		box.getChildren().addAll(createNameBox(), new Separator(), createImageBox(), new Separator(), createScaleBox(),
				createButtonBar());
		box.setPadding(new Insets(20));
		setCenter(box);
	}

	private HBox createNameBox() {
		HBox nameBox = new HBox(8);
		Label nameLabel = new Label();
		nameLabel.textProperty().bind(workspace.getPolyglot().get("TitlePrompt"));
		nameField = new TextField();
		nameField.setPrefWidth(100);
		nameField.setText(editor.getEntity().nameProperty().get());
		nameBox.getChildren().addAll(nameLabel, nameField);
		nameBox.setAlignment(Pos.CENTER);
		return nameBox;
	}

	private VBox createImageBox() {
		VBox imageBox = new VBox(20);
		thumbnail = new FixedThumbnail(editor.getEntity().getImagePath(), 50, 50);
		pickButton = workspace.getMaker().makeButton("PickButton", e -> pickImage(), true);
		imageBox.getChildren().addAll(thumbnail, pickButton);
		imageBox.setAlignment(Pos.CENTER);
		return imageBox;
	}

	private VBox createScaleBox() {
		linked = true;

		double imageWidth = getImage().getWidth();
		double currentWidth = editor.getEntity().widthProperty().get();
		double width = currentWidth == 0 ? imageWidth : currentWidth;
		widthSlider = new Slider(10, imageWidth * 2, width);
		VBox widthBox = createSliderBox("WidthSliderTitle", widthSlider);

		link = new ToggleButton();
		link.textProperty().bind(workspace.getPolyglot().get("Linked"));
		link.setSelected(true);
		link.setOnAction(e -> toggledLinked());

		double imageHeight = getImage().getHeight();
		double currentHeight = editor.getEntity().heightProperty().get();
		double height = currentHeight == 0 ? imageHeight : currentHeight;
		heightSlider = new Slider(10, imageHeight * 2, height);
		VBox heightBox = createSliderBox("HeightSliderTitle", heightSlider);

		setupSliderProperties();

		VBox box = new VBox(8);
		box.getChildren().addAll(widthBox, link, heightBox);
		box.setAlignment(Pos.CENTER);
		return box;
	}

	private VBox createSliderBox(String titleProperty, Slider slider) {
		VBox box = new VBox(4);
		Label label = new Label();
		label.textProperty().bind(Bindings.format(workspace.getPolyglot().getOriginal(titleProperty) + (" %,.0f px"),
				slider.valueProperty()));
		box.getChildren().addAll(label, slider);
		box.setAlignment(Pos.CENTER);
		return box;
	}

	private HBox createButtonBar() {
		HBox buttonBar = new HBox();
		saveButton = workspace.getMaker().makeButton("SaveButtonEditor", e -> editor.save(), true);
		Button cancelButton = workspace.getMaker().makeButton("CancelButton", e -> editor.dismiss(), true);
		buttonBar.getChildren().addAll(saveButton, cancelButton);
		return buttonBar;
	}

	private void pickImage() {
		FileChooser imageChooser = workspace.getMaker().makeFileChooser(
				System.getProperty("user.dir") + workspace.getIOResources().getString("ResourcesDirectory"),
				workspace.getIOResources().getString("ImageChooserFilter"), "*.png", "*.jpg", "*.gif");
		File file = imageChooser.showOpenDialog(getScene().getWindow());
		if (file != null) {
			thumbnail.setImage(file.toURI().toString());
		}
	}

	private Image getImage() {
		return new Image(getImagePath());
	}

	private void toggledLinked() {
		linked = !linked;
		if (linked) {
			link.textProperty().bind(workspace.getPolyglot().get("Linked"));
		} else {
			link.textProperty().bind(workspace.getPolyglot().get("Unlinked"));
		}
	}

	private void setupSliderProperties() {
		widthSlider.valueProperty().addListener(
				(ov, old_val, new_val) -> sliderPropertiesHelper(old_val, new_val, widthSlider, heightSlider));
		heightSlider.valueProperty().addListener(
				(ov, old_val, new_val) -> sliderPropertiesHelper(old_val, new_val, heightSlider, widthSlider));
	}

	private void sliderPropertiesHelper(Number old_val, Number new_val, Slider first, Slider second) {
		if (linked && !second.isValueChanging()) {
			double toMove = second.getMax() * (new_val.doubleValue() - old_val.doubleValue()) / first.getMax();
			second.valueProperty().set(toMove + second.getValue());
		}
	}

	/**
	 * Modify the save button's action for the tutorial.
	 * 
	 * @param r
	 *            the Runnable to be run when the save button is clicked.
	 */
	public void changeSaveHandler(Runnable r) {
		saveButton.setOnAction(e -> {
			editor.save();
			r.run();
		});
	}

	/**
	 * Modify the image selection button's action on click for the tutorial.
	 *
	 * @param r
	 *            the Runnable to be run when the image selection butotn is
	 *            clicked.
	 */
	public void changeImageHandler(Runnable r) {
		pickButton.setOnAction(e -> {
			pickImage();
			r.run();
		});
	}

}
