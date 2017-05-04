package exceptions;

import java.util.ResourceBundle;

/**
 * Exceptions for Action and its subclasses. Extends RuntimeException.
 * 
 * @author Kyle Finke
 *
 */
public class ActionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param message
	 *            information that is displayed when the exception occurs
	 */
	public ActionException(String message) {
		super(String.format(ResourceBundle.getBundle("resources/ActionExceptions").getString("ActionException"),
				message));
	}

	/**
	 * 
	 * @param cause
	 *            reason that caused the exception to occur
	 */
	public ActionException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 *            information that is displayed when the exception occurs
	 * @param cause
	 *            reason that caused the exception to occur
	 */
	public ActionException(String message, Throwable cause) {
		super(String.format(ResourceBundle.getBundle("resources/ActionExceptions").getString("ActionException"),
				message), cause);
	}

}
