package friendstalker.momstalker;

import java.net.URI;
import java.net.URISyntaxException;

import friendstalker.momstalker.Utility.Notification;

/**
 * Created by alcin on 28/05/2017.
 */

public class NotificationClient{
    private static String uri =  "ws://10.0.2.2:7000";
    public static Notification c = null;

    public NotificationClient(){
        try {
            c = new Notification( new URI( uri ));
            c.connectBlocking();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void logIn(String username){
        c.send("/log%" + username);
    }
}
