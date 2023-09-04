package tasksPackage.TaskFolder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import DatabaseTools.Objects.EventModel;
import tasksPackage.EnumFolder.Mode;
import tasksPackage.EnumFolder.TaskType;

public class Event extends Task{
	//private int instanceId=this.id+new Random().nextInt();//the same Event repeating weekly will have the same id but different instance id for every new reference in the calendar
	private LocalTime[] duration = new LocalTime[2];
	private Mode eventMode;	
	private Boolean [] weekPattern = new Boolean[7];//0-monday...6-sunday
	private Boolean [] monthPattern = new Boolean[31];
	private int gap;// repeat each [gap] days
	
	
	
	//repeat patern variable
	
	public Event(String title, String description, int importance, LocalTime startTime, LocalTime endTime) {
		super(title,description, importance,TaskType.Event);
		this.duration[0]=startTime;
		this.duration[1]=endTime;
		this.eventMode=Mode.oneTime;
		
	}
	public Event(EventModel a) {
		super(a.id, a.title, a.description, a.dateAdded, a.importance, TaskType.Event);
		this.duration[0]=a.startTime;
		this.duration[1]=a.endTime;
		this.eventMode=a.eventMode;		
		this.weekPattern=a.weekPattern;
		this.monthPattern=a.monthPattern;
		if(a.gap!=0) this.gap=a.gap;
		
	}
	
	//get methods
	
	//public int getInstanceId() {
		//return this.instanceId;
	//} 
	public Mode getMode() {
		return this.eventMode;
	}
	
	public LocalTime getStart() {
		return this.duration[0];
	}
	public LocalTime getEnd() {
		return this.duration[1];
	}
	public boolean[] getWeekPattern() {
		boolean [] pattern= new boolean[7];	
		if (this.weekPattern[0]!=null) {
			for(int i=0; i<7;i++) {
				pattern[i]=this.weekPattern[i];
			}
		}
		return pattern;
	}
	public boolean[] getMonthPattern() {
		boolean [] pattern= new boolean[31];
		if (this.monthPattern[0]!=null) {
			for(int i=0; i<31;i++) {
				pattern[i]=this.monthPattern[i];
			}
		}
		return pattern;
	}
	public int getCustomPattern() {
		return this.gap;
	}
	
	
	
	//update methods
	
	public void editDuration(LocalTime newStart,LocalTime newEnd) {//change duration for a single instance, a period or all similar Events
		if(newStart.isBefore(newEnd)) {
			this.duration[0]=newStart;
			this.duration[1]=newEnd;
			
		
		}
		
	}
	
	
	
	public void editCyclic(Mode newMode) {//changes mode to an one-time Event, daily, weekly,monthly Event
		this.eventMode=newMode;
	}
	
	public void resetCyclic() {//reset old pattern
		
		this.weekPattern= new Boolean[7];		
		this.monthPattern = new Boolean[31];
		this.gap=0;
		
	}
	//make new pattern
	public void editCyclicWeek(Boolean [] repeatOnDays)  {
		if(repeatOnDays.length == 7){
			this.weekPattern=repeatOnDays;
		}
		
	}
	
	public void editCyclicMonth(Boolean [] repeatOnDays)  {
		if(repeatOnDays.length == 31){
			this.monthPattern=repeatOnDays;
		}
		
	}
	
	public void editCyclicXdays(int newGap) {
		this.gap=newGap;
	}
	
	
	
	public void cancel(int id) {//deletes a reference from the timeline 
		
	}
	public void cancelMany(int id) {//cancel all similar Events in specified period
		
	}
	public void changePeriod(LocalDate startDay,int newPeriod) {//set period in which the Event repeats
		
	}
	@Override
	public String[] preview() {
		 String[] tmp=new String[7];
		 tmp[0]="Title: "+this.getTitle();
		 tmp[1]="Description: "+this.getDesc();
		 tmp[2]="Added: "+this.getDateAdded();
		 tmp[3]="Importance: "+ this.getImportance();
		 tmp[4]="Finished: "+ this.isDone();
		 tmp[5]="Time: "+this.duration[0]+"-"+this.duration[1];
		 tmp[6]="Planned: "+this.eventMode;
		return tmp;
	}
	
	public void testPrint() {
		System.out.println(this.getId());
		System.out.println(this.getTitle());
		System.out.println(this.getDesc());
		System.out.println(this.getImportance());
		System.out.println(this.getDateAdded());
		System.out.println(this.getSatisfaction());
		System.out.println(this.getStart());
		System.out.println(this.getEnd());
		System.out.println(this.getMode());
		System.out.println(this.getWeekPattern());
		System.out.println(this.getMonthPattern());
		System.out.println(this.getCustomPattern());
		System.out.println("");
		
	}
	
}
