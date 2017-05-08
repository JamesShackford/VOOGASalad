package authoring.canvas;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
 * The DragUtil class allows you to make an entity moveable and resizeable when
 * the mouse is dragged on top of it.
 * 
 * Some code is borrowed from
 * http://stackoverflow.com/questions/16925612/how-to-resize-component-with-mouse-drag-in-javafx
 * 
 * @author jimmy
 *
 */
public class DragUtil
{

	/**
	 * The margin around the control that a user can click in to start resizing
	 * the region.
	 */
	private static final int RESIZE_MARGIN = 10;

	private final EntityView region;

	private int tileSize;

	private double x;
	private double y;

	private double untiledHeight;
	private double untiledWidth;

	private boolean initMinWidth;
	private boolean initMinHeight;

	private boolean rightResizeDragging;
	private boolean bottomResizeDragging;
	private boolean leftResizeDragging;
	private boolean topResizeDragging;
	private boolean moveDragging;

	private DragUtil(EntityView entityDisplay, int gridSize)
	{
		region = entityDisplay;
		tileSize = gridSize;
	}

	/**
	 * Makes the given EntityView moveable and resizeable on mouse drag. When
	 * the mouse is dragged on the rectangular border of the EntityView, the
	 * EntityView is resized. When the mouse is dragged on the center of the
	 * EntityView, the EntityView is translated (moved) to be on top of the
	 * mouse. The translation/resizing is done such that the EntityView snaps to
	 * the grid whose tile size is defined as gridSize.
	 * 
	 * @param region
	 *            EntityView to be made draggable
	 * @param gridSize
	 *            the tile size of the grid that the EntityView is in.
	 */
	public static void makeDraggable(EntityView region, int gridSize)
	{
		final DragUtil resizer = new DragUtil(region, gridSize);

		region.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if (!event.isControlDown()) {
					resizer.mousePressedMove(event);
					event.consume();
				}
			}

		});

		region.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				resizer.mouseDraggedMove(event);
				event.consume();
			}

		});
		region.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				resizer.mouseOverMove(event);
				event.consume();
			}

		});
		region.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if (!event.isControlDown()) {
					resizer.mouseReleasedMove(event);
					event.consume();
				}
			}

		});
	}

	public static void makeResizeable(EntityView region, int gridSize)
	{
		final DragUtil resizer = new DragUtil(region, gridSize);

		region.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if (!event.isControlDown()) {
					resizer.mousePressedResize(event);
					event.consume();
				}
			}

		});

		region.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				resizer.mouseDraggedResize(event);
				event.consume();
			}

		});
		region.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				resizer.mouseOverResize(event);
				event.consume();
			}

		});
		region.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
		{

			@Override
			public void handle(MouseEvent event)
			{
				if (!event.isControlDown()) {
					resizer.mouseReleasedResize(event);
					event.consume();
				}
			}

		});
	}

	private void mouseReleasedMove(MouseEvent event)
	{
		moveDragging = false;
		region.setCursor(Cursor.DEFAULT);
	}

	private void mouseReleasedResize(MouseEvent event)
	{
		rightResizeDragging = false;
		bottomResizeDragging = false;
		leftResizeDragging = false;
		topResizeDragging = false;
		region.setCursor(Cursor.DEFAULT);
		region.getWorkspace().getDefaults().edit(region.getEntity());
	}

	private void mouseOverMove(MouseEvent event)
	{
		if (isInMoveDraggableZone(event) && !moveDragging) {
			region.setCursor(Cursor.CLOSED_HAND);
		} else if (moveDragging) {
			region.setCursor(Cursor.NONE);
		}
	}

	private void mouseOverResize(MouseEvent event)
	{
		if ((isInRightDraggableZone(event) && isInBottomDraggableZone(event))
				|| (rightResizeDragging && bottomResizeDragging)) {
			region.setCursor(Cursor.SE_RESIZE);
		} else if ((isInRightDraggableZone(event) && isInTopDraggableZone(event))
				|| (rightResizeDragging && topResizeDragging)) {
			region.setCursor(Cursor.NE_RESIZE);
		} else if ((isInLeftDraggableZone(event) && isInBottomDraggableZone(event))
				|| (leftResizeDragging && bottomResizeDragging)) {
			region.setCursor(Cursor.SW_RESIZE);
		} else if ((isInLeftDraggableZone(event) && isInTopDraggableZone(event))
				|| (leftResizeDragging && topResizeDragging)) {
			region.setCursor(Cursor.NW_RESIZE);
		} else if (isInRightDraggableZone(event) || rightResizeDragging) {
			region.setCursor(Cursor.E_RESIZE);
		} else if (isInBottomDraggableZone(event) || bottomResizeDragging) {
			region.setCursor(Cursor.S_RESIZE);
		} else if (isInLeftDraggableZone(event) || leftResizeDragging) {
			region.setCursor(Cursor.W_RESIZE);
		} else if (isInTopDraggableZone(event) || topResizeDragging) {
			region.setCursor(Cursor.N_RESIZE);
		}
	}

	private boolean isInRightDraggableZone(MouseEvent event)
	{
		return event.getX() > (region.getWidth() - RESIZE_MARGIN) && region.isSelected();
	}

	private boolean isInBottomDraggableZone(MouseEvent event)
	{
		return event.getY() > (region.getHeight() - RESIZE_MARGIN) && region.isSelected();
	}

	private boolean isInLeftDraggableZone(MouseEvent event)
	{
		return event.getX() < RESIZE_MARGIN && region.isSelected();
	}

	private boolean isInTopDraggableZone(MouseEvent event)
	{
		return event.getY() < RESIZE_MARGIN && region.isSelected();
	}

	private boolean isInMoveDraggableZone(MouseEvent event)
	{
		return event.getX() > RESIZE_MARGIN && event.getX() < region.getWidth() - RESIZE_MARGIN
				&& event.getY() > RESIZE_MARGIN && event.getY() < region.getHeight() - RESIZE_MARGIN;
	}

	private void mouseDraggedMove(MouseEvent event)
	{
		if (moveDragging) {
			region.setCursor(Cursor.NONE);
			double mouseX = event.getX();
			double mouseY = event.getY();

			List<EntityView> movedEntities = region.getSelectedNeighbors();
			movedEntities.add(region);

			// region.setTranslateX(GridUtil.getTiledCoordinate(region.getTranslateX()
			// +
			// mouseX - region.getWidth() / 2));
			// region.setTranslateY(GridUtil.getTiledCoordinate(region.getTranslateY()
			// +
			// mouseY - region.getHeight() / 2));

			for (EntityView entity : movedEntities) {
				entity.setTranslateX(
						GridUtil.getTiledCoordinate(entity.getTranslateX() + mouseX - region.getWidth() / 2, tileSize));
				entity.setTranslateY(GridUtil
						.getTiledCoordinate(entity.getTranslateY() + mouseY - region.getHeight() / 2, tileSize));
			}
		}
		event.consume();
	}

	private void mouseDraggedResize(MouseEvent event)
	{
		if (region.isSelected()) {
			if (rightResizeDragging) {

				double mousex = event.getX();

				double newWidth = untiledWidth + (mousex - x);
				untiledWidth = newWidth;

				if (untiledWidth >= tileSize) {
					region.setMinWidth(GridUtil.getTiledCoordinate(untiledWidth, tileSize));
				}

				x = mousex;
			}

			if (bottomResizeDragging) {

				double mousey = event.getY();

				double newHeight = untiledHeight + (mousey - y);
				untiledHeight = newHeight;

				if (untiledHeight >= tileSize) {
					region.setMinHeight(GridUtil.getTiledCoordinate(untiledHeight, tileSize));
				}

				y = mousey;
			}

			if (leftResizeDragging) {
				double mousex = event.getX();

				untiledWidth += (x - mousex);

				double currX = region.getTranslateX();
				double newX = GridUtil.getTiledCoordinate(region.getBoundsInParent().getMinX() + mousex, tileSize);

				if (untiledWidth >= tileSize) {
					region.setMinWidth(GridUtil.getTiledCoordinate(untiledWidth, tileSize));
					if (currX != newX) {
						untiledWidth += currX - newX;
					}
					region.setTranslateX(
							GridUtil.getTiledCoordinate(region.getBoundsInParent().getMinX() + mousex, tileSize));
				}

				x = mousex;
			}

			if (topResizeDragging) {
				double mousey = event.getY();

				untiledHeight += (y - mousey);

				double currY = region.getTranslateY();
				double newY = GridUtil.getTiledCoordinate(region.getBoundsInParent().getMinY() + mousey, tileSize);

				if (untiledHeight >= tileSize) {
					region.setMinHeight(GridUtil.getTiledCoordinate(untiledHeight, tileSize));
					if (currY != newY) {
						untiledHeight += currY - newY;
					}
					region.setTranslateY(
							GridUtil.getTiledCoordinate(region.getBoundsInParent().getMinY() + mousey, tileSize));
				}

				y = mousey;
			}
		}
	}

	private void mousePressedMove(MouseEvent event)
	{
		if (isInMoveDraggableZone(event)) {
			moveDragging = true;
			x = event.getX();
			y = event.getY();
		}
	}

	private void mousePressedResize(MouseEvent event)
	{

		if (isInRightDraggableZone(event)) {
			rightResizeDragging = true;

			if (!initMinWidth) {
				initMinWidth();
			}
			x = event.getX();
		}

		if (isInBottomDraggableZone(event)) {
			bottomResizeDragging = true;

			if (!initMinHeight) {
				initMinHeight();
			}
			y = event.getY();
		}

		if (isInLeftDraggableZone(event)) {
			leftResizeDragging = true;

			if (!initMinWidth) {
				initMinWidth();
			}
			x = event.getX();
		}

		if (isInTopDraggableZone(event)) {
			topResizeDragging = true;

			if (!initMinHeight) {
				initMinHeight();
			}
			y = event.getY();
		}
	}

	private void initMinWidth()
	{
		region.setMinWidth(region.getWidth());
		untiledWidth = region.getWidth();
		initMinWidth = true;
	}

	private void initMinHeight()
	{
		region.setMinHeight(region.getHeight());
		untiledHeight = region.getHeight();
		initMinHeight = true;
	}
}