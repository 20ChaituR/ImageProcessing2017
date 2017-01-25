/**
 * 
 */
package messages;

import java.io.Serializable;

/**
 * @author joelmanning
 *
 */
public class StreamToMe implements Serializable {
	
	
	private int port;

	/**
	 * @param port
	 */
	public StreamToMe(int port) {
		super();
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	
	
}
