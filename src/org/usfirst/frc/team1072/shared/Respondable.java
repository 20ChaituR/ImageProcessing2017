/**
 * 
 */
package org.usfirst.frc.team1072.shared;

/**
 * @author joelmanning
 *
 */
public interface Respondable {
	public void send(Object... objs);
	public String getIP();
}
