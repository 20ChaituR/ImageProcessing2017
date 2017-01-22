import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Fraction;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.Structure.InvalidFieldException;

/**
 * @author Ryan Adolf
 */
public class GstreamerControl {

	public static final String SOCKET_PATH = "/tmp/foo";
	public static final String STREAM_HOST = "169.254.84.224";
	public static final int STREAM_PORT = 5001;

	/**
	 * Creates the Gstreamer pipeline that takes in the vision webcam stream and
	 * outputs both an h.264-encoded stream and a raw stream to a shared memory
	 * location.
	 */
	public static Pipeline webcamStreamingPipeline() {
		return Pipeline.launch(// Take in stream from webcam
							   "v4l2src ! video/x-raw, width=640,height=480 ! "
							   // Copy the stream to two different outputs
							 + "tee name=t ! queue ! "
							   // Encode one output to h.264
							 + "omxh264enc ! omxh264enc ! h264parse ! "
							   // Convert to rtp packets
							 + "rtph264pay pt=96 config-interval=5 ! "
							   // Stream over udp
							 + "udpsink host="+STREAM_HOST+" port="+STREAM_PORT+" "
							   // Use other output
							 + "t. ! queue ! "
							   // Put output in a shared memory location
							 + "shmsink name=pipesink socket-path=" + SOCKET_PATH + " "
							 + "sync=true wait-for-connection=false shm-size=10000000"
		);
	}

	/**
	 * Creates the command used by opencv to parse the raw video from the shared
	 * memory location, given capture filters.
	 * <p>
	 * These capture filters are needed as opencv needs to know how to parse
	 * what is at the memory location (e.g. width and height).
	 */
	public static String webcamLoopbackCommand(String caps) {
		return ("shmsrc socket-path=" + SOCKET_PATH + " ! "
			  + caps + " ! "
			  + "videoconvert"
		);
	}

	/**
	 * Returns the negotiated capture filters of a given element's sink
	 * connection (i.e. what is outputted by the element).
	 * <p>
	 * Because these capture filters will be negotiated, this method must be
	 * used after the pipeline is playing.
	 */
	public static Caps getSinkCaps(Element element) {
		return element.getStaticPad("sink").getNegotiatedCaps();
	}

	/**
	 * Converts a Fraction to a String since for some reason it doesn't override
	 * it's toString() method.
	 */
	public static String getFractionString(Fraction fraction) {
		return fraction.getNumerator() + "/" + fraction.getDenominator();
	}

	/**
	 * For some reason <code>getValue(name)</code> works for every data type
	 * except fractions, so a bit more work needs to be done to get a capture
	 * filter by its name.
	 */
	public static Object getCapValueByName(Structure caps, String name) {
		try {
			return getFractionString(caps.getFraction(name));
		} catch (InvalidFieldException e) {
			return caps.getValue(name);
		}
	}

	/**
	 * A Cap object's toString method returns something very close to what this
	 * method returns, but the outputted string also contains types in
	 * parenthesis (e.g. <code>width=(int)320</code>). This method returns that
	 * string, but without the type and parenthesis.
	 * <p>
	 * One could just use regex to find all ocurrences of \(.*?\), but that
	 * would run into problems if the enclosed strings contained parenthesis.
	 */
	public static String makeCommandLineParsable(Caps caps) {
		Structure struct = caps.getStructure(0);
		String out = struct.getName(); // This will be the video type, most likely video/x-raw

		for (int i = 0; i < struct.getFields(); i++) {
			String capName = struct.getName(i);
			Object capValue = getCapValueByName(struct, capName);

			out += ", " + capName + "=" + capValue;
		}

		return out;
	}
}
