package lab5.Utility;

/**
 * Created by jon on 08/05/2017.
 */
public class User {
    public int id;
    public String name;
    public Coordinates gps;

    public User(int id, String name){
        this.id = id;
        this.name = name;
        gps = null;
    }

    public void setCoords(double x, double y){
        gps = new Coordinates(x,y);
    }
}
