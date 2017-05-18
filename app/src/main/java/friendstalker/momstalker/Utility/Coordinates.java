package friendstalker.momstalker.Utility;

/**
 * Created by alcin on 08/05/2017.
 */
public class Coordinates {
    public double x;
    public double y;
    public Coordinates(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
