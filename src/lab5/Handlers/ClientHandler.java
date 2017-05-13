package lab5.Handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alcin on 13/05/2017.
 *  Pattern p = Pattern.compile("a*b");
 *  Matcher m = p.matcher("aaaaab");
 *  boolean b = m.matches();
 */
public class ClientHandler{
    protected ArrayList<String> expectedParams;
    protected HashMap<String, String> receivedParams;
    protected Pattern value = Pattern.compile("(?<==).*");
    protected Pattern equalP = Pattern.compile(".*(?==)");

    protected ClientHandler(String params){
        expectedParams = new ArrayList<>(Arrays.asList(params.split(",")));
    }

    protected boolean validRequest(String request){
        ArrayList<String> params = new ArrayList<>(Arrays.asList(request.split("&")));
        receivedParams = new HashMap<>();
        if(params.size() != expectedParams.size())
            return false;
        for (String s : params) {
            Matcher m = equalP.matcher(s);
            String p;
            if(m.find()){
                if(!expectedParams.contains(m.group()))
                    return false;
                else p = m.group();
            }
            else return false;
            m = value.matcher(s);
            if(m.find()){
                receivedParams.put(p,m.group());
            }
            else return false;
        }
        return true;
    }

    protected void writeResponse(HttpExchange httpExchange, String response, int errorCode) throws IOException{
        httpExchange.sendResponseHeaders(errorCode, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
