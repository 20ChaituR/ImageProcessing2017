import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * @author joelmanning
 *
 */
public class RectangularityEvaluator extends Evaluator {

	/* (non-Javadoc)
	 * @see Evaluator#calculateError(org.opencv.core.MatOfPoint, org.opencv.core.MatOfPoint)
	 */
	@Override
	public double calculateError(MatOfPoint a, MatOfPoint b) {
		Rect ar = Imgproc.boundingRect(a);
		Rect br = Imgproc.boundingRect(b);
		double aa = ar.area();
		double ba = ar.area();
		return Math.abs(aa - Imgproc.contourArea(a))/aa + Math.abs(ba - Imgproc.contourArea(b))/ba;
	}

}
