package authoring.canvas;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

/**
 * A ZoomablePane contains a group of Nodes that are the content of the
 * ZoomablePane. If you scroll with the mouse wheel, then the ZoomablePane
 * either zooms in or out of the entities within this ZoomablePane. Moreover,
 * the content in a ZoomablePane can be panned (can move around the Pane) by
 * dragging the mouse on the Pane itself.
 * 
 * Some code is borrowed from:
 * http://stackoverflow.com/questions/16680295/javafx-correct-scaling
 * http://stackoverflow.com/questions/29506156/javafx-8-zooming-relative-to-mouse-pointer
 * 
 * @author jimmy
 *
 */
public class ZoomablePane extends ScrollPane
{
	private StackPane zoomPane;
	private Group scrollContent;

	public ZoomablePane(Group entities)
	{
		super();
		this.setup(entities);
	}

	private void setup(Group entities)
	{
		zoomPane = new StackPane();
		zoomPane.getChildren().add(entities);
		scrollContent = new Group(zoomPane);
		this.setContent(scrollContent);

		this.setPannable(false);
		this.makeZoomable(entities);
	}

	private void makeZoomable(Group entities)
	{
		final double SCALE_DELTA = 1.1;

		this.viewportBoundsProperty().addListener(new ChangeListener<Bounds>()
		{
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue)
			{
				zoomPane.setMaxSize(newValue.getWidth(), newValue.getHeight());
				zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
			}
		});

		zoomPane.setOnScroll(new EventHandler<ScrollEvent>()
		{
			@Override
			public void handle(ScrollEvent event)
			{
				event.consume();

				if (event.getDeltaY() == 0) {
					return;
				}

				double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

				Point2D scrollOffset = figureScrollOffset(scrollContent, ZoomablePane.this);

				entities.setScaleX(entities.getScaleX() * scaleFactor);
				entities.setScaleY(entities.getScaleY() * scaleFactor);

				repositionScroller(scrollContent, ZoomablePane.this, scaleFactor, scrollOffset);
			}
		});

		final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
		scrollContent.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
			}
		});

		scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				double deltaX = event.getX() - lastMouseCoordinates.get().getX();
				double extraWidth = scrollContent.getLayoutBounds().getWidth()
						- ZoomablePane.this.getViewportBounds().getWidth();
				double deltaH = deltaX * (ZoomablePane.this.getHmax() - ZoomablePane.this.getHmin()) / extraWidth;
				double desiredH = ZoomablePane.this.getHvalue() - deltaH;
				ZoomablePane.this.setHvalue(Math.max(0, Math.min(ZoomablePane.this.getHmax(), desiredH)));

				double deltaY = event.getY() - lastMouseCoordinates.get().getY();
				double extraHeight = scrollContent.getLayoutBounds().getHeight()
						- ZoomablePane.this.getViewportBounds().getHeight();
				double deltaV = deltaY * (ZoomablePane.this.getHmax() - ZoomablePane.this.getHmin()) / extraHeight;
				double desiredV = ZoomablePane.this.getVvalue() - deltaV;
				ZoomablePane.this.setVvalue(Math.max(0, Math.min(ZoomablePane.this.getVmax(), desiredV)));
			}
		});
	}

	private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller)
	{
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		double hScrollProportion = (scroller.getHvalue() - scroller.getHmin())
				/ (scroller.getHmax() - scroller.getHmin());
		double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		double vScrollProportion = (scroller.getVvalue() - scroller.getVmin())
				/ (scroller.getVmax() - scroller.getVmin());
		double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
		return new Point2D(scrollXOffset, scrollYOffset);
	}

	private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset)
	{
		double scrollXOffset = scrollOffset.getX();
		double scrollYOffset = scrollOffset.getY();
		double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
		if (extraWidth > 0) {
			double halfWidth = scroller.getViewportBounds().getWidth() / 2;
			double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
			scroller.setHvalue(
					scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
		} else {
			scroller.setHvalue(scroller.getHmin());
		}
		double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
		if (extraHeight > 0) {
			double halfHeight = scroller.getViewportBounds().getHeight() / 2;
			double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
			scroller.setVvalue(
					scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
		} else {
			scroller.setVvalue(scroller.getVmin());
		}
	}
}
