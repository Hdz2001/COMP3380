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
        System.out.println("Welcome! Type h for help. ");
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
            
            // ROUTE
            else if (parts[0].equals("r")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getRoute();
                } else {
                    System.out.println("Executing...");
                    db.getRoute(parts[1]);
                }
            }

            else if (parts[0].equals("rn")) {
                System.out.println("Executing...");
                String[] clone = new String[parts.length-1];

                for (int i=0; i<clone.length; i++)
                {
                    clone[i] = parts[i+1];
                }

                db.getRouteNumber(String.join(" ", clone));
            }

            // BUS
            else if (parts[0].equals("b")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getBus();
                } else {
                    System.out.println("Executing...");
                    db.getBus(parts[1]);
                }
            }

            else if (parts[0].equals("d")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getDestination();
                } else {
                    System.out.println("Executing...");

                    String[] clone = new String[parts.length-1];

                    for (int i=0; i<clone.length; i++)
                    {
                        clone[i] = parts[i+1];
                    }

                    db.getDestination(String.join(" ", clone));
                }
            }

            // BUS-STOP

            else if (parts[0].equals("s")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getStop();
                } else {
                    System.out.println("Executing...");
                    db.getStop(parts[1]);
                }
            }

            // SCHEDULE

            else if (parts[0].equals("sched")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getSchedule();
                } else if (parts.length == 2){
                    System.out.println("Executing...");
                    db.getSchedule(parts[1]);
                }
                else {
                    System.out.println("Executing...");
                    String scheInput = parts[1] + " " + parts[2];
                    db.getSchedule(scheInput);
                }

            }

            // ACTIVITY

            else if (parts[0].equals("a")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getActivity();
                } else {
                    System.out.println("Executing...");
                    db.getActivity(parts[1]);
                }
            }

            else if (parts[0].equals("as")) {
                System.out.println("Executing...");
                db.getActivityStopNum(parts[1]);   
            }

            else if (parts[0].equals("ac")) {
                System.out.println("Executing...");
                db.getEachTotalActivity();   
            }

            else if (parts[0].equals("at")) {
                if (parts.length == 2){
                    System.out.println("Executing...");
                    db.getActivityScheTime(parts[1]);
                }
                else {
                    System.out.println("Executing...");
                    String scheInput = parts[1] + " " + parts[2];
                    db.getActivityScheTime(scheInput);
                }

            }

            else if (parts[0].equals("ad")) {
                System.out.println("Executing...");
                db.getActivityDayType(parts[1]);   
            }

            // ARRIVE

            else if (parts[0].equals("ar")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getArrive();
                } else {
                    System.out.println("Executing...");
                    db.getArrive(parts[1]);
                }
            }

            else if (parts[0].equals("ard")) {
                System.out.println("Executing...");
                db.getArriveDeviation();
            }

            else if (parts[0].equals("art")) {
                System.out.println("Executing...");
                db.getArriveTotal();
            }

            // PASSUP

            else if (parts[0].equals("pu")) {
                if (parts.length == 1){
                    System.out.println("Executing...");
                    db.getPassUp();
                } else {
                    System.out.println("Executing...");
                    db.getPassUp(parts[1]);
                }
            }

            else if (parts[0].equals("puy")) {
                System.out.println("Executing...");
                db.getPassUpTime(parts[1]);
            }

            //COMPLICATED QUERIES
            else if (parts[0].equals("rinfo")) {
                System.out.println("Executing...");
                db.getRouteInfo(parts[1]);
            }

            else
                System.out.println("Read the help with h, or find help somewhere else.");

            System.out.print("db > ");
            line = console.nextLine();
        }

        console.close();
    }

    private static void printHelp() {
        System.out.println("TRANSIT DATABASE");
        System.out.println("");

        System.out.println("Commands:");

        System.out.println("---- Route table ----");
        System.out.println("r - List all routes and name");
        System.out.println("r [route number] - List route and name for a specific route");
        System.out.println("rn [route name] - List route and name for a specific route name - SPACE NEEDED BETWEEN VALUES");
        System.out.println("~ For example: rn St. Norbert / rn Kenaston/ rn St. Mary's Express");
        System.out.println("");

        System.out.println("---- Bus table ----");
        System.out.println("b - List all buses and destination");
        System.out.println("b [bus number]- List bus and destination for a specific bus");
        System.out.println("d - List all distinct destination");
        System.out.println("d [destination] - List all buses going to a specific destination - SPACE NEEDED BETWEEN VALUES");
        System.out.println("~ For example: d Polo Park / d To The Forks/ d Downtown (City Hall)");
        System.out.println("");

        System.out.println("---- BusStop table ----");
        System.out.println("s - List all bus stops and location");
        System.out.println("s [stop number]- List bus stop and location for a specific bus stop");
        System.out.println("");

        System.out.println("---- Schedule table ----");
        System.out.println("sched - List all schedules, start and end date");
        System.out.println("sched [input] - List all schedules from specific time period - it can be either season, year, or both - SPACE NEEDED BETWEEN VALUES");
        System.out.println("~ For example: sched 2018 / sched Fall / sched Fall 2018");
        System.out.println("");

        System.out.println("---- Activity table ----");
        System.out.println("a - List all Activities");
        System.out.println("ac - Count the total number of activities for each route");
        System.out.println("a [route number] - List all Activities for a specific route");
        System.out.println("as [stop number] - List all Activities for a specific stop");
        System.out.println("ad [day type] - List all Activities for a specific dayType and sort the results by timePeriod");
        System.out.println("~ For example: ad Weekday / ad Saturday / ad Sunday");
        System.out.println("at [input] - List all activities from specific time period - it can be either season, year, or both - SPACE NEEDED BETWEEN VALUES");
        System.out.println("~ For example: at 2018 / at Fall / at Fall 2018");
        System.out.println("");

        System.out.println("---- Arrive table ----");
        System.out.println("ar - List all Arrive");
        System.out.println("ar [bus number] - List all Arrive for a specific bus");
        System.out.println("ard - List all instances where a bus deviated from the schedule");
        System.out.println("art - Retrieve all unique bus-stop pairs with their total number of arrivals");
        System.out.println("");

        System.out.println("---- PassUp table ----");
        System.out.println("pu - List all Pass Up and sort by type");
        System.out.println("pu [route number] - List all Pass Up for a specific route ordered by time DESC");
        System.out.println("puy [year] - List all Pass Up for a specific year");
        System.out.println("");

        System.out.println("---- Complicated Queries ----");
        System.out.println("rinfo [route number] - List all information about a specific route");
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

    // ROUTE 
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

    public void getRoute(String num) {
        try {
            String sql = "SELECT * FROM Route WHERE routeNum = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Route Name: " + resultSet.getString("routeName"));
            } else {
                System.out.println("Not found.");
            }
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

    public void getRouteNumber(String num) {
        try {
            String sql = "SELECT * FROM Route WHERE routeName = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Route Name: " + resultSet.getString("routeName"));
            } else {
                System.out.println("Not found.");
            }
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

    // BUS
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

    public void getBus(String num) {
        try {
            String sql = "SELECT * FROM Bus WHERE busNum = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Destination: " + resultSet.getString("destination"));
            } else {
                System.out.println("Not found.");
            }
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

    public void getDestination() {
        try {
            String sql = "SELECT DISTINCT destination FROM Bus;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Destination: " + resultSet.getString("destination"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getDestination(String dest) {
        try {
            String sql = "SELECT * FROM Bus WHERE destination = ?;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dest);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Destination: " + resultSet.getString("destination"));
            } else {
                System.out.println("Not found.");
            }
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

    // BUS-STOP
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

    public void getStop(String num) {
        try {
            if(!isNumber(num)){
                System.out.println("Not stop number");
                return;
            }
            String sql = "SELECT * FROM BusStop WHERE stopNum = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.valueOf(num));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Stop Number: " + resultSet.getString("stopNum") +
                        ", Location: " + resultSet.getString("location"));
            } else {
                System.out.println("Not found.");
            }
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

    // SCHEDULE
    public void getSchedule() {
        try {
            String sql = "SELECT * FROM Schedule";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Start Date: " + resultSet.getString("startDate") +
                        ", End Date: " + resultSet.getString("endDate"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getSchedule(String num) {
        try {
            String sql = "SELECT * FROM Schedule WHERE scheName LIKE '%" + num + "%'";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Start Date: " + resultSet.getString("startDate") +
                        ", End Date: " + resultSet.getString("endDate"));
            } else {
                System.out.println("Not found.");
            }

            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Start Date: " + resultSet.getString("startDate") +
                        ", End Date: " + resultSet.getString("endDate"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // ACTIVITY
    public void getActivity() {
        try {
            String sql = "SELECT * FROM Activity";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getActivity(String num) {
        try {
            String sql = "SELECT * FROM Activity WHERE routeNum = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            } else {
                System.out.println("Not found.");
            }
            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getActivityStopNum(String num) {
        try {
            if(!isNumber(num)){
                System.out.println("Not stop number");
                return;
            }

            String sql = "SELECT * FROM Activity WHERE stopNum = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.valueOf(num));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            } else {
                System.out.println("Not found.");
            }
            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getEachTotalActivity() {
        try {
            String sql = "SELECT routeNum, COUNT(*) AS activityCount FROM Activity GROUP BY routeNum;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Activity Count: " + resultSet.getString("activityCount"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getActivityScheTime(String num) {
        try {
            String sql = "SELECT * FROM Activity WHERE scheName LIKE '%" + num + "%'";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            } else {
                System.out.println("Not found.");
            }

            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getActivityDayType(String num) {
        try {
            String sql = "SELECT * FROM Activity WHERE dayType = ? ORDER BY timePeriod;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            } else {
                System.out.println("Not found.");
            }
            while (resultSet.next()) {
                System.out.println("Schedule Name: " + resultSet.getString("scheName") +
                        ", Route Number: " + resultSet.getString("routeNum") +
                        ", Stop Number: " + resultSet.getString("stopNum") +
                        ", Boarding Number: " + resultSet.getInt("boardingNum") +
                        ", Alighting Number: " + resultSet.getInt("alightingNum") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // ARRIVE
    public void getArrive() {
        try {
            String sql = "SELECT * FROM Arrive";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Stop Number: " + resultSet.getInt("stopNum") +
                        ", Time: " + resultSet.getString("scheTime") +
                        ", Deviation: " + resultSet.getInt("deviation"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getArrive(String num) {
        try {
            String sql = "SELECT * FROM Arrive WHERE busNum = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Stop Number: " + resultSet.getInt("stopNum") +
                        ", Time: " + resultSet.getString("scheTime") +
                        ", Deviation: " + resultSet.getInt("deviation"));
            } else {
                System.out.println("Not found.");
            }
            while (resultSet.next()) {
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Stop Number: " + resultSet.getInt("stopNum") +
                        ", Time: " + resultSet.getString("scheTime") +
                        ", Deviation: " + resultSet.getInt("deviation"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getArriveDeviation() {
        try {
            String sql = "SELECT *FROM Arrive WHERE deviation > 0;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Stop Number: " + resultSet.getInt("stopNum") +
                        ", Time: " + resultSet.getString("scheTime") +
                        ", Deviation: " + resultSet.getInt("deviation"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getArriveTotal() {
        try {
            String sql = "SELECT busNum, stopNum, COUNT(*) AS TotalArrivals FROM Arrive GROUP BY busNum, stopNum;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Bus Number: " + resultSet.getString("busNum") +
                        ", Stop Number: " + resultSet.getInt("stopNum") +
                        ", TotalArrivals: " + resultSet.getString("TotalArrivals"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }


    // PASSUP
    public void getPassUp() {
        try {
            String sql = "SELECT * FROM PassUp ORDER BY type;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Location: " + resultSet.getString("location") +
                        ", Time: " + resultSet.getString("time") +
                        ", Pass Up Type: " + resultSet.getString("type"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getPassUp(String num) {
        try {
            String sql = "SELECT * FROM PassUp WHERE routeNum = ? ORDER BY time DESC;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Location: " + resultSet.getString("location") +
                        ", Time: " + resultSet.getString("time") +
                        ", Pass Up Type: " + resultSet.getString("type"));
            } else {
                System.out.println("Not found.");
            }
            while (resultSet.next()) {
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Location: " + resultSet.getString("location") +
                        ", Time: " + resultSet.getString("time") +
                        ", Pass Up Type: " + resultSet.getString("type"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getPassUpTime(String num) {
        try {
            if(!isNumber(num)){
                System.out.println("Not year number");
                return;
            }
            String sql = "SELECT * FROM PassUp WHERE time LIKE '%" + num + "%'";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Location: " + resultSet.getString("location") +
                        ", Time: " + resultSet.getString("time") +
                        ", Pass Up Type: " + resultSet.getString("type"));
            } else {
                System.out.println("Not found.");
            }
            while (resultSet.next()) {
                System.out.println("Route Number: " + resultSet.getString("routeNum") +
                        ", Location: " + resultSet.getString("location") +
                        ", Time: " + resultSet.getString("time") +
                        ", Pass Up Type: " + resultSet.getString("type"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getRouteInfo(String num) {
        try {

            String sql = "SELECT Route.routeNum AS rNum, Bus.destination AS busDestination, BusStop.stopNum AS stopNumber, BusStop.location AS stopLocation, " +
                        "Schedule.scheName AS sName, Schedule.startDate AS sStart, Schedule.endDate AS sEnd, " +
                        "Activity.boardingNum AS board, Activity.alightingNum AS alight, Activity.dayType AS dayType, Activity.timePeriod AS timePeriod " +
                        "FROM Route " +
                        "LEFT JOIN Bus ON Route.routeNum = Bus.busNum " +
                        "LEFT JOIN Activity ON Route.routeNum = Activity.routeNum " +
                        "LEFT JOIN BusStop ON Activity.stopNum = BusStop.stopNum " +
                        "LEFT JOIN Schedule ON Activity.scheName = Schedule.scheName " +
                        "WHERE Route.routeNum = ?;";


            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, num);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Route Number: " + resultSet.getString("rNum") + "\n" +
                        "Stop Number: " + resultSet.getString("stopNumber") +
                        ", Location: " + resultSet.getString("stopLocation") +
                        ", Schedule Name: " + resultSet.getString("sName") +
                        ", Start Date: " + resultSet.getString("sStart") +
                        ", End Date: " + resultSet.getString("sEnd") +
                        ", Boarding Number: " + resultSet.getInt("board") +
                        ", Alighting Number: " + resultSet.getInt("alight") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            } else {
                System.out.println("Not found.");
            }
            while (resultSet.next()) {
                System.out.println("Stop Number: " + resultSet.getString("stopNumber") +
                        ", Location: " + resultSet.getString("stopLocation") +
                        ", Schedule Name: " + resultSet.getString("sName") +
                        ", Start Date: " + resultSet.getString("sStart") +
                        ", End Date: " + resultSet.getString("sEnd") +
                        ", Boarding Number: " + resultSet.getInt("board") +
                        ", Alighting Number: " + resultSet.getInt("alight") +
                        ", Day type: " + resultSet.getString("dayType") +
                        ", Time Period: " + resultSet.getString("timePeriod") );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    private static boolean isRouteNum(String str){
        return str != null && str.matches("(^[0-9]+$)|(^S[0-9]+$)");
    }

    private static boolean isNumber(String str){
        return str != null && str.matches("^[0-9]+$");
    }
}