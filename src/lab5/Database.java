package lab5;

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
        User user = db.getUser(3);
        if(user != null)System.out.println(user.name);
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

    //TODO fazer hash
    public boolean createUser(String name, String password){
        String pass = "";
        try{
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
                System.out.println(pass);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Erro no algoritomo: " + e.getMessage());
            } catch (UnsupportedEncodingException e) {
                System.out.println("Erro ao encriptar password: " + e.getMessage());
            }


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
}
