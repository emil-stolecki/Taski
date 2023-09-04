package DatabaseTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TableControls {
	//static String dbUrl = "jdbc:sqlite:/C:\\Users\\asdf1\\eclipse-workspace\\Taski\\src\\database.db";
	static String dbUrl = "jdbc:sqlite:..\\database.db";
	public static void create() {
		
		 try (Connection con = DriverManager.getConnection(dbUrl);
	             Statement st = con.createStatement()) {
	            
	            String createTableQuery = "CREATE TABLE IF NOT EXISTS SimpleTasks ("
	                    + "id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,"
	                    + "title         VARCHAR (100),"
	                    + "description   STRING,"
	                    + "dateAdded     DATE          NOT NULL,"
	                    + "importance    INTEGER       NOT NULL,"
	                    + "isDone        BOOLEAN       NOT NULL,"
	                    + "satisfaction  INTEGER,"
	                    + "deadline      DATETIME,"
	                    + "preferredTime DATETIME,"  
	                    + "duration       DATETIME"
	                    + ");"
	            
	            		+ "CREATE TABLE IF NOT EXISTS Events ("
	            		+ "id INTEGER PRIMARY KEY UNIQUE NOT NULL,"
	            	    + "title        VARCHAR (100),"
	            	    + "description  STRING,"
	            	    + "dateAdded    DATE          NOT NULL,"
	            	    + "importance   INTEGER       NOT NULL,"
	            	    + "isDone       BOOLEAN       NOT NULL,"
	            	    + "satisfaction INTEGER,"
	            	    + "startTime    TIME          NOT NULL,"
	            	    + "endTime      TIME          NOT NULL,"
	            	    + "eventMode    INTEGER       NOT NULL,"	            	  
	            	    + "weekPattern  VARCHAR(7),"
	            	    + "monthPattern VARCHAR(31),"
	            	    + "gap          INTEGER"
	            		+ ");";
	            st.executeUpdate(createTableQuery);
	            
	            
	        } catch (SQLException e) {
	            System.out.println("Couln't create tables " + e.getMessage());
	        }
	}
	
	public static void drop() {
		try (Connection con = DriverManager.getConnection(dbUrl);
	             Statement st = con.createStatement()) {
	            
	            String createTableQuery = "DROP TABLE SimpleTasks;"
	            						+ "DROP TABLE Events;";
	            st.executeUpdate(createTableQuery);
	            
	            System.out.println("Tables dropped");
	        } catch (SQLException e) {
	            System.out.println("Couldn't drop tables " + e.getMessage());
	        }
	}
}
