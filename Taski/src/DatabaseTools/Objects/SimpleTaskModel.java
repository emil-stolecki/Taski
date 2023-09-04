package DatabaseTools.Objects;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import tasksPackage.EnumFolder.Satisfaction;
import tasksPackage.EnumFolder.TaskType;

public class SimpleTaskModel extends TaskModel{

	 public int id;//assigned by database, for test random number	
	 public String title;
	 public String description;
	 public LocalDate dateAdded;
	 public int importance;
	 public Boolean isDone;
	 public Satisfaction satisfaction;
	 public TaskType t=TaskType.SimpleTask;
	 public LocalDateTime deadline;
	 public LocalDateTime preferredTime;
	 public Duration duration;

	public SimpleTaskModel(int id, String title, String description, long dateAdded, int importance, Boolean isDone,
			Satisfaction satisfaction, long deadline, long preferredTime, long duration) {
		
		this.id = id;
		this.title = title;
		this.description = description;
		//this.dateAdded =  LocalDateTime.ofInstant(Instant.ofEpochMilli(dateAdded), ZoneId.systemDefault());
		this.dateAdded =  LocalDate.ofEpochDay(dateAdded);
		this.importance = importance;
		this.isDone = isDone;
		this.satisfaction = satisfaction;
	
		this.deadline = LocalDateTime.ofInstant(Instant.ofEpochMilli(deadline), ZoneId.systemDefault());
		this.preferredTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(preferredTime), ZoneId.systemDefault());
		this.duration=Duration.ofSeconds(duration);
	}

}
