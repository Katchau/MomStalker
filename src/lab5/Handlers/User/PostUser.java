package lab5.Handlers.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.Database;
import lab5.Handlers.ClientHandler;
import lab5.Utility.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by alcin on 16/05/2017.
 */
public class PostUser extends ClientHandler implements HttpHandler {
    public PostUser(){
        super("id");
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if(!httpExchange.getRequestMethod().equals("POST")){
            System.err.println("Received bad request @GetExample");
            writeResponse(httpExchange,"Bad Request!", HttpURLConnection.HTTP_BAD_METHOD);
        }
        if(!validPostRequest(httpExchange)){
            System.err.println("Received bad Parameters @PostUser");
            writeResponse(httpExchange,"Bad Parameters!",HttpURLConnection.HTTP_NOT_ACCEPTABLE);//no clue what error to pick xD
        }

        int id = Integer.parseInt(receivedParams.get("id"));
        System.out.println("id " + id);

        writeResponse(httpExchange,"lol=cenas",HttpURLConnection.HTTP_OK);
    }
}
