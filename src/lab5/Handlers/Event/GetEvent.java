package lab5.Handlers.Event;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;
import lab5.Utility.Event;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by jon on 13/05/2017.
 */
public class GetEvent extends ClientHandler implements HttpHandler{

    public GetEvent(){
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
        Event e = db.getEvent(id);
        db.closeDB();

        if(e == null){
            writeResponse(httpExchange,"No such event",HttpURLConnection.HTTP_NOT_FOUND);
        }
        else{
            String response = "" + e.name + "&" + e.userHost + "&" + e.gps.x + "&" + e.gps.y;
            writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
        }
    }
}
