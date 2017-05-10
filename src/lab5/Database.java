package lab5;

import lab5.Utility.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by jon on 08/05/2017.
 */
public class Database {
    private Connection conn;
    private Statement stat;

    public static void main(String args[]){
        Database db = new Database();
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
            System.err.println("Hashing Algorithm Error: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Hashing Error: " + e.getMessage());
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
            System.err.println("Getting User error: " + e.getMessage());
        }
        return user;
    }

    public boolean verifyLogin(String username, String password){
        String iPass = "lol";
        String rPass = "meque";
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
            System.err.println("Verify Error: " + e.getMessage());
        }
        return iPass.equals(rPass);//triggered
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

    public boolean execSql(String sql, int id1, int id2, String errorMsg){
        try{
            PreparedStatement insert = conn.prepareStatement(sql);
            insert.setString(1,""+id1);
            insert.setString(2,""+id2);
            insert.addBatch();

            conn.setAutoCommit(false);
            insert.executeBatch();
            conn.setAutoCommit(true);
            insert.close();
        }
        catch(SQLException e){
            System.err.println(errorMsg + e.getMessage());
            return false;
        }
        return true;
    }

    public ArrayList<User> execSql2(String sql, int userId, String errorMsg){
        ArrayList<User> users = new ArrayList<>();
        try{
            PreparedStatement insert = conn.prepareStatement(sql);
            insert.setString(1,"" + userId);
            insert.setString(2,"" + userId);
            ResultSet result = insert.executeQuery();
            while(result.next()){
                int id1 = Integer.parseInt(result.getString("user1"));
                int id2 = Integer.parseInt(result.getString("user2"));
                int id = (id1 != userId) ? id1 : id2;
                User user = getUser(id);
                users.add(user);
            }
            result.close();
            insert.close();
        } catch (SQLException e) {
            System.err.println(errorMsg + e.getMessage());
        }
        return users;
    }


    public boolean createFRequest(int id1, int id2){
        return execSql("insert into FriendRequest(user1, user2) values(?,?)",id1,id2,"Create Request Error: ");
    }

    private boolean deleteFRequest(int id1, int id2){
        return execSql("delete from FriendRequest where user1 = ? and user2 = ?",id1,id2,"Delete Request Error: ");
    }

    public ArrayList<User> getFRequests(int userId){
        return execSql2("select * from FriendRequest where user1 = ? or user2 = ?",userId,"Get Request Error: ");
    }

    public boolean createAmizade(int id1, int id2) {
        return deleteFRequest(id1, id2) && execSql("insert into Amizade(user1, user2) values(?,?)", id1, id2, "Create Amizade Error: ");
    }

    public boolean deleteAmizade(int id1, int id2) {
        return execSql("delete from Amizade where user1 = ? and user2 = ?",id1,id2,"Delete Amizade Error: ");
    }

    public ArrayList<User> getAmizade(int userId){
        return execSql2("select * from Amizade where user1 = ? or user2 = ?",userId,"Get Amizade Error: ");
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
