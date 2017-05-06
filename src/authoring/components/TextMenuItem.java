package authoring.components;

import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

/**
 * 
 * A custom-width MenuItem. Makes use of a Label to achieve its desired effect:
 * there is no way to increase the width of a MenuItem without setting its
 * internal components manually. Provides several constructors for different
 * kinds of text to show to the user.
 * 
 * @author Elliott Bolzan
 *
 */
public class TextMenuItem extends MenuItem {

	private Label label;

	/**
	 * Returns a TextMenuItem.
	 * 
	 * @param handler
	 *            the handler to be executed on click.
	 */
	public TextMenuItem(EventHandler<ActionEvent> handler) {
		label = new Label();
		label.setMinWidth(100);
		setGraphic(label);
		setOnAction(handler);
	}

	/**
	 * 
	 * Returns a TextMenuItem.
	 * 
	 * @param binding
	 *            the text to shown.
	 * @param keyCombination
	 *            the key combination that triggers the MenuItem.
	 * @param handler
	 *            the handler to be executed.
	 */
	public TextMenuItem(StringBinding binding, String keyCombination, EventHandler<ActionEvent> handler) {
		this(handler);
		setBinding(binding);
		setAccelerator(KeyCombination.keyCombination(keyCombination));
	}

	/**
	 * 
	 * Returns a TextMenuItem.
	 * 
	 * @param binding
	 *            the text to shown.
	 * @param handler
	 *            the handler to be executed.
	 */
	public TextMenuItem(StringBinding binding, EventHandler<ActionEvent> handler) {
		this(handler);
		setBinding(binding);
	}

	private void setBinding(StringBinding binding) {
		label.textProperty().bind(binding);
	}

}
