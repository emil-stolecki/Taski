package tasksPackage.TaskFolder;

import java.util.Scanner;

import tasksPackage.EnumFolder.Satisfaction;
import tasksPackage.EnumFolder.TaskType;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public abstract class Task {
	private int id;//assigned by database, for test random number	
	private String title;
	private String description;
	private LocalDate dateAdded;
	private int importance=3; //1(skippable)-3(default)-5(urgent)
	private Boolean isDone=false;
	private Satisfaction satisfaction;
	private TaskType t;
	
	
	public Task(String title, String description, int importance,TaskType t) {
		this.id=0;
		this.title = title;
		this.description =description;
		this.importance= importance;
		this.t=t;
		this.dateAdded= LocalDate.now();
	};
	
   public Task(int id,String title, String description, LocalDate dateAdded, int importance,TaskType t) {
		this.id=id;
		this.title = title;
		this.description =description;
		this.dateAdded=dateAdded;
		this.importance= importance;
		this.t=t;
	}
	//get methods
	public int getId() {
		return this.id;
	}
	public String getTitle() {
		return this.title;
		
	}
	public String getDesc() {
		return this.description;
		
	}
	public LocalDate getDateAdded() {
		return this.dateAdded;
	}
	public int getImportance() {
		return this.importance;
		
	}
	public Boolean isDone () {
		return this.isDone;
	}
	
	public TaskType getType() {
		return this.t;
	}
	
	public Satisfaction getSatisfaction() {
		return this.satisfaction;
	}
	public String[] preview() {
		return new String[]{""};
	}
	//update methods
	public void editTitle(String newTitle) {
		this.title =newTitle;
		
	};
		
	public void editDesc(String newDesc) {
		this.description=newDesc;
		
	};
	public void editImportance(int newImpo) {
		this.importance=newImpo;
		
	};
	
	public void setIsDone(Boolean isdone) {
		this.isDone=isdone;
		
	};
	private void repeatTask() {
		Scanner repeatTaskScanner = new Scanner(System.in);
		String response;
		do response = repeatTaskScanner.nextLine();
		while (!response.matches("(?i)^(?:y|n|yes|no)$"));
		
		if (response=="Y"||response=="y") {
			//new time
			//new reference in the calendar
			System.out.println("new time");
		}
		else {
			this.isDone=true;
			System.out.println("task completed");
		}
		repeatTaskScanner.close();
	}
}
