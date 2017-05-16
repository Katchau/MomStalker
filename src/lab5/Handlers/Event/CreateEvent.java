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
public class CreateEvent extends ClientHandler implements HttpHandler{

    public CreateEvent(){
        super("name,idhost,x,y");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //same thing for post just change the method
        if(!httpExchange.getRequestMethod().equals("POST")){
            System.err.println("Received bad request @CreateEvent");
            writeResponse(httpExchange,"Bad Request!",HttpURLConnection.HTTP_BAD_METHOD);
        }
        if(!validPostRequest(httpExchange)){
            System.err.println("Received bad Parameters @PostUser");
            writeResponse(httpExchange,"Bad Parameters!",HttpURLConnection.HTTP_NOT_ACCEPTABLE);//no clue what error to pick xD
        }

        String name = receivedParams.get("name");
        int idUser = Integer.parseInt(receivedParams.get("idhost"));
        double x = Double.parseDouble(receivedParams.get("x"));
        double y = Double.parseDouble(receivedParams.get("y"));

        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        String response = (db.createEvent(name, idUser, x, y)) ? "Success" : "Fail";
        db.closeDB();
        writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
    }
}
