import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Scanner;

public class SQLServerProject {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) throws Exception{

        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password="+ password +";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        // startup sequence
        MyDatabase db = new MyDatabase(connectionUrl);
        runConsole(db);
    }

    public static void runConsole(MyDatabase db) {

        Scanner console = new Scanner(System.in);
        System.out.print("Welcome! Type h for help. ");
        System.out.print("db > ");
        String line = console.nextLine();
        String[] parts;
        String arg = "";

        while (line != null && !line.equals("q")) {
            parts = line.split("\\s+");
            if (line.indexOf(" ") > 0)
                arg = line.substring(line.indexOf(" ")).trim();

            if (parts[0].equals("h"))
                printHelp();
            else if (parts[0].equals("r")) {
                db.getRoute();
            }

            else if (parts[0].equals("b")) {
                db.getBus();
            }

            else if (parts[0].equals("s")) {
                db.getStop();
            }

            else if (parts[0].equals("sched")) {
                db.getSchedule();
            }

            else if (parts[0].equals("a")) {
                db.getActivity();
            }

            else if (parts[0].equals("pu")) {
                db.getPassUp();
            }

            else
                System.out.println("Read the help with h, or find help somewhere else.");

            System.out.print("db > ");
            line = console.nextLine();
        }

        console.close();
    }

    private static void printHelp() {
        System.out.println("Library database");
        System.out.println("Commands:");
        System.out.println("r - List all routes and name");
        System.out.println("b - List all buses and destination");
        System.out.println("s - List all bus stops and location");
        System.out.println("sched - List all schedules, start and end date");
        System.out.println("a - List all Activities");
        System.out.println("pu - List all Pass Up");
        System.out.println("");

        System.out.println("q - Exit the program");

        System.out.println("---- end help ----- ");
    }
}

class MyDatabase {
    private Connection connection;

    public MyDatabase(String url) {
        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    public void getRoute() {
        try {
            String sql = "SELECT * FROM Route";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Route Name: " + resultSet.getString("routeName"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getBus() {
        try {
            String sql = "SELECT * FROM Bus";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Destination: " + resultSet.getString("destination"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getStop() {
        try {
            String sql = "SELECT * FROM BusStop";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Stop Number: " + resultSet.getString("stopNum") +
                        ", Location: " + resultSet.getString("location"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getSchedule() {
        try {
            String sql = "SELECT * FROM Schedule";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Start Date: " + resultSet.getDate("startDate") +
                        ", End Date: " + resultSet.getDate("endDate"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getActivity() {
        try {
            String sql = "SELECT * FROM Activity";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("routeNum") +
                        ", Alighting Number: " + resultSet.getInt("stopNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getPassUp() {
        try {
            String sql = "SELECT * FROM PassUp";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Location: " + resultSet.getString("location") +
                        ", Time: " + resultSet.getDate("time") +
                        ", Pass Up Type: " + resultSet.getString("type"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }
}
