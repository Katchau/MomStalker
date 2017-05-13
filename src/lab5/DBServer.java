package lab5;

import java.io.IOException;
import java.net.InetSocketAddress;

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

public class DBServer {
	private final int port = 6969;

	public static void main(String args[]){
		new DBServer();
	}

	public DBServer(){
		try{
			HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
			server.createContext("/porn",new HandlerTest());
			server.createContext("/getuser",new GetUser());
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
		}
	}
}