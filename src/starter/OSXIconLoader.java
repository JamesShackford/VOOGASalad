package starter;

import java.net.URL;

import javax.swing.ImageIcon;

import com.apple.eawt.Application;

/**
 * 
 * This class is only loaded if the operating system is Mac OS X. It serves to
 * set the icon - on Apple machines only.
 * 
 * @author Elliott Bolzan
 *
 */
public class OSXIconLoader {

	/**
	 * Load the icon on Mac OS X.
	 * 
	 * @param path
	 *            the URL representing the icon in the filesystem.
	 */
	public OSXIconLoader(URL path) {
		Application.getApplication().setDockIconImage(new ImageIcon(path).getImage());
	}

}
