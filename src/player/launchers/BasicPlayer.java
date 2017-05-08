package player.launchers;

import java.util.ResourceBundle;

import data.Game;
import javafx.stage.Stage;
import polyglot.Polyglot;

/**
 * This class is designed to make launching a game as easy is possible. The idea
 * is that if you have a Game object, you should be able to just make a
 * BasicPlayer and give it a JavaFX Stage to run on and have a playable game.
 * The other two parameters to the constructor are resource packs for the
 * project.
 * 
 * @author Jay Doherty
 *
 */
public class BasicPlayer extends AbstractPlayer {

	public BasicPlayer(Stage primaryStage, Game game, Polyglot polyglot, ResourceBundle IOResources,
			boolean firstTimeLoading) {
		super(primaryStage, game, polyglot, IOResources, firstTimeLoading);
		this.getRunningGameLoop().startTimeline();
	}

}