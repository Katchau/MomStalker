package lab5;

import lab5.Utility.Coordinates;
import lab5.Utility.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * Created by jon on 08/05/2017.
 */
public class Database {
    private Connection conn;
    private Statement stat;

    public static void main(String args[]){
        Database db = new Database();

        db.updateCoordinates(1, 2.0, 3.0);
        User u = db.getUser(1);
        System.out.println(u.name);
        System.out.println("x=" + u.gps.x + " y=" + u.gps.y);

        db.closeDB();
    }

    public Database(){
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
            stat = conn.createStatement();
        }
        catch(SQLException e){
            System.err.println("Database Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Missing lib for SQLite!");
        }
    }

    private String createHash(String password){
        String pass = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-16"));
            byte[] byteData = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            pass = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erro no algoritomo: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Erro ao encriptar password: " + e.getMessage());
        }
        return pass;
    }

    public boolean createUser(String name, String password){
        try{
            String pass = createHash(password);
            PreparedStatement insert = conn.prepareStatement("insert into Users(username, password) values(?,?)");
            insert.setString(1,name);
            insert.setString(2,pass);
            insert.addBatch();

            conn.setAutoCommit(false);
            insert.executeBatch();
            conn.setAutoCommit(true);
            insert.close();
        }
        catch(SQLException e){
            System.err.println("Create User Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public User getUser(int id){
        User user = null;
        try{
            PreparedStatement insert = conn.prepareStatement("select * from Users where id = ?");
            insert.setString(1,"" + id);
            ResultSet result = insert.executeQuery();
            if(result.next()){
                user = new User(Integer.parseInt(result.getString("id")),result.getString("username"));
                String xC = result.getString("xCoord");

                if(xC != null){
                    double x = Double.parseDouble(xC);
                    double y = Double.parseDouble(result.getString("yCoord"));
                    user.setCoords(x,y);
                }
            }
            result.close();
            insert.close();
        } catch (SQLException e) {
            System.err.println("Select Error: " + e.getMessage());
        }
        return user;
    }

    public void closeDB(){
        try {
            stat.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error: Closing DB");
        }
    }

    public boolean verifyLogin(String username, String password){
        String iPass = "";
        String rPass = "";
        try{
            PreparedStatement insert = conn.prepareStatement("select * from Users where username = ?");
            insert.setString(1,"" + username);
            ResultSet result = insert.executeQuery();
            if(result.next()){
                iPass = createHash(password);
                rPass = result.getString("password");
            }
            result.close();
            insert.close();
        } catch (SQLException e) {
            System.err.println("Select Error: " + e.getMessage());
        }
        if (iPass.equals(rPass))
            return true;
        return false;
    }

    public boolean updateCoordinates(int id, double x, double y){
        try{
            PreparedStatement update = conn.prepareStatement("update Users set xCoord = ? , yCoord = ? where id = ?");

            update.setString(1, "" + x);
            update.setString(2, "" + y);
            update.setString(3, "" + id);

            conn.setAutoCommit(false);
            update.executeUpdate();
            conn.setAutoCommit(true);
            update.close();
        }
        catch(SQLException e){
            System.err.println("Update Coordinates Error: " + e.getMessage());
            return false;
        }
        return true;
    }
}
