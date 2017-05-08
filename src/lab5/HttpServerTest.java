package lab5;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;
import lab5.Handlers.Barbosa;
import lab5.Handlers.HandlerTest;

public class HttpServerTest {
	private final int port = 6969;

	public static void main(String args[]){
		new HttpServerTest();
	}

	public HttpServerTest(){

		try{
			HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
			server.createContext("/porn",new HandlerTest());
			server.createContext("/giru",new Barbosa());
			System.out.println("Server Started!");
			server.start();
		}
		catch(IOException e){
			System.err.println("Server Error: " + e.getMessage());
		}

	}
}