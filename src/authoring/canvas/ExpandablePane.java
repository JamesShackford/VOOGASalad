package authoring.canvas;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * An ExpandablePane has a dynamic width and height that automatically updates
 * to the maxX, maxY, minX, and minY values of the furthest entities in it.
 * 
 * @author jimmy
 *
 */
public class ExpandablePane extends Pane
{

	private final int TILE_SIZE = 25;
	private final int DEFAULT_WIDTH = 800;
	private final int DEFAULT_HEIGHT = 600;

	private double width;
	private double height;

	public ExpandablePane()
	{
		super();
		setup();
	}

	/**
	 * Remove all of the entities from within the canvas.
	 */
	public void clear()
	{
		setup();
	}

	/**
	 * Set up the canvas (set all of its entities and displays to the default
	 * ones).
	 */
	private void setup()
	{
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		updateDisplay();
		setupCoordinateTooltip();
	}

	private void setupCoordinateTooltip()
	{
		Tooltip coordinates = new Tooltip();
		setOnMousePressed(e -> {
			if (e.isControlDown()) {
				Node node = (Node) e.getSource();
				coordinates.setText("(" + (int) e.getX() + ", " + (int) e.getY() + ")");
				coordinates.show(node, getScene().getWindow().getX() + e.getSceneX(),
						getScene().getWindow().getY() + e.getSceneY());
			}
		});
		setOnMouseReleased(e -> {
			coordinates.hide();
		});
	}

	/**
	 * Returns the size of each tile in the Canvas.
	 * 
	 * @return canvas tile size.
	 */
	public double getTileSize()
	{
		return TILE_SIZE;
	}

	/**
	 * Add an entity to the top-left corner of the canvas. This method makes a
	 * clone of the given Entity and creates an actual EntityView from it ((The
	 * EntityView is what is actually displayed in the Canvas.)
	 * 
	 * @param entity
	 *            Entity to be added to the canvas.
	 * @return EntityView that is displayed in the Canvas.
	 */
	public void addNode(Node node)
	{
		this.addEntity(node, 0, 0);
	}

	/**
	 * Add an entity to the given x and y position of the canvas. This method
	 * makes a clone of the given Entity and creates an actual EntityView from
	 * it (the EntityView is what is actually displayed in the Canvas)
	 * 
	 * @param entity
	 *            Entity to be added
	 * @param x
	 *            x Position
	 * @param y
	 *            y position
	 * @return EntityView that is displayed in the Canvas.
	 */
	public void addEntity(Node node, double x, double y)
	{
		node.setTranslateX(GridUtil.getTiledCoordinate(x, TILE_SIZE));
		node.setTranslateY(GridUtil.getTiledCoordinate(y, TILE_SIZE));
		this.getChildren().add(node);

		makeDraggable(node);
		updateCanvasBounds();
		updateDisplay();
	}

	/**
	 * Remove the given EntityView from the Canvas.
	 * 
	 * @param entity
	 *            EntityView to be removed from the Canvas.
	 */
	public void removeEntity(Node node)
	{
		if (this.getChildren().contains(node)) {
			this.getChildren().remove(node);
		}
	}

	/**
	 * Bind the scrollbar of the scroll screen to the currently dragged object.
	 * As the object is dragged, the scrollbar will automatically re-adjust so
	 * that the object is in the center of the scroll screen. Moreover, this
	 * method updates the bounds of the canvas as the dragged object moves past
	 * the currently defined bounds.
	 * 
	 * @param entity
	 *            EntityView to bind the scrollbar to.
	 */
	private void makeDraggable(Node node)
	{
		node.translateXProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldX, Number newX)
			{
				if (newX.intValue() + node.getBoundsInLocal().getWidth() > width) {
					updateCanvasBounds();
				} else if (newX.intValue() < 0) {
					updateCanvasBounds();
					shiftNodesX(-1 * newX.intValue());
					node.setTranslateX(0);
				}
				updateDisplay();
			}

		});

		node.translateYProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldY, Number newY)
			{
				if (newY.intValue() + node.getBoundsInLocal().getHeight() > height) {
					updateCanvasBounds();
				} else if (newY.intValue() < 0) {
					updateCanvasBounds();
					shiftNodesY(-1 * newY.intValue());
					node.setTranslateY(0);
				}
				updateDisplay();
			}

		});
	}

	private void shiftNodesX(double xShift)
	{
		for (Node child : this.getChildren()) {
			child.setTranslateX(child.getTranslateX() + xShift);
		}
	}

	private void shiftNodesY(double yShift)
	{
		for (Node child : this.getChildren()) {
			child.setTranslateY(child.getTranslateY() + yShift);
		}
	}

	/**
	 * This method draws the current bounds of the grid and updates the bounds
	 * of the layer.
	 */
	private void updateDisplay()
	{
		this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		this.setPrefHeight(height);
		this.setPrefWidth(width);
	}

	/**
	 * Updates the bounds of the canvas based on the position of the entity
	 * furthest from the origin in each direction (x and y)
	 */
	private void updateCanvasBounds()
	{
		double minX = 0;
		double minY = 0;
		double maxX = width;
		double maxY = height;
		for (Node node : this.getChildren()) {
			double nodeMaxX = node.getTranslateX() + node.getBoundsInParent().getWidth();
			double nodeMaxY = node.getTranslateY() + node.getBoundsInParent().getHeight();
			double nodeMinX = node.getTranslateX();
			double nodeMinY = node.getTranslateY();
			maxX = Math.max(nodeMaxX, maxX);
			maxY = Math.max(nodeMaxY, maxY);
			minX = Math.min(nodeMinX, minX);
			minY = Math.min(minY, nodeMinY);
		}
		this.width = maxX - minX;
		this.height = maxY - minY;
	}

}