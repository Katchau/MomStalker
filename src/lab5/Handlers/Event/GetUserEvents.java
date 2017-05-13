package lab5.Handlers.Event;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;
import lab5.Utility.Event;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by jon on 13/05/2017.
 */
public class GetUserEvents extends ClientHandler implements HttpHandler{

    public GetUserEvents(){
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

        int userID = Integer.parseInt(receivedParams.get("id"));

        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        ArrayList<Event> events = db.getEvents(userID);
        db.closeDB();
        String response = "";

        if(events.size() == 0){
            writeResponse(httpExchange,"Sem Eventos",HttpURLConnection.HTTP_NOT_FOUND);
        }
        else{
            for(int i=0; i < events.size(); i++){
                Event e = events.get(i);
                response += "name=" + e.name + "&host=" + e.userHost + "&x=" + e.gps.x + "&y=" + e.gps.y + "\n";
            }
            writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
        }
    }
}
