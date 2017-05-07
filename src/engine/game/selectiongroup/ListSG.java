package engine.game.selectiongroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List implementation for the SelectionGroup.
 * 
 * @author Matthew Barbano
 *
 * @param <T>
 */
public class ListSG<T extends Selectable> implements SelectionGroup<T> {
	private List<T> list;

	public ListSG() {
		list = new ArrayList<>();
	}

	/**
	 * Example in client class: SG<Level> group = new ListSG<>(new
	 * LinkedList<>());
	 * 
	 * @param listImplementation
	 */
	public ListSG(List<T> listImplementation) {
		list = listImplementation;
	}

	@Override
	public SGIterator<T> createIterator() {
		return new SGListIterator<T>(list);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public void add(T element) {
		list.add(element);
	}

	@Override
	public void add(int index, T element) {
		list.add(index, element);
	}

	@Override
	public void set(int index, T element) {
		list.set(index, element);
	}

	@Override
	public void remove(int index) {
		list.remove(index);
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public List<T> getListRepresentation() {
		return list;
	}

	@Override
	public void addAll(Collection<T> collection) {
		list.addAll(collection);
	}

	@Override
	public void removeAll() {
		while (!list.isEmpty()) {
			list.remove(0);
		}
	}
}
