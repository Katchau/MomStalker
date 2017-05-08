package lab5.Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

/**
 * Created by alcin on 08/05/2017.
 */
public class Barbosa implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange){
        Headers h = httpExchange.getResponseHeaders();
        h.add("Content-Type", "image/gif");
        File file = new File ("barbosa.gif");
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){
            byte [] buffer  = new byte [(int)file.length()];
            bis.read(buffer, 0, buffer.length);
            httpExchange.sendResponseHeaders(200, file.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(buffer,0,buffer.length);
            os.close();
        }
        catch (IOException e){
            System.err.println("Error: at uploading giru");
        }
    }
}
