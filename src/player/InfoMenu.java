package player;

import javafx.stage.Stage;

public class InfoMenu extends AbstractMenu{

	public InfoMenu(Stage stage){
		backButton().setOnAction(e -> back(stage));
	}

}
