package authoring.command;

/**
 * This class represents a Command that can be applied or undone. This is
 * specifically useful for implementing an undo-redo feature in the authoring
 * environment.
 * 
 * @author jimmy
 *
 */
public interface UndoableCommand
{
	/**
	 * Execute this command as intended
	 */
	public void execute();

	/**
	 * Unapply this command after it has been executed.
	 */
	public void unexecute();
}
