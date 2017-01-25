/**
 * 
 */
package org.usfirst.frc.team1072.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author joelmanning
 *
 */
public class Server extends Thread {
	
	private ServerSocket socket;
	private Set<Handler> handlers;
	private List<ObjectListener> listeners;
	
	public Server(int port){
		try {
			socket = new ServerSocket(port);
			handlers = new HashSet<Handler>();
			listeners = new ArrayList<ObjectListener>();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		while(true){
			if(socket.isClosed()){
				System.out.println("Server socket closed");
				break;
			}
			try {
				handlers.add(new Handler(socket.accept()));
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void broadcast(Object... objs){
		for(Handler handler: handlers){
			handler.send(objs);
		}
	}
	
	public void addListener(ObjectListener listener){
		listeners.add(listener);
	}
	
	class Handler extends Thread implements Respondable {
		
		private Socket s;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		
		public Handler(Socket s){
			this.s = s;
			try {
				oos = new ObjectOutputStream(s.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(s.getInputStream());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run(){
			while(true){
				if(s.isClosed()){
					handlers.remove(this);
					System.out.println("Connection closed");
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
		
		public void send(Object... objs){
			try {
				for(Object obj: objs){
					oos.writeObject(obj);
				}
				oos.flush();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return s.getPort() * 31 + s.getInetAddress().hashCode();
		}

		/* (non-Javadoc)
		 * @see org.usfirst.frc.team1072.shared.Respondable#getIP()
		 */
		@Override
		public String getIP() {
			return s.getInetAddress().getHostAddress();
		}
	}
	
}
