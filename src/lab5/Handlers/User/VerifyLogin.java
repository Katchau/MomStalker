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
public class VerifyLogin extends ClientHandler implements HttpHandler{
    Database db;

    public VerifyLogin(){
        super("username,password");
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

        String username = receivedParams.get("username");
        String password = receivedParams.get("password"); //não deve ser a melhor maneira passar uma password por aqui

        String response = "";

        if (db.verifyLogin(username, password))
            response = "Success";
        else
            response = "Fail";

        //http://localhost:6969/getaids?cebolas=false&bananas=false
        writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
    }
}
