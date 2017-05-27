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
public class UserID extends ClientHandler implements HttpHandler{

    public UserID(){
        super("username");
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

        String username = receivedParams.get("username");
        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        int id = db.getUserID(username);
        db.closeDB();

        writeResponse(httpExchange,id+"",HttpURLConnection.HTTP_OK);
    }
}
