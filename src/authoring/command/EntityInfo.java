package authoring.command;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import data.EntityConverter;
import engine.entities.Entity;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Info needed to represent an entity to the game authoring environment. This
 * information can be sent over a network to communicate the entity to another
 * client. The EntityInfo consists of the actual entity. When this class is
 * serialized, the stored Entity's image is saved and serialized so that it can
 * be sent across a server.
 * 
 * @author jimmy
 *
 */
public class EntityInfo implements Serializable
{
	private static final long serialVersionUID = -5310590949892001516L;
	private transient Entity entity;
	private String extension;
	// private transient Image image;
	private String xmlString;

	public EntityInfo(Entity entity)
	{
		this.entity = entity;
		xmlString = "";
		extension = entity.getImagePath().replaceAll("^.*\\.(.*)$", "$1");
	}

	public Entity getEntity()
	{
		return entity;
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
	{
		s.defaultReadObject();
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new EntityConverter());
		xmlString = (String) s.readObject();
		entity = (Entity) xStream.fromXML(xmlString);

		RenderedImage renderedImage = SwingFXUtils.fromFXImage(SwingFXUtils.toFXImage(ImageIO.read(s), null), null);
		writeImageTempFile(renderedImage);
	}

	private void writeObject(ObjectOutputStream s) throws IOException
	{
		s.defaultWriteObject();
		extension = entity.getImagePath().replaceAll("^.*\\.(.*)$", "$1");
		XStream xStream = new XStream(new DomDriver());
		xStream.registerConverter(new EntityConverter());
		xmlString = xStream.toXML(entity);
		s.writeObject(xmlString);

		ImageIO.write(SwingFXUtils.fromFXImage(new Image(entity.getImagePath()), null), extension, s);
	}

	private void writeImageTempFile(RenderedImage renderedImage) throws IOException
	{
		extension = entity.getImagePath().replaceAll("^.*\\.(.*)$", "$1");
		String tempDir = System.getProperty("java.io.tmpdir");
		File dir = new File(tempDir);
		File imageTemp = File.createTempFile(entity.getName() + "Image", "." + extension, dir);
		ImageIO.write(renderedImage, extension, imageTemp);
		entity.setImagePath("file:" + imageTemp.getAbsolutePath());
	}
}