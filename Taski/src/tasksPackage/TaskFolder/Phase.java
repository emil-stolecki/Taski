package tasksPackage.TaskFolder;

import java.util.Date;

import tasksPackage.EnumFolder.Satisfaction;

public class Phase {

	int phaseId;
	int projectId;
	String title;
	String description;
	Date prefferedTime;
	boolean isDone;
	Satisfaction satisfaction;
	
	public Phase(int projectId, String title, String description, Date prefferedTime) {
		
		this.projectId=projectId;
		this.title=title;
		this.description=description;
		this.prefferedTime=prefferedTime;
		
	}
	
	
	public void changePrefferedTime(Date newTime) {
		
	}
	public void repeat(){//make new reference to a failed tasks in the calendar
		
	}
	public void complete() {//mark task as completed or failed
		
	}
	
}
