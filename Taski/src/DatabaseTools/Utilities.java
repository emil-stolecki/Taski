package DatabaseTools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

import DatabaseTools.Objects.EventModel;
import DatabaseTools.Objects.SimpleTaskModel;
import DatabaseTools.Objects.TaskModel;
import tasksPackage.EnumFolder.Mode;
import tasksPackage.EnumFolder.Satisfaction;
import tasksPackage.EnumFolder.TaskType;


public class Utilities {
 //switch  to JPA
	
	//table structures
	static String[] simples = {"title","description","dateAdded","importance",
						"isDone","satisfaction", "deadline", "preferredTime", "duration"};
	static String[] events = {"title","description","dateAdded","importance",
						"isDone","satisfaction", "startTime", "endTime", 
						"eventMode","weekPattern", 
						"monthPattern", "gap"};
	
	private static final HashMap<TaskType, String[]> types = initializeTypes();
	

	
	public static HashMap<TaskType, String[]> initializeTypes(){
		HashMap<TaskType, String[]> map =new HashMap<>();		
		map.put(TaskType.SimpleTask, simples);
		map.put(TaskType.Event, events);
		//map.put(TaskType.project, projects);
		//map.put(TaskType.phase, phases);
		//map.put(TaskType.routine, routines);
		return map;
	}
	
		
	//makes a query for inserting or updating 
	public static String makeQuery(TaskType t, int mode) {
		//mode =
		//0 -> insert
		//1 -> update
		
		String query = "";
		String[] columns = null;
		
		columns=types.get(t);
		
		if(mode==0) {
			query=query +"INSERT INTO " 
					+t.toString()+"s "
					+"(";
			for (String col : columns) {
				query=query+col+", ";
			}
			query=query.substring(0,query.length() - 2);
			query=query +") VALUES (?";
			for(int i = 0;i<columns.length-1;i++) {
				query=query+",?";
			}
			//+",?".repeat(columns.length-1)
					
					
			query=query +")";
		}
		
		
		else {
			query=query +"UPDATE " 
						+t.toString()+"s "
						+"SET ";
		
			for (String col : columns) {
				query=query+col+"=?, ";			
			}
			query=query.substring(0,query.length() - 2);
			query=query +" WHERE id=";
		}
		
		return query;
	}
	
	//fills values in into the fields 
	public static void fill(PreparedStatement st,TaskType t, String[] values) {
		
		try {
			st.setString(1, values[1]);	
			st.setString(2, values[2]);
			
			st.setTimestamp(3, new java.sql.Timestamp(Long.parseLong(values[3])));
			st.setInt(4, Integer.parseInt(values[4]));
			st.setBoolean(5, Boolean.parseBoolean(values[5]));
			st.setInt(6, Integer.parseInt(values[6]));
		
			switch(t) {
			case SimpleTask:
				
				st.setTimestamp(7, new java.sql.Timestamp(Long.parseLong(values[7])));
				st.setTimestamp(8, new java.sql.Timestamp(Long.parseLong(values[8])));
				st.setTime(9, new java.sql.Time(Long.parseLong(values[9])));
				break;
			case Event:
				
				st.setTime(7,new java.sql.Time(Integer.parseInt(values[7])));
				st.setTime(8,new java.sql.Time(Integer.parseInt(values[8])));
				st.setInt(9, Integer.parseInt(values[9]));//mode				
				st.setString(10, values[10]);//pattern
				st.setString(11, values[11]);//pattern
				st.setInt(12, Integer.parseInt(values[12]));//gap
				
				break;
			default: 
				System.out.println("Error, no such type of task");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//converts retrieved data into a suitable model
	public static TaskModel handleResponse(TaskType t, ResultSet r) {
		try {
			int id=r.getInt("id");
			String title=r.getString("title");
			String desc=r.getString("description");
			long dateAdded=r.getLong("dateAdded");			
			int importance=r.getInt("importance");
			Boolean isDone=r.getBoolean("isDone");
			Satisfaction sat=Satisfaction.values()[r.getInt("satisfaction")];	
			switch(t) {
			case SimpleTask:
				
				long deadline=r.getLong("deadline");
				long preffered=r.getLong("preferredTime");
				long duration =r.getLong("duration");
				return new SimpleTaskModel(id,title,desc,dateAdded,importance,isDone,sat,deadline,preffered,duration);
			case Event:
				
				Time start =r.getTime("startTime");
				Time end =r.getTime("endTime");
				Mode mode =Mode.values()[r.getInt("eventMode")-1]	;
				String patternW =r.getString("weekPattern");
				String patternM=r.getString("monthPattern");
				int gap=r.getInt("gap");
				return new EventModel(id,title,desc,dateAdded,importance,isDone,sat,start,end,mode,patternW,patternM,gap);
			default: 
				System.out.println("Error, no such type of task");
				return null;
			}
			
		} catch (SQLException e) {
			System.out.println("Error: ");
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	//Makes a query for GETWHERE method
	public static String makeQueryWhere(TaskType t, String[] TestedCols, String[] conditions) {
		String query = "SELECT * FROM " +t.toString()+"s WHERE ";
		query=query+TestedCols[0]+" = "+ conditions[0];
		if(TestedCols.length>1 && TestedCols.length==conditions.length) {
			for(int i=1;i<TestedCols.length;i++) {
				query=query+" AND "+TestedCols[i]+" = "+ conditions[1];
			}
		}//System.out.println(query);		
		return query;
	}
}


