package lab5.Handlers.Event;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by jon on 13/05/2017.
 */
public class DeleteEvent extends ClientHandler implements HttpHandler{

    public DeleteEvent(){
        super("id");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //same thing for post just change the method
        if(!httpExchange.getRequestMethod().equals("POST")){
            System.err.println("Received bad request @DeleteEvent");
            writeResponse(httpExchange,"Bad Request!",HttpURLConnection.HTTP_BAD_METHOD);
        }
        if(!validPostRequest(httpExchange)){
            System.err.println("Received bad Parameters @PostUser");
            writeResponse(httpExchange,"Bad Parameters!",HttpURLConnection.HTTP_NOT_ACCEPTABLE);//no clue what error to pick xD
        }

        int id = Integer.parseInt(receivedParams.get("id"));

        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        String response = (db.deleteEvent(id)) ? "Success" : "Fail";
        db.closeDB();
        writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
    }
}
