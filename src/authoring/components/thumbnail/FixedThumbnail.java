package authoring.components.thumbnail;

import javafx.scene.image.Image;

/**
 * This subclass of Thumbnail represents a fixed Thumbnail. This means that its
 * image is set on startup, and will not update automatically. If it needs to be
 * updated, a setImage(String path) method can be used.
 * 
 * @author Elliott Bolzan
 *
 */
public class FixedThumbnail extends Thumbnail {

	private String path;

	/**
	 * Creates a FixedThumbnail.
	 * 
	 * @param path
	 *            the absolute path to the image.
	 * @param width
	 *            the width of the Thumbnail.
	 * @param height
	 *            the height of the Thumbnail.
	 */
	public FixedThumbnail(String path, int width, int height) {
		super(width, height);
		setImage(path);
	}

	/**
	 * Sets the Thumbnail's image.
	 * 
	 * @param path
	 *            the abslute path to the image.
	 */
	public void setImage(String path) {
		this.path = path;
		getImage().set(new Image(path));
	}

	public String getImagePath() {
		return path;
	}

}
