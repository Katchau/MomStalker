package friendstalker.momstalker.Utility;

/**
 * Created by Bruno Barros on 10/05/2017.
 */
public class Event {
    public int id;
    public int userHost;
    public String name;
    public Coordinates gps;
    public boolean active;

    public Event(int id, int userHost, String name, double x, double y){
        this.id = id;
        this.userHost = userHost;
        this.name = name;
        this.gps = new Coordinates(x, y);
        active = false;
    }

    public void changeState(){
        active = !active;
    }
}
