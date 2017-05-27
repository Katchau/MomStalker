package lab5;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;

import com.sun.net.httpserver.*;
import lab5.Handlers.*;
import lab5.Handlers.Event.*;
import lab5.Handlers.Friend.AddFriend;
import lab5.Handlers.Friend.DeleteFriend;
import lab5.Handlers.Friend.GetFriends;
import lab5.Handlers.Request.CreateRequest;
import lab5.Handlers.Request.DeleteRequest;
import lab5.Handlers.Request.GetRequests;
import lab5.Handlers.User.*;

import javax.net.ssl.*;

public class DBServer {
	private final int port = 6969;

	public static void main(String args[]){
		new DBServer();
	}

	public DBServer(){
		try{
			HttpsServer server = HttpsServer.create(new InetSocketAddress(port),0);
			SSLContext ssl = SSLContext.getInstance("TLS");

			char[] password = "123456".toCharArray();

			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("server.keys"), password);

			KeyStore ts = KeyStore.getInstance("JKS");
			ts.load(new FileInputStream("truststore"), password);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, password);

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ts);

			ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			server.setHttpsConfigurator (new HttpsConfigurator(ssl) {
				public void configure (HttpsParameters params) {
					try {
						SSLContext c = SSLContext.getDefault();
						SSLEngine engine = c.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(engine.getEnabledCipherSuites());
						params.setProtocols(engine.getEnabledProtocols());

						SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
						params.setSSLParameters(defaultSSLParameters);
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out.println("Failed to create HTTPS server");
					}
				}
			});

			server.createContext("/porn",new HandlerTest());
			server.createContext("/getuser",new GetUser());
			server.createContext("/postuser",new PostUser());
			server.createContext("/createuser",new CreateUser());
			server.createContext("/login",new VerifyLogin());
			server.createContext("/updtcoords",new UpdateCoords());
			server.createContext("/getevent",new GetEvent());
			server.createContext("/createevent",new CreateEvent());
			server.createContext("/deleteevent",new DeleteEvent());
			server.createContext("/friendsevents",new GetFriendsEvents());
			server.createContext("/myevents",new GetUserEvents());
			server.createContext("/getrequests",new GetRequests());
			server.createContext("/createrequest",new CreateRequest());
			server.createContext("/deleterequest",new DeleteRequest());
			server.createContext("/getfriends",new GetFriends());
			server.createContext("/addfriend",new AddFriend());
			server.createContext("/deletefriend",new DeleteFriend());
			server.createContext("/giru",new Barbosa());
			System.out.println("Server Started!");
			server.start();
		}
		catch(IOException e){
			System.err.println("Server Error: " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}
}