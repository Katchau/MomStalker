package lab5.Handlers;

import com.sun.net.httpserver.HttpExchange;
import lab5.Database;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
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

    protected Database initiateDB(){
        try{
            Database db = new Database();
            return db;
        }catch(SQLException e){
            System.err.println("Database Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Missing lib for SQLite!");
        }
        return null;
    }

    protected void dbError(HttpExchange httpExchange) throws IOException{
        writeResponse(httpExchange,"Couldn't access db!", HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    protected boolean validPostRequest(HttpExchange httpExchange) throws IOException{
        String response = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response += inputLine;
        return validRequest(response);
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
