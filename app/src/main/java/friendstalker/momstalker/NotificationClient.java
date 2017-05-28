package friendstalker.momstalker;

import java.net.URI;
import java.net.URISyntaxException;

import friendstalker.momstalker.Utility.Notification;

/**
 * Created by alcin on 28/05/2017.
 */

public class NotificationClient{
    public static String host = "10.0.2.2";
    public static int port = 7000;
    private static String uri =  "ws://10.0.2.2:7000";
    public static Notification c = null;

    public NotificationClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c = new Notification( new URI( uri ));
                    //c.setConnectionLostTimeout(1);
                    c.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void rebuildUri(){
        uri = "ws://" + host + ":" + port;
    }

    public static void logIn(String username){
        if(c != null)
            c.send("/log%" + username);
    }
}
