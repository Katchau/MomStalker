package lab5.Handlers;

import lab5.Utility.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Bruno Barros on 14/05/2017.
 */
public class Connection {
    static final String url = "http://localhost:6969";

    public static void main(String[] args){
        int id;
        if (createUser("Loli", "lala"))
            System.out.println("yey");
        if ((id = verifyLogin("Loli", "lala")) != -1)
            System.out.println("yey");
        if (verifyLogin("Loli", "lele") == -1)
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
    }


    public static String connect(String request){
        String response = "";
        try {
            URL myURL = new URL(url + request);
            URLConnection myURLConnection = myURL.openConnection();

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

    public static boolean createUser(String username, String passsword){
        String request = "/createuser?username=" + username + "&password=" + passsword;
        String response = connect(request);

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

    public static boolean updateCoords(int id, double x, double y){
        String request = "/updtcoords?id=" + id + "&x=" + x + "&y="+ y;
        String response = connect(request);

        return response.equals("Success");
    }

    public static int verifyLogin(String username, String passsword){
        String request = "/login?username=" + username + "&password=" + passsword;
        String response = connect(request);

        return Integer.parseInt(response);
    }

    public static boolean createEvent(String name, int idUser, double xCoord, double yCoord){
        String request = "/createevent?name=" + name + "&idhost=" + idUser + "&x=" + xCoord + "&y=" + yCoord;
        String response = connect(request);

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
        String request = "/deleteevent?id=" + id;
        String response = connect(request);

        return response.equals("Success");
    }

    public static boolean createFRequest(int id1, int id2){
        String request = "/createrequest?user1=" + id1 + "&user2=" + id2;
        String response = connect(request);

        return response.equals("Success");
    }

    public static boolean deleteFRequest(int id1, int id2) {
        String request = "/deleterequest?user1=" + id1 + "&user2=" + id2;
        String response = connect(request);

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
        String request = "/addfriend?user1=" + id1 + "&user2=" + id2;
        String response = connect(request);

        return response.equals("Success");
    }

    public static boolean deleteAmizade(int id1, int id2){
        String request = "/deletefriend?user1=" + id1 + "&user2=" + id2;
        String response = connect(request);

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
