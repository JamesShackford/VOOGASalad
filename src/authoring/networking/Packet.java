package authoring.networking;

import java.io.Serializable;

/**
 * 
 * An abstract class that represents data passed from one machine to another
 * over the network.
 * 
 * If a coder wishes to send new information over the network, he or she must
 * subclass this superclass. Indeed, the Networking class verifies that the
 * information it has been sent can be cast to a Packet before processing it.
 * 
 * @author Elliott Bolzan
 *
 */
public abstract class Packet implements Serializable {

	private static final long serialVersionUID = 5279768333160170194L;

}
