import java.util.HashMap;

import messages.StreamToMe;

import org.freedesktop.gstreamer.Pipeline;
import org.usfirst.frc.team1072.shared.Networking;
import org.usfirst.frc.team1072.shared.ObjectListener;
import org.usfirst.frc.team1072.shared.Respondable;
import org.usfirst.frc.team1072.shared.Server;

/**
 * 
 */

/**
 * @author joelmanning
 *
 */
public class GStreamServerMain implements ObjectListener {
	
	private Server registration;
	private HashMap<String, Pipeline> pipes;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new GStreamServerMain().run();
	}

	/**
	 * 
	 */
	private void run() {
		pipes = new HashMap<String, Pipeline>();
		registration = new Server(Networking.RASPI_REGISTER);
		registration.addListener(this);
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team1072.shared.ObjectListener#objectRecieved(java.lang.Object, org.usfirst.frc.team1072.shared.Respondable)
	 */
	@Override
	public void objectRecieved(Object obj, Respondable source) {
		if(obj instanceof StreamToMe){
			int port = ((StreamToMe) obj).getPort();
			String ip = source.getIP();
			if(pipes.containsKey(ip)){
				return;
			}
			pipes.put(ip, GstreamerControl.webcamStreamingPipeline(ip, port));
		}
	}
	
}
