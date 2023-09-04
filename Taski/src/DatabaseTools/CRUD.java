package DatabaseTools;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import DatabaseTools.Utilities;
import DatabaseTools.Objects.EventModel;
import DatabaseTools.Objects.SimpleTaskModel;
import DatabaseTools.Objects.TaskModel;
import tasksPackage.EnumFolder.Mode;
import tasksPackage.EnumFolder.Satisfaction;
import tasksPackage.EnumFolder.TaskType;

///wpisywanie nazw kolumn ręcznie
public class CRUD {
	
	static String dbUrl=TableControls.dbUrl;
	
	//Returns all records from selected table - used for testing
	public static ArrayList<TaskModel> get(TaskType t) {	
		ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
		try {
			Connection con=DriverManager.getConnection(dbUrl);
			String query= "SELECT * FROM " + t.toString()+"s";
			Statement st = con.createStatement();
			ResultSet result = st.executeQuery(query);			
			while (result.next()) {
				tasks.add(Utilities.handleResponse(t, result));
			}
			st.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("Error: ");
			e.printStackTrace();
		}	
		
		return tasks;
	}
	
	//Returns record of specified from selected table
	public static TaskModel get(TaskType t,int id) {
		TaskModel task = new TaskModel();
		try {
			Connection con=DriverManager.getConnection(dbUrl);
			String query= "SELECT * FROM " + t.toString() +"s" +" WHERE id="+id;
			Statement st = con.createStatement();
			ResultSet result = st.executeQuery(query);
			if (result.next()) {
				task=Utilities.handleResponse(t, result);
				
			}
			st.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("Error: ");
			e.printStackTrace();
		}	
			return task;
	}
	
	//Deletes specified record from selected table
	public static void delete(TaskType t, int id) {
		try {
			String deleteQuery = "DELETE FROM "+ t.toString()+"s "+ "WHERE id="+id;
			Connection con=DriverManager.getConnection(dbUrl);
			Statement st = con.createStatement();			
			st.executeUpdate(deleteQuery);
	        st.close();
	        con.close();	        
			
		} catch (SQLException e) {
			System.out.println("Error: ");
			e.printStackTrace();
		}	
	}
		
