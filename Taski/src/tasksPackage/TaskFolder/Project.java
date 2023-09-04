package tasksPackage.TaskFolder;

import java.util.Date;

import tasksPackage.EnumFolder.TaskType;

import java.util.ArrayList;

public class Project extends Task{
	
	Date deadline;
	ArrayList<Phase> phases=new ArrayList<Phase>();
	
	public Project (String title, String description, int importance, Date deadline) {
		super(title,description, importance,TaskType.Project);
		this.deadline=deadline;
		
	}
	
	
	
}
