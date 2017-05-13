package lab5;

import lab5.Utility.Event;
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

    public Database() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
        stat = conn.createStatement();

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

    public int verifyLogin(String username, String password){
        String iPass = "lol";
        String rPass = "meque";
        int id = -1;
        try{
            PreparedStatement insert = conn.prepareStatement("select * from Users where username = ?");
            insert.setString(1,"" + username);
            ResultSet result = insert.executeQuery();
            if(result.next()){
                iPass = createHash(password);
                rPass = result.getString("password");
                id = Integer.parseInt(result.getString("id"));
            }
            result.close();
            insert.close();
        } catch (SQLException e) {
            System.err.println("Verify Error: " + e.getMessage());
        }
        if (iPass.equals(rPass)){
            return id;
        }
        else return -1;
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

    public boolean deleteFRequest(int id1, int id2){
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

    public ArrayList<User> getAmizades(int userId){
        return execSql2("select * from Amizade where user1 = ? or user2 = ?",userId,"Get Amizade Error: ");
    }

    public boolean createEvent(String name, int idUser, double xCoord, double yCoord){
        try{

            PreparedStatement insert = conn.prepareStatement("insert into Event(uHost, name, xCoord, yCoord) values(?, ?, ?, ?)");
            insert.setString(1, ""+idUser);
            insert.setString(2, name);
            insert.setString(3, ""+xCoord);
            insert.setString(4, ""+yCoord);
            insert.addBatch();

            conn.setAutoCommit(false);
            insert.executeBatch();
            conn.setAutoCommit(true);
            insert.close();
        }
        catch(SQLException e){
            System.err.println("Create Event Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public Event getEvent(int id){
        Event event = null;
        try{
            PreparedStatement insert = conn.prepareStatement("select * from Event where id = ?");
            insert.setString(1,"" + id);
            ResultSet result = insert.executeQuery();
            if(result.next()){
                event = new Event(id,
                        Integer.parseInt(result.getString("uHost")),
                        result.getString("name"),
                        Double.parseDouble(result.getString("xCoord")),
                        Double.parseDouble(result.getString("yCoord")));
            }
            result.close();
            insert.close();
        } catch (SQLException e) {
            System.err.println("Getting event error: " + e.getMessage());
        }
        return event;
    }

    public ArrayList<Event> getEvents(int userId){
        ArrayList<Event> events = new ArrayList<>();
        try{
            PreparedStatement insert = conn.prepareStatement("select * from Event where uhost = ?");
            insert.setString(1,"" + userId);
            ResultSet result = insert.executeQuery();
            while(result.next()){
                Event event = new Event(Integer.parseInt(result.getString("id")),
                        userId,
                        result.getString("name"),
                        Double.parseDouble(result.getString("xCoord")),
                        Double.parseDouble(result.getString("yCoord")));
                events.add(event);
            }
            result.close();
            insert.close();
        } catch (SQLException e) {
            System.err.println("Getting event error: " + e.getMessage());
        }
        return events;
    }

    public ArrayList<Event> getFriendEvents(int userId){
        ArrayList<Event> events = new ArrayList<>();
        String query = "select Event.* from Event inner join Amizade where (user1 = ? and user2 = uhost) or (user2 = ? and user1 = uhost)";
        try{
            PreparedStatement insert = conn.prepareStatement(query);
            insert.setString(1,"" + userId);
            insert.setString(2,"" + userId);
            ResultSet result = insert.executeQuery();
            while(result.next()){
                Event event = new Event(Integer.parseInt(result.getString("id")),
                        Integer.parseInt(result.getString("uHost")),
                        result.getString("name"),
                        Double.parseDouble(result.getString("xCoord")),
                        Double.parseDouble(result.getString("yCoord")));
                events.add(event);
            }
            result.close();
            insert.close();
        } catch (SQLException e) {
            System.err.println("Getting event error: " + e.getMessage());
        }
        return events;
    }

    public boolean deleteEvent(int id){
        try{
            PreparedStatement insert = conn.prepareStatement("delete from event where id = ?");
            insert.setString(1,""+id);
            insert.addBatch();

            conn.setAutoCommit(false);
            insert.executeBatch();
            conn.setAutoCommit(true);
            insert.close();
        }
        catch(SQLException e){
            System.err.println("Deleting event error: " + e.getMessage());
            return false;
        }
        return true;
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
