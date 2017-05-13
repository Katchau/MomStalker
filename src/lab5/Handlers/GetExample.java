package lab5.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Created by jon on 13/05/2017.
 */
public class GetExample extends ClientHandler implements HttpHandler{
    public GetExample(){
        super("bananas,cebolas");
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
        String response = "Get example, trying crap";
        //http://localhost:6969/getaids?cebolas=false&bananas=false
        System.out.println("Ola " + receivedParams.get("bananas") + " e " + receivedParams.get("cebolas"));
        writeResponse(httpExchange,response,HttpURLConnection.HTTP_OK);
    }
}
