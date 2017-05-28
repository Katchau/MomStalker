package friendstalker.momstalker;

import java.util.ArrayList;

import friendstalker.momstalker.Utility.Event;
import friendstalker.momstalker.Utility.User;

/**
 * Created by alcin on 27/05/2017.
 */

public class AndroidUser {
    public static User user = null;
    public static ArrayList<User> friends = null;
    public static ArrayList<User> friendRequests = null;
    public static ArrayList<Event> myEvents = null;
    public static ArrayList<Event> events = null;


    public AndroidUser(User user){
        this.user = user;
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
        myEvents = new ArrayList<>();
        events = new ArrayList<>();
    }

    public static void resetVars(){
        user = null;
        friends = null;
        friendRequests = null;
        myEvents = null;
        events = null;
    }
}
