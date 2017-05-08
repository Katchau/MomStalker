package lab5;

import lab5.Utility.User;

import java.sql.*;

/**
 * Created by jon on 08/05/2017.
 */
public class Database {
    private Connection conn;
    private Statement stat;

    public static void main(String args[]){
        Database db = new Database();
        User user = db.getUser(1);
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
        try{
            PreparedStatement insert = conn.prepareStatement("insert into Users(username, password) values(?,?)");
            insert.setString(1,name);
            insert.setString(2,password);
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
