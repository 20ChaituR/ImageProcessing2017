import java.util.ArrayList;

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.videoio.VideoCapture;


/**
 *
 */

/**
 * @author joelmanning
 *
 */
public class Main {

	public static int width;
	public static int height;

	static VideoCapture cam;
	static GRIPSelection grip;
	static ImageViewer viewer;
	static MainEvaluator eval;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		} catch (Exception e){
			try {
				System.loadLibrary("opencv_java320");
			} catch (Exception e2){
				System.out.println("Could not find an opencv native library");
			}
		}

		Gst.init();

		Pipeline mainPipeline = GstreamerControl.webcamStreamingPipeline();
		mainPipeline.play();

		Caps caps = GstreamerControl.getSinkCaps(mainPipeline.getElementByName("pipesink"));
		String stringCaps = GstreamerControl.makeCommandLineParsable(caps);

		width = caps.getStructure(0).getInteger("width");
		height = caps.getStructure(0).getInteger("height");

		cam = new VideoCapture(GstreamerControl.webcamLoopbackCommand(stringCaps));
		grip = new GRIPSelection();
		viewer = new ImageViewer();
		eval = new MainEvaluator();
		Mat mat = new Mat(height, width, CvType.CV_64FC4);
		while(true){
			cam.read(mat);
			viewer.setImage(mat);
			viewer.repaint();
			grip.process(mat);
			ArrayList<MatOfPoint> contours = grip.getContours();
			if(contours != null)
				for(int i = 0; i < contours.size(); i++){
					for(int j = i + 1; j < contours.size(); j++){
						double error = eval.calculateError(contours.get(i), contours.get(j));
					}
				}
		}
	}

}
