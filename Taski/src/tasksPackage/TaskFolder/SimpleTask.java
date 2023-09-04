package tasksPackage.TaskFolder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import DatabaseTools.Objects.SimpleTaskModel;
import tasksPackage.EnumFolder.TaskType;

public class SimpleTask extends Task{
	
	private LocalDateTime deadline;
	private LocalDateTime preferredTime;
	private Duration duration;

	
	public SimpleTask(String title, String description, int importance, LocalDateTime deadline, LocalDateTime prefferedTime,Duration duration) {
		super(title, description, importance,TaskType.SimpleTask);
		this.deadline=deadline;
		this.preferredTime=prefferedTime;
		this.duration=duration;
	}
	
	public SimpleTask(SimpleTaskModel a) {
		super(a.id,a.title,a.description,a.dateAdded,a.importance,TaskType.SimpleTask);
		this.setIsDone(a.isDone);
		this.deadline=a.deadline;
		this.preferredTime=a.preferredTime;				
		this.duration=a.duration;
	}
	
	//get methods
	public LocalDateTime getDeadline () {
		return this.deadline;
	}
	public LocalDateTime getPreffered () {
		return this.preferredTime;
	}
	public Duration getDuration () {
		return this.duration;
	}
	
	//update methods
	public void updateDeadline (LocalDateTime newDeadline) {
		this.deadline=newDeadline;
	}
	public void changePrefferedTime(LocalDateTime newTime) {
		this.preferredTime=newTime;
	}
	public void changeDuration(Duration newDuration) {
		this.duration=newDuration;
	}
	
	
	public void repeat(){//make new reference to a failed tasks in the calendar
		//to the calendar
	}
	
	public String[] preview() {
		 String[] tmp=new String[7];
		 tmp[0]="Title: "+this.getTitle();
		 tmp[1]="Description: "+this.getDesc();
		 tmp[2]="Added: "+this.getDateAdded();
		 tmp[3]="Importance: "+ this.getImportance();
		 tmp[4]="Finished: "+ this.isDone();
		 tmp[5]="Deadline: "+this.deadline;
		 tmp[6]="Planned: "+this.preferredTime;
		return tmp;
	}
	
	public void testPrint() {
		System.out.println(this.getId());
		System.out.println(this.getTitle());
		System.out.println(this.getDesc());
		System.out.println(this.getImportance());
		System.out.println(this.getDateAdded());
		System.out.println(this.getSatisfaction());
		System.out.println(this.getDeadline());
		System.out.println(this.getPreffered());
		System.out.println("");
		
	}
}
