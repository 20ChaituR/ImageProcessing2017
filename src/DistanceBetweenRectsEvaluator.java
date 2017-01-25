import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * @author NathanielMelisso
 *
 */
public class DistanceBetweenRectsEvaluator extends Evaluator {

	/* (non-Javadoc)
	 * @see Evaluator#calculateError(org.opencv.core.MatOfPoint, org.opencv.core.MatOfPoint)
	 */
	@Override
	public double calculateError(MatOfPoint a, MatOfPoint b) {
		Rect ar = Imgproc.boundingRect(a);
		Rect br = Imgproc.boundingRect(b);
    Rect dist = new Rect(a.tl, b.br);
    return Math.abs((dist.getWidth() - 10.25) + (dist.getHeight() - 5));
	}

}