		//Updates all fields of specified record
		public static void updateWhole(TaskType t, int id, String[] newValues)  {		
			try {
				String updateQuery = Utilities.makeQuery(t,1)+id;
				Connection con=DriverManager.getConnection(dbUrl);
				PreparedStatement st = con.prepareStatement(updateQuery);	
				Utilities.fill(st,t, newValues);
				st.executeUpdate();
		        st.close();
		        con.close();
						    
						
			} catch (SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	

		}
		
		//Updates one field of specified record
		public static void updateSingle(TaskType t, int id, String colName,String newValue )  {		
			try {
				String updateQuery = "UPDATE "+t.toString()+"s"+ " SET " +colName+ " = '"+newValue+"' WHERE id= "+id;
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();	
								
				st.execute(updateQuery);
		        st.close();
		        con.close();
						    
						
			} catch (SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	

		}
	
		//Creates new record in selected table
		public static void add(TaskType t, String[] newValues) {
			try {
				String insertQuery = Utilities.makeQuery(t,0);
				Connection con=DriverManager.getConnection(dbUrl);
				PreparedStatement st = con.prepareStatement(insertQuery);
				Utilities.fill(st,t, newValues);
		        st.executeUpdate();
		        st.close();
		        con.close();	        
				
			} catch (SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
		}
		
		//returns all records that meet a condition
		public static ArrayList<TaskModel> getWhere(TaskType t, String[] testedCols,String[] conditions) {
			ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
			try {
				String query=Utilities.makeQueryWhere(t,testedCols,conditions);
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query);	
				while (result.next()) {
					tasks.add(Utilities.handleResponse(t, result));
				}
				st.close();
				con.close();
			}
			catch(SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
			
			return tasks;
		}
		//
		public static ArrayList<TaskModel> getByPattern(int mode,int day){
			//mode 0 ->week pattern
			//mode 1 -> month pattern
			ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
			try {
				String query="SELECT * FROM Events WHERE " ;
				if(mode==0) {
					query = query+"eventMode = 3 AND SUBSTRING(weekPattern, "+day+", 1) = '1'";
				}
				else if(mode==1) {
					query = query+"eventMode = 4 AND SUBSTRING(monthPattern, "+day+", 1) = '1'";
				}
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query);	
				while (result.next()) {
					tasks.add(Utilities.handleResponse(TaskType.Event, result));
				}
				st.close();
				con.close();
			}
			catch(SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
			return tasks;
		}
		
		public static ArrayList<TaskModel> getByCustomPattern(int mode,long day){
			//mode 0 -> one time event
			//mode 1 -> cyclic event			
			ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
			try {
				String query="SELECT * FROM Events WHERE eventMode =";
				if(mode==0) {
					query=query+"1 AND ABS(dateAdded - "+day+")=gap";
				}else if(mode==1){
					query=query+"5 AND ABS(dateAdded - "+day+") % gap =0";
				}
				
	
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query);	
				while (result.next()) {
					tasks.add(Utilities.handleResponse(TaskType.Event, result));
					
				}
				st.close();
				con.close();
			}
			catch(SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
			return tasks;
		}
		public static ArrayList<TaskModel> getSimpleTask(long day){				
			ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();
			try {
				String query="SELECT * FROM SimpleTasks WHERE FLOOR(((preferredTime/3600000)+2)/24)=="+day;				
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query);	
				while (result.next()) {
					tasks.add(Utilities.handleResponse(TaskType.SimpleTask, result));
				}
				st.close();
				con.close();
			}
			catch(SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
			return tasks;
		}
		
		public static ArrayList<TaskModel> getPotentialTasks(LocalDate day){			
			ArrayList<TaskModel> tasks = new ArrayList<TaskModel>();			
			try {
				String query1="SELECT * FROM SimpleTasks WHERE isDone = false AND deadline =0 AND "
						+ "FLOOR(((preferredTime/3600000)+2)/24)!="+day.toEpochDay() ;
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query1);	
				while (result.next()) {
					SimpleTaskModel t = (SimpleTaskModel) Utilities.handleResponse(TaskType.SimpleTask, result);
					t.deadline=LocalDateTime.of(day,LocalTime.of(0, 0));
					t.preferredTime=LocalDateTime.of(day,LocalTime.of(0, 0));	
					tasks.add(t);
					
				}
				
				String query2="SELECT * FROM SimpleTasks WHERE isDone = false AND FLOOR(((deadline/3600000)+2)/24)>"+day.toEpochDay()
						+" AND FLOOR(((preferredTime/3600000)+2)/24)!= "+day.toEpochDay();
				
				result = st.executeQuery(query2);
				
				while (result.next()) {
					tasks.add(Utilities.handleResponse(TaskType.SimpleTask, result));
					
				}
				st.close();
				con.close();
			}
			catch(SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
			
			return tasks;
		}
		
		public static Object[][] loadSimpleTasks(int limit, int offset) {
			ArrayList<Object[]> tasksArray = new ArrayList<Object[]>();
			
			try {
				String query="SELECT * FROM SimpleTasks ORDER BY ID DESC LIMIT "+limit+" OFFSET "+offset;
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query);	
				
				while (result.next()) {
					
					Object[] task = new Object[10];
					task[0]=result.getString("title");
					task[1]=result.getString("description");
					task[2]=LocalDate.ofEpochDay(result.getLong("dateAdded"));							
					task[3]=result.getInt("importance");
					task[4]=result.getBoolean("isDone");
					task[5]=Satisfaction.values()[result.getInt("satisfaction")];
					
					if(result.getLong("deadline")==0) {
						task[6]="-";
					}
					else {
						task[6]=LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("deadline")), ZoneId.systemDefault());
					}
					
					if(result.getLong("preferredTime")==0) {					
						task[7]="-";
					}
					else {
						task[7]=LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("preferredTime")), ZoneId.systemDefault());
					}
					
					Duration d=Duration.ofSeconds(result.getLong("duration"));	
					task[8]=d.toHours()	+":"+(d.toMinutes()-d.toHours()*60);
					task[9]=Integer.toString(result.getInt("id"));
					tasksArray.add(task);
															
				}
				
				
				
