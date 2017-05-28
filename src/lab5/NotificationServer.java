package lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class NotificationServer extends WebSocketServer{

    static int port = 8887;
    HashMap<String ,WebSocket> users;

    public NotificationServer( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
        users = new HashMap<String,WebSocket>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println( conn + " entered the room!" );
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println( conn + " has left the room!" );
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String[] request = message.split("%");
        String response = "Error loging user " + conn;

        switch(request[0])
        {
            case "/log":
                if(request.length == 2){
                    users.put(request[1], conn);
                    response = "User " + request[1] + " logged";
                    sendToUser(request[1], "Look, there is a notification");
                }
             break;

        }
        System.out.println( response );
    }

    @Override
    public void onFragment( WebSocket conn, Framedata fragment ) {
        System.out.println( "received fragment: " + fragment );
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if( conn != null ) {
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
    }

    public void sendToAll( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }

    public void sendToUser(String username, String text){
        WebSocket user = users.get(username);
        user.send(text + ", " + username);
    }

    public static void main( String[] args ) throws InterruptedException , IOException {
        NotificationServer n = new NotificationServer( port );
        n.start();
    }
}
