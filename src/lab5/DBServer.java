package lab5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

import com.sun.net.httpserver.*;
import lab5.Handlers.Barbosa;
import lab5.Handlers.GetExample;
import lab5.Handlers.HandlerTest;

public class DBServer {
	private final int port = 6969;
	public static Database db = null;

	public static void main(String args[]){
		new DBServer();
	}

	public boolean initiateDB(){
		try{
			db = new Database();
		}catch(SQLException e){
			System.err.println("Database Error: " + e.getMessage());
			return false;
		} catch (ClassNotFoundException e) {
			System.err.println("Missing lib for SQLite!");
			return false;
		}
		return true;
	}

	public DBServer(){
		if(!initiateDB())return;
		try{
			HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
			server.createContext("/porn",new HandlerTest());
			server.createContext("/getaids",new GetExample());
			server.createContext("/giru",new Barbosa());
			System.out.println("Server Started!");
			server.start();
		}
		catch(IOException e){
			System.err.println("Server Error: " + e.getMessage());
		}
		db.closeDB();
	}
}