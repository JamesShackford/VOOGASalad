package engine.game.selectiongroup;

import java.util.Collection;
import java.util.List;

/**
 * Wrapped for data structure used to represent all Levels in a game (or
 * possibly other Selectable objects, via the generic). Implementation examples
 * in subclasses include a List and graph. Note: This is NOT part of any API
 * since it is only used within the Game Engine. I did not have time to
 * do the graph implementation during the project, but this class could
 * be easily extended to include it.
 * 
 * @author Matthew Barbano
 *
 * @param <T>
 */
public interface SelectionGroup<T extends Selectable> {
	SGIterator<T> createIterator();

	int size();

	void add(T element);

	void add(int index, T element);

	void addAll(Collection<T> collection);

	void set(int index, T element);

	void remove(int index);

	void removeAll();

	T get(int index);

	List<T> getListRepresentation();
}
