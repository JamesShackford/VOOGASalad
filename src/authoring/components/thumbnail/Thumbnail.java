package authoring.components.thumbnail;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.views.View;

/**
 * 
 * This abstract class serves to encapsulate a specific and oft-used ImageView
 * implementation: that of the Thumbnail. Thumbnails can be created with a
 * specific size. Their image property is bound, so that the thumbnail can
 * update if necessary.
 * 
 * There are two classes that extend this superclass: FixedThumbnail and
 * LiveThumbnail. As their names indicate, the first class represents a static
 * thumbnail, while the second class represents a thumbnail that can update.
 * 
 * @author Elliott Bolzan
 *
 */
public abstract class Thumbnail extends View {

	private SimpleObjectProperty<Image> image;
	private ImageView imageView = new ImageView();

	/**
	 * Creates a Thumbnail.
	 * 
	 * @param width
	 *            the thumbnail's width.
	 * @param height
	 *            the thumbnail's height.
	 */
	public Thumbnail(int width, int height) {
		image = new SimpleObjectProperty<Image>();
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		imageView.setPreserveRatio(true);
		imageView.imageProperty().bind(image);
		setCenter(imageView);
		setMinSize(0, 0);
	}

	public SimpleObjectProperty<Image> getImage() {
		return image;
	}

}
