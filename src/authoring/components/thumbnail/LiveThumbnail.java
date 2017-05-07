package authoring.components.thumbnail;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

/**
 * 
 * This class represents a Thumbnail whose image can update. The updating
 * process functions as follows: a SimpleStringProperty, provided in the
 * constructor, is updated upstream. A listener is added to that property in
 * this class: when the property is changed, its value is used to update the
 * Thumbnail's image.
 * 
 * @author Elliott Bolzan
 *
 */
public class LiveThumbnail extends Thumbnail {

	private SimpleStringProperty path;

	/**
	 * Creates a LiveThumbnail.
	 * 
	 * @param path
	 *            the SimpleStringProperty that represents the Thumbnail's image
	 *            path.
	 * @param width
	 *            the width of the thumbnail.
	 * @param height
	 *            the height of the thumbnail.
	 */
	public LiveThumbnail(SimpleStringProperty path, int width, int height) {
		super(width, height);
		this.path = path;
		path.addListener(e -> setImage());
		setImage();
	}

	private void setImage() {
		if (path.get().startsWith("file:"))
			getImage().set(new Image(path.get()));
	}

}
