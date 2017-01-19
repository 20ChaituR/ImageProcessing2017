import org.opencv.core.MatOfPoint;

/**
 * 
 */

/**
 * @author joelmanning
 *
 */
public class MainEvaluator extends Evaluator {

	private double[] weights;
	private Evaluator[] subs;
	/* (non-Javadoc)
	 * @see Evaluator#calculateError(org.opencv.core.MatOfPoint, org.opencv.core.MatOfPoint)
	 */
	@Override
	public double calculateError(MatOfPoint a, MatOfPoint b) {
		double error = 0;
		for(int i = 0; i < subs.length; i++){
			error += weights[i] * subs[i].calculateError(a, b);
		}
		return error;
	}
	
	public MainEvaluator(){
		weights = new double[] { 1 };
		subs = new Evaluator[] { new RatioEvaluator() };
	}
	
}
