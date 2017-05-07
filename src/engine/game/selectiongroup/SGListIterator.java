package engine.game.selectiongroup;

import java.util.Iterator;
import java.util.List;

/**
 * For iterating over a ListSelectionGroup. Iterator Design Pattern.
 * 
 * @author Matthew Barbano
 *
 * @param <T>
 */
public class SGListIterator<T> implements SGIterator<T> {
	private Iterator<T> listIterator;

	public SGListIterator(List<T> list) {
		listIterator = list.listIterator();
	}

	@Override
	public boolean hasNext() {
		return listIterator.hasNext();
	}

	@Override
	public T next() {
		return listIterator.next();
	}

}
