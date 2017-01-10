import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.highgui.VideoCapture;


/**
 * 
 */

/**
 * @author joelmanning
 *
 */
public class Main {
	
	public static final int ROWS = 480;
	public static final int COLS = 640;
	
	static VideoCapture cam;
	static GRIPSelection grip;
	static ImageViewer viewer;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		cam = new VideoCapture(0);
		grip = new GRIPSelection();
		viewer = new ImageViewer();
		Mat mat = new Mat(ROWS, COLS, CvType.CV_64FC4);
		while(true){
			cam.retrieve(mat);
			viewer.setImage(mat);
			grip.process(mat);
			/*ArrayList<MatOfPoint> contours = grip.getContours();
			for(int i = 0; i < contours.size(); i++){
				for(int j = i + 1; j < contours.size(); j++){
					
				}
			}*/
		}
	}
	
}
