package player.menus;

import java.util.ResourceBundle;
import java.util.stream.Collectors;

import data.Game;
import javafx.stage.Stage;
import player.MediaManager;
import polyglot.Polyglot;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.Parameter;
import engine.actions.Action;
import engine.entities.Entity;
import engine.events.Event;
import engine.events.regular_events.KeyPressEvent;
import engine.events.regular_events.KeyReleaseEvent;
import engine.game.Level;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Menu where user can change controls and media
 * 
 * @author Jesse
 *
 */
public class OptionsMenu extends AbstractMenu {

	private ScrollPane center;
	private Map<String, Parameter> keys;
	private Map<String, Parameter> keyReleases;
	private GridPane grid;
	private int count = 0;

	public OptionsMenu(Stage stage, Game game, MediaManager mediaManager, Polyglot polyglot,
			ResourceBundle IOResources) {
		super(stage, game, mediaManager, "OptionsTitle", polyglot, IOResources);
	}

	@Override
	public void addElements() {
		center = new ScrollPane();
		center.setFitToWidth(true);
		keys = new HashMap<>();
		keyReleases = new HashMap<>();
		this.setCenter(center);
		loadKeyBindings();
		setupView();
		this.setInsets();
	}

	private void setupView() {
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(20);
		center.setContent(grid);

		this.addHeading(getPolyglot().get("Controls"), count);
		count++;

		for (String s : keys.keySet()) {
			addControlRow(s, keys.get(s).getObject(), count);
			count++;
		}
		if (this.getMediaManager().getMediaPlayer() != null) {
			this.addHeading(getPolyglot().get("Audio"), count);
			count++;

			Slider volume = setupVolumeSlider();
			grid.add(new Label("Master Volume"), 0, count);
			grid.add(volume, 1, count);
			count++;
		}

		this.setBottom(this.makeBackButton());

	}

	private Slider setupVolumeSlider() {
		Slider volume = new Slider();
		MediaPlayer songPlayer = this.getMediaManager().getMediaPlayer();

		volume.setValue(songPlayer.getVolume() * 100.0);
		songPlayer.volumeProperty().bind(volume.valueProperty().divide(100.0));
		volume.setShowTickLabels(true);
		volume.setShowTickMarks(true);

		return volume;
	}

	private void loadKeyBindings() {
		for (Level level : this.getGame().getLevels()) {
			Collection<Entity> entities = level.getEntities();
			for (Entity e : entities) {
				List<Event> keyPress = e.getEvents().stream().filter(s -> s.getClass().equals(KeyPressEvent.class))
						.collect(Collectors.toList());
				List<Event> keyRelease = e.getEvents().stream().filter(s -> s.getClass().equals(KeyReleaseEvent.class))
						.collect(Collectors.toList());

				for (Event event : keyPress) {
					Parameter k = getKeyParameter(event);
					List<Action> actions = event.getActions();
					for (Action action : actions) {
						String actionName = action.getParams().get(0).getName();
						String actionValue = action.getParams().get(0).getObject().toString();
						StringBuilder builder = new StringBuilder(actionName);
						builder.append(" ");
						builder.append(actionValue);
						keys.put(builder.toString(), k);
					}
				}

				for (Event event : keyRelease) {
					Parameter k = getKeyParameter(event);
					keyReleases.put(k.getObject().toString(), k);
				}
			}
		}
	}

	private Parameter getKeyParameter(Event event) {
		List<Parameter> params = event.getParams().stream()
				.filter(s -> s.getName().equals("Key")).collect(Collectors.toList());
		return params.get(0);
	}

	private void addControlRow(String action, Object key, int row) {
		Label actionLabel = new Label(action);
		TextField keyLabel = makeKeyLabel(key.toString(), actionLabel);
		grid.add(actionLabel, 0, row);
		grid.add(keyLabel, 1, row);
	}

	private void addHeading(StringBinding heading, int row) {
		Label title = new Label();
		title.textProperty().bind(heading);
		title.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
		grid.add(title, 0, row, 2, 1);
	}

	private TextField makeKeyLabel(String key, Label actionLabel) {
		TextField keyLabel = new TextField();
		keyLabel.setText(key);
		keyLabel.setEditable(false);
		keyLabel.setOnMouseClicked(e -> keyLabelAction(keyLabel, actionLabel));

		return keyLabel;
	}

	private void keyLabelAction(TextField key, Label actionLabel) {
		key.setEditable(true);
		key.setOnKeyPressed(e -> keyPressAction(e.getCode(), key, actionLabel));
		key.setEditable(false);
	}

	private void keyPressAction(KeyCode e, TextField key, Label actionLabel) {
		keys.get(actionLabel.getText()).setObject(e);
		// Change associated key releases
		if (keyReleases.get(key.getText()) != null) {
			keyReleases.get(key.getText()).setObject(e);
		}
		// this.getLoader().loadData().saveGame(this.getLoader().loadGame(),
		// this.getLoader().getGamePath());
		key.setText(e.toString());
	}

}
