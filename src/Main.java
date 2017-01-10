import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

/**
 * 
 */

/**
 * @author joelmanning
 *
 */
public class Main {
	
	static Webcam cam;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		cam = Webcam.getDefault();
		while(true){
			BufferedImage bi = cam.getImage();
		}
	}
	
}
