package lab5.Handlers.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;
import lab5.Utility.User;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;

/**
 * Created by jon on 13/05/2017.
 */
public class UpdateCoords extends ClientHandler implements HttpHandler{
    Database db;

    public UpdateCoords(){
        super("id,x,y");
        try {
            this.db = new Database();
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Missing lib for SQLite!");
        }
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
        double x = Double.parseDouble(receivedParams.get("x"));
        double y = Double.parseDouble(receivedParams.get("y"));

        String response = "";

        if (db.updateCoordinates(id, x, y))
            response = "Success";
        else
            response = "Fail";

        //http://localhost:6969/getaids?cebolas=false&bananas=false
        writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
    }
}
