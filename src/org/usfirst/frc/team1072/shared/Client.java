/**
 * 
 */
package org.usfirst.frc.team1072.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author joelmanning
 *
 */
public class Client extends Thread implements Respondable {
	
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private List<ObjectListener> listeners;
	
	public Client(String ip, int port){
		try {
			s = new Socket(ip, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream());
			listeners = new ArrayList<ObjectListener>();
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		while(true){
			if(s.isClosed()){
				System.out.println("Socket closed");
				break;
			}
			try {
				Object obj = ois.readObject();
				if(obj != null){
					for(ObjectListener listener: listeners){
						listener.objectRecieved(obj, this);
					}
				}
			} catch(ClassNotFoundException e){
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team1072.shared.Respondable#send(java.lang.Object[])
	 */
	@Override
	public void send(Object... objs) {
		try {
			for(Object obj: objs){
				oos.writeObject(obj);
			}
			oos.flush();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void addListener(ObjectListener listener){
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.usfirst.frc.team1072.shared.Respondable#getIP()
	 */
	@Override
	public String getIP() {
		return s.getInetAddress().getHostAddress();
	}
}
