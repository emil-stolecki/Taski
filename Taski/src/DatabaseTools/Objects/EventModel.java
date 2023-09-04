package DatabaseTools.Objects;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;


import tasksPackage.EnumFolder.Mode;
import tasksPackage.EnumFolder.Satisfaction;
import tasksPackage.EnumFolder.TaskType;

public class EventModel extends TaskModel{
	 public int id;	
	 public String title;
	 public String description;
	 public LocalDate dateAdded;
	 public int importance;
	 public Boolean isDone;
	 public Satisfaction satisfaction;
	 public TaskType t=TaskType.Event;
	 public LocalTime startTime;
	 public LocalTime endTime;
	 public Mode eventMode;	 
	 public Boolean [] weekPattern = new Boolean[7];
	 public Boolean [] monthPattern = new Boolean[31];
	 public int gap;
	 
	public EventModel(int id, String title, String description, long dateAdded, 
			int importance, Boolean isDone, Satisfaction satisfaction, Time startTime, Time endTime, 
			Mode eventMode, String weekPattern, String monthPattern, int gap) {
		
		this.id = id;
		this.title = title;
		this.description = description;
		this.dateAdded = LocalDate.ofEpochDay(dateAdded);
		this.importance = importance;
		this.isDone = isDone;
		this.satisfaction = satisfaction;
		this.startTime = startTime.toLocalTime();
		this.endTime = endTime.toLocalTime();
		this.eventMode = eventMode;
				
		char[] ch1=weekPattern.toCharArray();
		Boolean [] bo1 = new Boolean[7];
		if (ch1.length==7) {
			for (int i=0;i<7;i++) {
				if (ch1[i]=='1') bo1[i]=true;			
				else bo1[i]=false;
			}
		
		}
		this.weekPattern = bo1;
		
		char[] ch2= monthPattern.toCharArray();
		Boolean [] bo2 = new Boolean[31];
		
		if (ch2.length==31) {
			for (int i=0;i<31;i++) {
				if (ch2[i]=='1') bo2[i]=true;			
				else bo2[i]=false;
				
			}
		}
		this.monthPattern = bo2;
		
		
		this.gap = gap;
	}
	 
	 
	
	
	
	
}
