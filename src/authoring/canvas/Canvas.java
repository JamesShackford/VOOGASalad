package authoring.canvas;

import java.util.ArrayList;
import java.util.List;

import authoring.Workspace;
import authoring.views.View;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 
 * The Canvas is where you can drag Entities to create your own level. It can
 * contain multiple layers for backgrounds and foregrounds. Each background and
 * foreground can move at their own respective velocities. The Canvas is located
 * in the center of the workspace.
 * 
 * @author jimmy
 *
 */
public class Canvas extends View
{

	private Workspace workspace;
	private final int TILE_SIZE = 25;
	private final int DEFAULT_WIDTH = 100;
	private final int DEFAULT_HEIGHT = 100;

	private Group gridNodes;
	private ScrollPane scrollScreen;
	// private Map<Node, Region> entityRegions;
	private List<EntityDisplay> entities;
	private Pane layer;

	private double width;
	private double height;

	public Canvas(Workspace workspace)
	{
		super(workspace.getResources().getString("CanvasTitle"));
		this.workspace = workspace;
		setup();
	}

	public void setPaneOnMouseClicked(EventHandler<? super MouseEvent> eventHandler)
	{
		layer.setOnMouseClicked(eventHandler);
	}

	private void setup()
	{
		gridNodes = new Group();
		// entityRegions = new HashMap<Node, Region>();
		entities = new ArrayList<EntityDisplay>();
		scrollScreen = createLayer();
		this.setCenter(scrollScreen);
	}

	private ScrollPane createLayer()
	{
		scrollScreen = new ScrollPane();
		layer = new Pane();
		layer.setPrefHeight(height);
		layer.setPrefWidth(width);
		layer.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		layer.getChildren().add(gridNodes);
		scrollScreen.setContent(layer);
		// clickToAddEntity();
		updateDisplay();
		return scrollScreen;
	}

	public void addEntity(ImageView entity)
	{
		this.addEntity(entity, 0, 0);
	}

	public void addEntity(ImageView entity, double x, double y)
	{
		EntityDisplay newEntity = new EntityDisplay(entity, TILE_SIZE, x, y);
		Point2D tiledCoordinate = getTiledCoordinate(x, y);
		newEntity.setTranslateX(tiledCoordinate.getX());
		newEntity.setTranslateY(tiledCoordinate.getY());
		entities.add(newEntity);
		layer.getChildren().add(newEntity);

		makeDraggable(newEntity);
		updateLayerBounds();
		updateDisplay();
	}

	private void drawGrid()
	{
		for (int i = 0; i < width / TILE_SIZE; i++) {
			for (int j = 0; j < height / TILE_SIZE; j++) {
				drawGridDot(i, j);
			}
		}
	}

	private void drawGridDot(double tileX, double tileY)
	{
		Circle gridMarker = new Circle();
		gridMarker.setCenterX(tileX * TILE_SIZE);
		gridMarker.setCenterY(tileY * TILE_SIZE);
		gridMarker.setRadius(1);
		gridMarker.setFill(Color.GREY);
		gridNodes.getChildren().add(gridMarker);
	}

	private void makeDraggable(EntityDisplay entity)
	{
		entity.translateXProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldX, Number newX)
			{
				scrollScreen.setHvalue(newX.doubleValue() / (width - entity.getWidth()));
				if (newX.intValue() < 0) {
					entity.setTranslateX(0);
				} else if (newX.intValue() + entity.getWidth() > width) {
					updateLayerBounds();
				}
				updateDisplay();
			}

		});

		entity.translateYProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldY, Number newY)
			{
				scrollScreen.setVvalue(newY.doubleValue() / (height - entity.getHeight()));
				if (newY.intValue() < 0) {
					entity.setTranslateY(0);
				} else if (newY.intValue() + entity.getHeight() > height) {
					updateLayerBounds();
				}
				updateDisplay();
			}

		});

		entity.minHeightProperty().addListener(e -> {
			updateLayerBounds();
			updateDisplay();
		});
		entity.minWidthProperty().addListener(e -> {
			updateLayerBounds();
			updateDisplay();
		});
	}

	private void updateDisplay()
	{
		updateLayerBounds();
		layer.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		gridNodes.getChildren().clear();
		drawGrid();
		layer.setPrefHeight(height);
		layer.setPrefWidth(width);
	}

	private void updateLayerBounds()
	{
		double maxX = DEFAULT_WIDTH;
		double maxY = DEFAULT_HEIGHT;
		for (EntityDisplay entity : entities) {
			double nodeMaxX = entity.getTranslateX() + entity.getBoundsInParent().getWidth();
			double nodeMaxY = entity.getTranslateY() + entity.getBoundsInParent().getHeight();
			if (nodeMaxX > maxX) {
				maxX = nodeMaxX;
			}
			if (nodeMaxY > maxY) {
				maxY = nodeMaxY;
			}
		}
		this.width = maxX;
		this.height = maxY;
	}

	private Point2D getTiledCoordinate(double x, double y)
	{
		double gridX = ((int) x / TILE_SIZE) * TILE_SIZE;
		double gridY = ((int) y / TILE_SIZE) * TILE_SIZE;
		return new Point2D(gridX, gridY);
	}

}