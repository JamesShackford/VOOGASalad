package authoring.command;

import authoring.canvas.EntityView;
import authoring.canvas.Layer;
import authoring.canvas.LevelEditor;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A command to add or delete an EntityView from the LevelEditor.
 * 
 * @author jimmy (animations by Elliott Bolzan)
 *
 */
public class AddDeleteCommand extends EntityCommand {

	private LevelEditor levels;
	private boolean add;

	/**
	 * A command that will either add or delete the given EntityView from the
	 * given LevelEditor. If the add boolean is true, then the entityView will
	 * be added. Otherwise, the EntityView will be deleted.
	 * 
	 * @param entityView
	 *            EntityView to be added/deleted
	 * @param levels
	 *            LevelEditor to add/delete the EntityView from
	 * @param add
	 *            true if adding EntityView, false if deleting EntityView
	 */
	public AddDeleteCommand(EntityView entityView, LevelEditor levels, boolean add)
	{
		super(entityView);
		this.levels = levels;
		this.add = add;
	}

	@Override
	public void execute() {
		addOrRemove(add);
	}

	@Override
	public void unexecute() {
		addOrRemove(!add);
	}

	private void addOrRemove(boolean add) {
		EntityView entityView = super.getEntityView();
		if (add && levels.getEntity(entityView.getEntityId()) == null) {
			add(entityView);
		} else {
			remove(entityView);
		}
	}

	private void add(EntityView entityView) {
		entityView.setOpacity(0);
		levels.getCurrentLevel().addEntity(entityView, (int) entityView.getEntity().getZ());
		animate(entityView, 0, 1);
		entityView.setSelected(true);
	}

	private void remove(EntityView entityView) {
		FadeTransition ft = animate(entityView, 1, 0);
		ft.setOnFinished(event -> {
			Layer addedLayer = levels.getCurrentLevel().getLayers().get((int) entityView.getEntity().getZ() - 1);
			if (addedLayer != null && addedLayer.getEntities().contains(entityView)) {
				levels.getCurrentLevel().getCanvas().removeEntity(entityView);
				addedLayer.getEntities().remove(entityView);
			}
		});
	}

	private FadeTransition animate(Node node, double from, double to) {
		FadeTransition ft = new FadeTransition(Duration.millis(300), node);
		ft.setFromValue(from);
		ft.setToValue(to);
		ft.play();
		return ft;
	}

}