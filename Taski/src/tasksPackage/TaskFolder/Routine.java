package tasksPackage.TaskFolder;

import java.util.Date;
import java.util.Random;

import tasksPackage.EnumFolder.Mode;
import tasksPackage.EnumFolder.TaskType;

public class Routine extends Task {
	int instanceId=new Random().nextInt();//the same event repeating weekly will have the same id but different instance id for every new reference in the calendar
	Mode routineMode;
	
	Boolean [] weekPattern = new Boolean[7];//0-monday...6-sunday
	Boolean [] monthPattern = new Boolean[31];
	int gap;// repeat each [gap] days
	
	public Routine(String title, String description, int importance, Mode routineMode) {
		super(title, description, importance,TaskType.Routine);
		this.routineMode=routineMode;
		
	}
	
	/////get methods
	public int getInstance() {
		return this.instanceId;
	}
	public Mode getMode() {
		return this.routineMode;
	}
	
	/////update methods
	public void editMode(Mode newMode) {
		this.routineMode=newMode;
	}
	public void resetCyclic() {//reset old pattern
		
		this.weekPattern= new Boolean[7];		
		this.monthPattern = new Boolean[31];
		this.gap=0;
		
	}
	//make new pattern
	public void editCyclicWeek(Boolean [] repeatOnDays) throws Exception {
		if(repeatOnDays.length != 7){
		    throw new Exception("Array of days must have a length of 7.");
		}
		else {
			this.weekPattern=repeatOnDays;
		}
	}
	
	public void editCyclicMonth(Boolean [] repeatOnDays) throws Exception {
		if(repeatOnDays.length != 31){
		    throw new Exception("Array of days must have a length of 31.");
		}
		else {
			this.monthPattern=repeatOnDays;
		}
	}
	
	public void editCyclicXdays(int newGap) {
		this.gap=newGap;
	}
	
	
	/////calendar methods to finish
	public void calculateNext() {
		switch(this.routineMode) {
		case oneTime:
			break;
		case daily:
		break;
		case weekly:
		break;
		case monthly:
		break;
		case custom:
		break;
		}
	}
	public void cancel(int id){//removes reference to a single event from the calendar 
		
	}
	public void cancelMany(Date startDay, int period) {
		
	}
}
