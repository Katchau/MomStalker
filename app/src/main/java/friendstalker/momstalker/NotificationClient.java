package friendstalker.momstalker;

import java.net.URI;
import java.net.URISyntaxException;

import friendstalker.momstalker.Utility.Notification;

/**
 * Created by alcin on 28/05/2017.
 */

public class NotificationClient{
    public static String host = "friendstalker.ddns.net";
    public static int port = 7000;
    private static String uri =  "ws://friendstalker.ddns.net:7000";
    public static Notification c = null;
    private static boolean canConnect = false;

    public NotificationClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c = new Notification( new URI( uri ));
                    //c.setConnectionLostTimeout(1);
                    c.connectBlocking();
                    canConnect = true;
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void rebuildUri(){
        uri = "ws://" + host + ":" + port;
    }

    public static void logIn(String username){
        if(c != null && canConnect)
            c.send("/log%" + username);
    }

    public static void logOut(String username){
        if(c != null && canConnect)
            c.send("/logout%" + username);
    }
}
