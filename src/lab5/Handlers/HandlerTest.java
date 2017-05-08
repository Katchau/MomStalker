package lab5.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lab5.HttpServerTest;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jon on 07/05/2017.
 */
public class HandlerTest implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Oh GOD PLS WORK FKKKKK";
        httpExchange.sendResponseHeaders(MSG.OK_CODE, response.length());

        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
