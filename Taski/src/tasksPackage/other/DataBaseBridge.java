package tasksPackage.other;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;

import DatabaseTools.CRUD;
import DatabaseTools.Objects.*;
import tasksPackage.EnumFolder.Mode;
import tasksPackage.EnumFolder.Satisfaction;
import tasksPackage.EnumFolder.TaskType;
import tasksPackage.TaskFolder.*;
import java.time.ZoneOffset;

public class DataBaseBridge {
	
	
	private static final HashMap<Satisfaction, Integer> satisfactions = initializeSats();
	private static final HashMap<Mode, Integer> modes = initializeModes();

	

	public static HashMap<Satisfaction, Integer> initializeSats(){
		HashMap<Satisfaction, Integer> map =new HashMap<>();
		map.put(null,0);
		map.put(Satisfaction.bad,1);
		map.put(Satisfaction.rush,2);
		map.put(Satisfaction.ok,3);
		map.put(Satisfaction.great,4);
		
		return map;
	}
	public static HashMap<Mode, Integer> initializeModes(){
		HashMap<Mode, Integer> map =new HashMap<>();	
		map.put(Mode.oneTime,1);
		map.put(Mode.daily,2);
		map.put(Mode.weekly,3);
		map.put(Mode.monthly,4);
		map.put(Mode.custom,5);
		return map;
	}
	
	
	
	public static SimpleTask getSimpleTask(TaskModel m) {
		try {
		SimpleTaskModel stm=(SimpleTaskModel) m;
		SimpleTask e =new SimpleTask(stm);
		return e;
		}
		catch(Exception e) {
			System.out.println("Error: Task doesn't exist");
			//e.printStackTrace();
		}
		return null;
	}
	public static Event getEvent(TaskModel t) {
		try {
		EventModel em=(EventModel)t;
		Event e = new Event(em);
		return e;
		}
		catch(Exception e) {
			System.out.println("Error: Task doesn't exist");
		}
		return null;
	}
	public static ArrayList<SimpleTask> getSimpleTaskArray(ArrayList<TaskModel> tm) {
		ArrayList<SimpleTask> e=new ArrayList<SimpleTask>();
		for(int  i=0;i<tm.size();i++) {			
			e.add(new SimpleTask((SimpleTaskModel) tm.get(i)));
		}
		
		return e;
	}
	public static ArrayList<Event> getEventArray(ArrayList<TaskModel> em) {
		ArrayList<Event> e=new ArrayList<Event>();
		for(int  i=0;i<em.size();i++) {			
			e.add(new Event((EventModel) em.get(i)));
		}
		return e;
	}
	
	public static String[] simpleTaskToArray(SimpleTask e) {
		
		String[] a = new String[10];
		a[0]=Integer.toString(e.getId());
		a[1]=e.getTitle();
		a[2]=e.getDesc();
		a[3]=Long.toString(e.getDateAdded().toEpochDay());
		a[4]=Integer.toString(e.getImportance());
		a[5]=Boolean.toString(e.isDone());	
		a[6]=Integer.toString(satisfactions.get(e.getSatisfaction()));				
		a[7]=Long.toString(e.getDeadline().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());
		a[8]=Long.toString(e.getPreffered().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli());		
		a[9]=Long.toString(e.getDuration().getSeconds());
		
		return a;
	}
	
	public static String[] eventToArray(Event e) {
		String[] a = new String[13];
		a[0]=Integer.toString(e.getId());
		a[1]=e.getTitle();
		a[2]=e.getDesc();
		a[3]=Long.toString(e.getDateAdded().toEpochDay());
		a[4]=Integer.toString(e.getImportance());
		a[5]=Boolean.toString(e.isDone());
		a[6]=Integer.toString(satisfactions.get(e.getSatisfaction()));		
		a[7]=Integer.toString((e.getStart().toSecondOfDay()-3600)*1000);
		a[8]=Integer.toString((e.getEnd().toSecondOfDay()-3600)*1000);
		a[9]=Integer.toString(modes.get(e.getMode()));
		///////////////////////////////////////
		if(e.getWeekPattern()!=null) {
			a[10]="";
			for(Boolean d:e.getWeekPattern()){
				if(d) a[10]=a[10]+"1";
				else  a[10]=a[10]+"0";
			}
		}
		if(e.getMonthPattern()!=null) {
			a[11]="";
			for(Boolean d:e.getMonthPattern()){
				if(d) a[11]=a[11]+"1";
				else  a[11]=a[11]+"0";
			}
		}
		a[12]=Integer.toString(e.getCustomPattern());
		return a;
	}
	
}