				st.close();
				con.close();
			}
			catch(SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
			
			Object[][] tasksFinal = new Object[tasksArray.size()][10];
			for (int i=0;i<tasksArray.size();i++) {
				tasksFinal[i]=tasksArray.get(i);
			}
			return tasksFinal;
		}
		public static Object[][] loadEvents(int limit, int offset) {
			ArrayList<Object[]> eventsArray = new ArrayList<Object[]>();
			     
			try {
				String query="SELECT * FROM Events ORDER BY ID DESC LIMIT "+limit+" OFFSET "+offset;
				Connection con=DriverManager.getConnection(dbUrl);
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query);
												
				while (result.next()) {
					Object[] event =new Object[9];	  
					event[0]=result.getString("title");
					event[1]=result.getString("description");
					event[2]=LocalDate.ofEpochDay(result.getLong("dateAdded"));
					event[3]=result.getInt("importance");				
					event[4]=result.getTime("startTime").toLocalTime();
					event[5]=result.getTime("endTime").toLocalTime();	
					event[6]=Mode.values()[result.getInt("eventMode")-1];
					event[7]="";
					LocalDate today=LocalDate.now();
					
					if(result.getInt("eventMode")-1==1) {						
						event[7]=today.toString();						
					}
					else if(result.getInt("eventMode")-1==2) {
						String[] w =result.getString("weekPattern").split("");
						int addDays=0;
																		
						if(w[today.getDayOfWeek().getValue()-1].equals("1")) {
							event[7]=today.toString();							
						}
						else{							
							int j=today.getDayOfWeek().getValue();							
							boolean isfound=false;
							while(j<7&& !isfound) {								
								if(w[j].equals("1")) {
									addDays=j-today.getDayOfWeek().getValue()+2;
									isfound=true;									
								}
							
								j++;
							}
							j=0;
							while(j<today.getDayOfWeek().getValue()-1&& !isfound) {
								if(w[j].equals("1")) {
									addDays=j+8-today.getDayOfWeek().getValue();
									isfound=true;
									
								}
								
								j++;
							}
							
							LocalDate closest=LocalDate.ofEpochDay(result.getLong("dateAdded")).plusDays(addDays);;
							
							event[7]=closest.toString();	
						}
					}
					else if(result.getInt("eventMode")-1==3) {
						String[] m=result.getString("monthPattern").split("");
						int day=today.getDayOfMonth();
						int addDays=0;
						if(m[day-1].equals("1")){
							event[7]=today.toString();	
						}
						else{
							LocalDate closest=today;
							int j=day-1;
							boolean isfound=false;
							
							while(j<31&& !isfound) {
							
								if(m[j].equals("1")) {
									addDays=j-day+1;
									closest=closest.plusDays(addDays);									
									isfound=true;
									event[7]=closest.toString();	
								}
							
							j++;
							}
							j=0;
							while(j<day-1&& !isfound) {
								
								if(m[j].equals("1")) {
									addDays=j;
									isfound=true;
									
									
									closest=LocalDate.of(today.getYear(),today.getMonth().getValue()+1,today.getDayOfMonth());
									closest=closest.minusDays(day-addDays-1);
									event[7]=closest.toString();	
								}
								
								
							}
							
						}
					}				
					else if(result.getInt("eventMode")-1==0) {
						long gap=(long)result.getInt("gap");
						LocalDate closest=LocalDate.ofEpochDay(result.getLong("dateAdded")).plusDays(gap);;
													
						event[7]=closest.toString();	
					}
					else if(result.getInt("eventMode")-1==4) {
						int gap=result.getInt("gap");
						int diff =(int) ChronoUnit.DAYS.between(LocalDate.ofEpochDay(result.getLong("dateAdded")),
								today);
						int remDays=gap-diff%gap;
						LocalDate closest=LocalDate.ofEpochDay(result.getLong("dateAdded")).plusDays(remDays);
						
						event[7]=closest.toString();
					}
					
					
					
					
					event[8]=Integer.toString(result.getInt("id"));
					
					
					
					eventsArray.add(event);
				}
				
				
				
				st.close();
				con.close();
			}
			catch(SQLException e) {
				System.out.println("Error: ");
				e.printStackTrace();
			}	
			
			Object[][] eventsFinal = new Object[eventsArray.size()][11];
			for (int i=0;i<eventsArray.size();i++) {
				eventsFinal[i]=eventsArray.get(i);
			}
			return eventsFinal;
		}
		
	

}
