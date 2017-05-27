package lab5.Handlers;

import com.sun.net.httpserver.HttpsServer;
import lab5.Utility.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;

/**
 * Created by Bruno Barros on 14/05/2017.
 */
public class Connection {
    static final String url = "https://localhost:6969";

       static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return hostname.equals("localhost");
        }
    };

    public static void main(String[] args) throws Exception{

        int id;
        System.out.println(id = getUserID("Batatum"));
        User u = getUser(id);
        System.out.println(u.name);

        /*
        if (createUser("Batatum", "lala"))
            System.out.println("yey");
        if ((id = verifyLogin("Batatum", "lala")) != -1)
            System.out.println("yey");
        if (verifyLogin("Batatum", "lele") == -1)
            System.out.println("yey");
        if (updateCoords(1, 2.0, 98.4))
            System.out.println("yey");
        User u = getUser(id);
        System.out.println(u.name);

        if (createEvent("Ola", 1, 2.0,3.4))
            System.out.println("yey");
        Event e = getEvent(2);
        if(e != null)System.out.println(e.name);
        ArrayList<Event> myevents = getEvents(1);
        for (int i = 0; i < myevents.size(); i++){
            System.out.println(myevents.get(i).name);
        }
        System.out.println(" ");
        ArrayList<Event> myfevents = getFriendsEvents(1);
        for (int i = 0; i < myfevents.size(); i++){
            System.out.println(myfevents.get(i).name);
        }
        if (deleteEvent(2))
           System.out.println("Yey");

        if (createFRequest(1, 2))
            System.out.println("yey");
        if (createAmizade(1,3))
            System.out.println("yey");
        ArrayList<User> us = getFRequests(1);
        for (User user : us){
            System.out.println(user.name);
        }
        System.out.println("--");
        ArrayList<User> fs = getAmizades(1);
        for (User user : fs){
            System.out.println(user.name);
        }
        if (deleteFRequest(1, 2))
            System.out.println("yey");
        if (deleteAmizade(1,3))
            System.out.println("yey");
        */
    }

    public static SSLContext createSSL(){
        SSLContext ssl = null;
        try {
            ssl = SSLContext.getInstance("TLS");

            char[] password = "123456".toCharArray();

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("client.keys"), password);

            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(new FileInputStream("truststore"), password);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);

            ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro: " + e.toString());
        } catch (IOException e) {
            System.err.println("Erro: " + e.toString());
        } catch (CertificateException e) {
            System.err.println("Erro Certificado: " + e.toString());
        } catch (UnrecoverableKeyException e) {
            System.err.println("Erro Unrecoverable Key: " + e.toString());
        } catch (KeyStoreException e) {
            System.err.println("Erro Key Store: " + e.toString());
        } catch (KeyManagementException e) {
            System.err.println("Erro Key Management: " + e.toString());
        }

        return ssl;
    }


    public static String connect(String request){
        String response = "";
        try {
            SSLContext ssl = createSSL();
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            URL myURL = new URL(url + request);
            HttpsURLConnection myURLConnection = (HttpsURLConnection) myURL.openConnection();

            myURLConnection.setConnectTimeout(2000);

            BufferedReader in = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response += inputLine;

            in.close();
        }
        catch (MalformedURLException e) {
            System.err.println("Malformed");
        }
        catch (IOException e) {
            System.err.println("Erro: " + e.toString());
        }
        return response;
    }

    public static String postConnect(String link,String request){
        String response = "";
        try {
            SSLContext ssl = createSSL();
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            URL myURL = new URL(url + link);
            HttpsURLConnection myURLConnection = (HttpsURLConnection) myURL.openConnection();

            myURLConnection.setRequestMethod("POST");
            myURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");

            myURLConnection.setConnectTimeout(2000);

            myURLConnection.setDoOutput(true); // Triggers POST.
            try (DataOutputStream output = new DataOutputStream(myURLConnection.getOutputStream())) {
                output.write(request.getBytes("UTF-8"));
                output.flush();
                output.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response += inputLine;

            in.close();
        }
        catch (MalformedURLException e) {
            System.err.println("Malformed");
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: " + e.toString());
        }
        return response;
    }

    public static boolean createUser(String username, String passsword){
        String request = "/createuser";
        String response = postConnect(request, "username=" + username + "&password=" + passsword);

        return response.equals("Success");
    }

    public static User getUser(int id){
        String request = "/getuser?id=" + id;
        String response = connect(request);

        if(response.equals("")){
            return null;
        }

        String[] parts = response.split("&");

        User u = new User(id, parts[0]);

        if (parts.length > 1){
            u.setCoords(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
        }
        return u;
    }

    public static int getUserID(String username){
        String request = "/getiduser?username=" + username;
        String response = connect(request);

        if (response.equals(""))
            return -1;

        return Integer.parseInt(response);
    }

    public static boolean updateCoords(int id, double x, double y){
        String request = "/updtcoords";
        String response = postConnect(request, "id=" + id + "&x=" + x + "&y="+ y);

        return response.equals("Success");
    }

    public static String verifyLogin(String username, String passsword){
        String request = "/login";
        String response = postConnect(request, "username=" + username + "&password=" + passsword);

        return response;
    }

    public static boolean createEvent(String name, int idUser, double xCoord, double yCoord){
        String request = "/createevent";
        String response = postConnect(request, "name=" + name + "&idhost=" + idUser + "&x=" + xCoord + "&y=" + yCoord);

        return response.equals("Success");
    }

    public static Event getEvent(int id){
        String request = "/getevent?id=" + id;
        String response = connect(request);

        if(response.equals("")){
            return null;
        }
        String[] parts = response.split("&");

        Event e = new Event(id, Integer.parseInt(parts[1]), parts[0], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));

        return e;
    }

    public static ArrayList<Event> getEvents(int userId){
        String request = "/myevents?id=" + userId;
        String response = connect(request);

        if (response.equals(""))
            return new ArrayList<Event>();

        ArrayList<Event> res = new ArrayList<Event>();

        String[] events = response.split("%");

        for (int i = 0; i < events.length; i++){
            String[] parts = events[i].split("&");

            Event e = new Event(Integer.parseInt(parts[0]), Integer.parseInt(parts[2]), parts[1], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
            res.add(e);
        }

        return res;
    }

    public static ArrayList<Event> getFriendsEvents(int userId){
        String request = "/friendsevents?id=" + userId;
        String response = connect(request);

        if (response.equals(""))
            return new ArrayList<Event>();

        ArrayList<Event> res = new ArrayList<Event>();

        String[] events = response.split("%");

        for (int i = 0; i < events.length; i++){
            String[] parts = events[i].split("&");

            Event e = new Event(Integer.parseInt(parts[0]), Integer.parseInt(parts[2]), parts[1], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
            res.add(e);
        }

        return res;
    }

    public static boolean deleteEvent(int id){
        String request = "/deleteevent";
        String response = postConnect(request, "id=" + id);

        return response.equals("Success");
    }

    public static boolean createFRequest(int id1, int id2){
        String request = "/createrequest";
        String response = postConnect(request, "user1=" + id1 + "&user2=" + id2);

        return response.equals("Success");
    }

    public static boolean deleteFRequest(int id1, int id2) {
        String request = "/deleterequest";
        String response = postConnect(request, "user1=" + id1 + "&user2=" + id2);

        return response.equals("Success");
    }

    public static ArrayList<User> getFRequests(int userId){
        String request = "/getrequests?user=" + userId;
        String response = connect(request);

        if (response.equals(""))
            return new ArrayList<User>();

        ArrayList<User> res = new ArrayList<User>();

        String[] users = response.split("%");

        for (int i = 0; i < users.length; i++){
            String[] parts = users[i].split("&");

            User u = new User(Integer.parseInt(parts[0]), parts[1]);
            res.add(u);
        }

        return res;
    }

    public static boolean createAmizade(int id1, int id2){
        String request = "/addfriend";
        String response = postConnect(request, "user1=" + id1 + "&user2=" + id2);

        return response.equals("Success");
    }

    public static boolean deleteAmizade(int id1, int id2){
        String request = "/deletefriend";
        String response = postConnect(request, "user1=" + id1 + "&user2=" + id2);

        return response.equals("Success");
    }

    public static ArrayList<User> getAmizades(int userId){
        String request = "/getfriends?user=" + userId;
        String response = connect(request);

        if (response.equals(""))
            return new ArrayList<User>();

        ArrayList<User> res = new ArrayList<User>();

        String[] users = response.split("%");

        for (int i = 0; i < users.length; i++){
            String[] parts = users[i].split("&");

            User u = new User(Integer.parseInt(parts[0]), parts[1]);
            if (parts.length == 4)
                u.setCoords(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            res.add(u);
        }

        return res;
    }
}
