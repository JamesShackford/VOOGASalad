package player.menus;

import java.util.ResourceBundle;

import data.Game;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import player.MediaManager;
import player.score.Score;
import polyglot.Polyglot;

/**
 * Menu that displays highscores
 * 
 * @author Jesse Yue
 *
 */
public class HighscoreMenu extends AbstractMenu {

	private TableView<Score> scoreTable;
	private Game game;

	public HighscoreMenu(Stage stage, Game game, MediaManager mediaManager, Polyglot polyglot,
			ResourceBundle IOResources) {
		super(stage, game, mediaManager, "HighscoresTitle", polyglot, IOResources);
		this.game = game;
		setupScene();
		loadScores();
	}

	@SuppressWarnings("unchecked")
	private void setupScene() {
		scoreTable = new TableView<>();

		scoreTable.getColumns().setAll(makeRankColumn(), makeScoreColumn(), makeTimeColumn(), makeNameColumn());
		this.setCenter(scoreTable);
		this.setInsets();
	}

	private void loadScores() {
		scoreTable.setItems(game.getHighscores());
	}

	private TableColumn<Score, Integer> makeRankColumn() {
		TableColumn<Score, Integer> rank = new TableColumn<>("Rank");
		rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
		rank.prefWidthProperty().bind(scoreTable.widthProperty().multiply(.1));

		return rank;
	}

	private TableColumn<Score, String> makeScoreColumn() {
		TableColumn<Score, String> score = new TableColumn<>("Score");
		score.setCellValueFactory(new PropertyValueFactory<Score, String>("score"));
		score.prefWidthProperty().bind(scoreTable.widthProperty().multiply(.3));

		return score;
	}

	private TableColumn<Score, String> makeTimeColumn() {
		TableColumn<Score, String> time = new TableColumn<>("Time");
		time.setCellValueFactory(new PropertyValueFactory<>("time"));
		time.prefWidthProperty().bind(scoreTable.widthProperty().multiply(.29));

		return time;
	}

	private TableColumn<Score, String> makeNameColumn() {
		TableColumn<Score, String> name = new TableColumn<>("User");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.prefWidthProperty().bind(scoreTable.widthProperty().multiply(.3));

		return name;
	}

	@Override
	public void addElements() {
		this.setBottom(this.makeBackButton());
	}
}
