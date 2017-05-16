package lab5.Handlers.Friend;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;
import lab5.Utility.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by alcin on 13/05/2017.
 */
public class GetFriends extends ClientHandler implements HttpHandler {
    public GetFriends(){
        super("user");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //same thing for post just change the method
        if(!httpExchange.getRequestMethod().equals("GET")){
            System.err.println("Received bad request @GetExample");
            writeResponse(httpExchange,"Bad Request!", HttpURLConnection.HTTP_BAD_METHOD);
        }
        if(!validRequest(httpExchange.getRequestURI().getQuery())){
            System.err.println("Received bad Parameters @GetExample");
            writeResponse(httpExchange,"Bad Parameters!",HttpURLConnection.HTTP_NOT_ACCEPTABLE);//no clue what error to pick xD
        }

        int id = Integer.parseInt(receivedParams.get("user"));
        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        ArrayList<User> friends = db.getAmizades(id);
        db.closeDB();
        if(friends.size() == 0){
            writeResponse(httpExchange,"No Users found!",HttpURLConnection.HTTP_NOT_FOUND);
        }
        else{
            String response = "";
            for(User u : friends){
                response += u.id + "&" + u.name;
                if(u.gps != null)response += "&" + u.gps.x + "&" + u.gps.y;
                response += '%';
            }
            writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
        }
    }
}
