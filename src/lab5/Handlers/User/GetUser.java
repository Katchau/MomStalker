package lab5.Handlers.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;
import lab5.Utility.User;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by jon on 13/05/2017.
 */
public class GetUser extends ClientHandler implements HttpHandler{

    public GetUser(){
        super("id");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //same thing for post just change the method
        if(!httpExchange.getRequestMethod().equals("GET")){
            System.err.println("Received bad request @GetExample");
            writeResponse(httpExchange,"Bad Request!",HttpURLConnection.HTTP_BAD_METHOD);
        }
        if(!validRequest(httpExchange.getRequestURI().getQuery())){
            System.err.println("Received bad Parameters @GetExample");
            writeResponse(httpExchange,"Bad Parameters!",HttpURLConnection.HTTP_NOT_ACCEPTABLE);//no clue what error to pick xD
        }

        int id = Integer.parseInt(receivedParams.get("id"));
        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        User u = db.getUser(id);
        db.closeDB();
        if(u == null){
            writeResponse(httpExchange,"No such user",HttpURLConnection.HTTP_NOT_FOUND);
        }
        else{
            String response = u.name;
            if(u.gps != null)response += "&" + u.gps.x + "&" + u.gps.y;
            writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
        }
    }
}
