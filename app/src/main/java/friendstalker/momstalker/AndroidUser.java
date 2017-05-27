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

    //TODO para testes apagar dp
    public AndroidUser(){
        user = new User(69,"Teste1");
        user.setCoords(69,69);

        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
        friends.add(new User(70,"Teste2"));
        friends.add(new User(71,"Teste3"));

        myEvents = new ArrayList<>();
        myEvents.add(new Event(69,69,"Lmaooo",69,69));
        myEvents.add(new Event(70,69,"Lmaooo2",45,23));

        events = new ArrayList<>();

    }

    public AndroidUser(User user){
        this.user = user;
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
        myEvents = new ArrayList<>();
        events = new ArrayList<>();
    }
}
