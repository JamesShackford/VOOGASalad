package authoring.settings;

import java.io.File;
import java.util.Scanner;
import java.util.function.Consumer;

import authoring.ActionButton;
import authoring.Workspace;
import authoring.utils.Direction;
import authoring.views.CollapsibleView;
import authoring.views.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
/**
 * The Settings sub-panel provides the user with the option of selecting his or her own background music
 * as well as saving the entire game and sending it to the Game Data Module
 * 
 * @author Mina
 *
 */
public class Settings extends View {

	private Workspace workspace;
	private VBox settingsContainer;

	/**
	 * The constructor needs a parent workspace specified. The rest of the constructor is inherited from
	 * its superclass. The constructor immediately instantiates all the buttons necessary.
	 * @param workspace
	 */
	public Settings(Workspace workspace) {
		super(workspace.getResources().getString("SettingsTitle"));
		this.workspace = workspace;
		configureSettings();
	}
	
	private void dummyMethod(){
		int i = 0;
	}
	
	private void chooseFile(Consumer<File> r)
	{
		FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showOpenDialog(new Stage());
				if (file != null){
					r.accept(file);
				}
	}
	
	private void configureSettings(){
		settingsContainer = new VBox();
		settingsContainer.setSpacing(Integer.parseInt(workspace.getResources().getString("SettingsSpacing")));
		Button selectMusic = new ActionButton(workspace.getResources().getString("MusicSelect"), event->chooseFile((File f) ->{
			Scanner scan;
		}));
	//	CheckBox hScrolling = new CheckBox("Horizontal Scrolling");
	//	CheckBox vScrolling= new CheckBox("Vertical Scrolling");
		Button saveButton = new ActionButton(workspace.getResources().getString("SaveButtonSettings"), event->dummyMethod());
		settingsContainer.getChildren().addAll(selectMusic,saveButton);
		setCenter(settingsContainer);
	}

}
