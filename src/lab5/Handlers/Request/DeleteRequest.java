package lab5.Handlers.Request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by alcin on 13/05/2017.
 */
public class DeleteRequest extends ClientHandler implements HttpHandler {
    public DeleteRequest(){
        super("user1,user2");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //same thing for post just change the method
        if(!httpExchange.getRequestMethod().equals("POST")){
            System.err.println("Received bad request @DeleteRequest");
            writeResponse(httpExchange,"Bad Request!", HttpURLConnection.HTTP_BAD_METHOD);
        }
        if(!validPostRequest(httpExchange)){
            System.err.println("Received bad Parameters @PostUser");
            writeResponse(httpExchange,"Bad Parameters!",HttpURLConnection.HTTP_NOT_ACCEPTABLE);//no clue what error to pick xD
        }

        int user1 = Integer.parseInt(receivedParams.get("user1"));
        int user2 = Integer.parseInt(receivedParams.get("user2"));

        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        String response = (db.deleteFRequest(user1, user2)) ? "Success" : "Fail";
        db.closeDB();
        writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
    }
}
