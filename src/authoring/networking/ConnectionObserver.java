package authoring.networking;

import networking.net.SocketConnection;

/**
 * 
 * Allows the classes implementing it to serve as observers for new connections
 * to their server. In other words, if a class implementing this interface
 * starts a server, it will be notified by the newConnection(SocketConnection
 * connection) method when a new machine connects.
 * 
 * @author Elliott Bolzan
 *
 */
public interface ConnectionObserver {

	/**
	 * 
	 * Called when a new client connects to the server created by this machine.
	 * Information about the created connection is passed through the
	 * SocketConnection parameter in the method signature.
	 * 
	 * @param connection
	 *            the connection that was just created.
	 */
	public void newConnection(SocketConnection connection);

}
