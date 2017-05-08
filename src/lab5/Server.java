package lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server extends Thread{
	private SSLServerSocket s = null;  
	private SSLServerSocketFactory ssf = null;
	
	public static void main(String[] args) {
		new Server(4444).start();
	}
	
	public Server(int port){ 
		ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		 
		try {  
		    s = (SSLServerSocket) ssf.createServerSocket(port);
		    s.setNeedClientAuth(true);
			s.setEnabledCipherSuites(ssf.getDefaultCipherSuites());
		}  
		catch( IOException e) {  
		    System.out.println("Server - Failed to create SSLServerSocket");  
		    e.getMessage();  
		    return;  
		} 
	}
	
	public void start(){
		try (SSLSocket clientSocket = (SSLSocket) s.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));){
            
			String outputLine = "banana";
            out.println(outputLine);
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
            	System.out.println("Received " + inputLine);
            }
			
		}catch (IOException e) {
			System.out.println("god fk");
        }
	        
	}
}
