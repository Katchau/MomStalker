package lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Client extends Thread{
	SSLSocket s = null;  
	SSLSocketFactory ssf = null; 
	
	public static void main(String[] args) {
		new Client("127.0.0.1",4444).start();
	}
	
	public Client(String ip, int port){
		ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();  
		 
		try {  
			InetAddress address = InetAddress.getByName(ip);
		    s = (SSLSocket) ssf.createSocket(address,port);
		    s.setEnabledCipherSuites(ssf.getDefaultCipherSuites());
		}  
		catch( IOException e) {  
		    System.out.println("Client - Failed to create SSLSocket");  
		    e.getMessage();  
		    return; 
		}
		
	}
	
	public void start(){
		try (PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));){
			
			String fromServer, fromUser;
			while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                fromUser = "ora bolas";
                out.println(fromUser);
            }
			
		}catch(IOException e){
			System.out.println("fk god");
		}
	}
	
}
