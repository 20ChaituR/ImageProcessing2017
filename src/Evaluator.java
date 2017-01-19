import org.opencv.core.MatOfPoint;

/**
 * 
 */

/**
 * @author joelmanning
 *
 */
public abstract class Evaluator {
	public abstract double calculateError(MatOfPoint a, MatOfPoint b);
}
