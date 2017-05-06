package authoring.components;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 
 * This class provides a special editing mechanism for TableViews: it allows
 * user to input keystrokes while editing a field. This is particularly
 * practical for keys like SHIFT, RIGHT, or LEFT, which are commonly used in
 * games, but impossible to input on a normal TextField.
 * 
 * @author Elliott Bolzan
 * 
 */
public class KeyCodeField extends TextField {

	private boolean editing;
	private KeyCode keyCode;

	/**
	 * Create a new KeyCodeField.
	 */
	public KeyCodeField() {
		focusedProperty().addListener((arg0, oldValue, newValue) -> {
			editing = newValue;
		});
		setOnKeyPressed(e -> keyPressed(e));
	}

	/**
	 * Create a new KeyCodeField from a string.
	 * 
	 * @param text
	 *            the content of the KeyCodeField.
	 */
	public KeyCodeField(String text) {
		super(text);
	}

	/**
	 * @return the KeyCodeField's current KeyCode.
	 */
	public KeyCode getKeyCode() {
		return keyCode;
	}

	private void keyPressed(KeyEvent e) {
		if (editing) {
			e.consume();
			keyCode = e.getCode();
			setText(e.getText());
			setFocused(false);
		}
	}

}
