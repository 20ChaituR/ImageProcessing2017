/**
 * 
 */
package org.usfirst.frc.team1072.shared;

/**
 * @author joelmanning
 *
 */
public class Networking {
	
	public static final int TEAM_NUMBER = 1072;
	public static final String BRIDGE_IP = generateIP(1);
	public static final String ROBORIO_IP = generateIP(2);
	public static final String RASPI_IP = generateIP(6);
	public static final int ROBORIO_PORT = 5800;
	public static final int RASPI_REGISTER = 5999;
	public static final int RASPI_STREAM = 6000;
	public static final int RASPI_PROCESSED = 6001;
	
	public static String generateIP(int num){
		return "127.0.0.1";//"10." + TEAM_NUMBER / 100 + '.' + TEAM_NUMBER % 100 + '.' + num;
	}
}
