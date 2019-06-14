import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("Gardens.db");
            listPlants();
            deletePlant(3);
            insertPlant("Rose", "Thorny stems with flowers", 3);
            updatePlant("Rosey", "Beautiful thorny stems with flowers", 3);
        closeDatabase();
    }

    private static void openDatabase(String dbFile) {
        try  {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

    private static void closeDatabase(){
        try {
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }

    public static void listPlants() {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT plantid, plantname, plantdescription FROM plants");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int plantId = results.getInt(1);
                String plantName = results.getString(2);
                String plantDescription = results.getString(3);
                System.out.println(plantId + " " + plantName + " " + plantDescription);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }

    }

    public static void insertPlant(String plantName, String plantDescription, int plantId){
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO Plants (plantname, plantdescription, plantid) VALUES (?, ?, ?)");
            ps.setString(1, plantName);
            ps.setString(2, plantDescription);
            ps.setInt(3, plantId);
            ps.executeUpdate();
            System.out.println("Record added to plants table");

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error: Something as gone wrong. Please contact the administrator with the error code WC-WA.");
        }
    }



    public static void updatePlant(String plantName, String plantDescription, int plantId) {
        try {

            PreparedStatement ps = db.prepareStatement("UPDATE Plants SET plantName = ?, plantDescription = ? WHERE plantId = ?");
            ps.setString(1, plantName);
            ps.setString(2, plantDescription);
            ps.setInt(3, plantId);
            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    public static void deletePlant (int plantId){
        try {

            PreparedStatement ps = db.prepareStatement("DELETE FROM plants WHERE plantId = ?");
            ps.setInt(1, plantId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }



}
