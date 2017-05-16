package lab5.Handlers.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by jon on 13/05/2017.
 */
public class CreateUser extends ClientHandler implements HttpHandler{

    public CreateUser(){
        super("username,password");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //same thing for post just change the method
        if(!httpExchange.getRequestMethod().equals("POST")){
            System.err.println("Received bad request @CreateUser");
            writeResponse(httpExchange,"Bad Request!",HttpURLConnection.HTTP_BAD_METHOD);
        }
        if(!validPostRequest(httpExchange)){
            System.err.println("Received bad Parameters @PostUser");
            writeResponse(httpExchange,"Bad Parameters!",HttpURLConnection.HTTP_NOT_ACCEPTABLE);//no clue what error to pick xD
        }

        String username = receivedParams.get("username");
        String password = receivedParams.get("password"); //não deve ser a melhor maneira passar uma password por aqui

        Database db = initiateDB();
        if(db == null ){
            dbError(httpExchange);
            return;
        }
        String response = (db.createUser(username, password)) ? "Success" : "Fail";
        db.closeDB();
        writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
    }
}
