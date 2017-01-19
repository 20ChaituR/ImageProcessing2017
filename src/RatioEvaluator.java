import org.opencv.core.MatOfPoint;

/**
 * 
 */

/**
 * @author joelmanning
 * determines error in height to width ratios
 */
public class RatioEvaluator extends Evaluator {
	
	private double ar;
	private double br;
	/* (non-Javadoc)
	 * @see Evaluator#calculateError(org.opencv.core.MatOfPoint, org.opencv.core.MatOfPoint)
	 */
	@Override
	public double calculateError(MatOfPoint a, MatOfPoint b) {
		return Math.abs(ar - a.height()/a.width())/ar + Math.abs(br - a.height()/b.width())/br;
	}
	
	public RatioEvaluator() {
		ar = 2.0/5.0;
		br = 2.0/5.0;
	}
}
