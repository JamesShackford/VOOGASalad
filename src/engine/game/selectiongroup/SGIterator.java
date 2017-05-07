package engine.game.selectiongroup;

/**
 * For iterating over a SelectionGroup. Iterator Design Pattern.
 * 
 * @author Matthew Barbano
 *
 * @param <T>
 */
public interface SGIterator<T> {
	public boolean hasNext();

	public T next();
}
