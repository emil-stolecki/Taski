package tasksPackage.StoragesFolder;


import java.time.LocalDate;

import java.util.ArrayList;

import java.util.Comparator;


import DatabaseTools.CRUD;

import tasksPackage.EnumFolder.TaskType;
import tasksPackage.TaskFolder.Event;

import tasksPackage.TaskFolder.SimpleTask;
import tasksPackage.TaskFolder.Task;
import tasksPackage.other.DataBaseBridge;
import tasksPackage.other.TimeFrame;

public class Day {

	private LocalDate date;
	
	Comparator<TimeFrame> byStart = Comparator.comparing(TimeFrame::getStart);
	
    private ArrayList <Task> tasks = new ArrayList<Task>();
    private ArrayList  <TimeFrame> timeframes = new ArrayList<TimeFrame>();
    private ArrayList <SimpleTask> potentialTasks = new ArrayList<SimpleTask>();

   
    private int eventsCount;
    private int tasksCount;
    
	public Day(LocalDate date) {
		this.date =date;			
		this.initiate();
	}
	
	private void initiate(){

		ArrayList<Event> tmp= new ArrayList<Event>();
	
		tmp=DataBaseBridge.getEventArray(CRUD.getWhere(TaskType.Event, new String[]{"eventMode"},new String[] {"2"}));
		for(Event e:tmp) {
			this.tasks.add(e);
			this.timeframes.add( new TimeFrame(e.getStart(),e.getEnd(),e.getId()));
			eventsCount=eventsCount+1;
		}
		
		tmp=DataBaseBridge.getEventArray(CRUD.getByCustomPattern(0,this.date.toEpochDay()));
		for(Event e:tmp) {
			this.tasks.add(e);
			this.timeframes.add( new TimeFrame(e.getStart(),e.getEnd(),e.getId()));
			eventsCount=eventsCount+1;
		}
		tmp=DataBaseBridge.getEventArray(CRUD.getByPattern(0,this.date.getDayOfWeek().getValue()));
		for(Event e:tmp) {
			this.tasks.add(e);
			this.timeframes.add( new TimeFrame(e.getStart(),e.getEnd(),e.getId()));
			eventsCount=eventsCount+1;
		}
		tmp=DataBaseBridge.getEventArray(CRUD.getByPattern(1,this.date.getDayOfMonth()));
		for(Event e:tmp) {
			this.tasks.add(e);
			this.timeframes.add( new TimeFrame(e.getStart(),e.getEnd(),e.getId()));
			eventsCount=eventsCount+1;
		}
		tmp=DataBaseBridge.getEventArray(CRUD.getByCustomPattern(1,this.date.toEpochDay()));
		for(Event e:tmp) {
			this.tasks.add(e);
			this.timeframes.add( new TimeFrame(e.getStart(),e.getEnd(),e.getId()));
			eventsCount=eventsCount+1;
		}
		
		
		ArrayList<SimpleTask> tmp2= new ArrayList<SimpleTask>();
		tmp2=DataBaseBridge.getSimpleTaskArray(CRUD.getSimpleTask(this.date.toEpochDay()));
		for(SimpleTask s:tmp2) {
			
			this.tasks.add(s);
			if(s.getDuration().toMinutes()==0) {
				this.timeframes.add(new TimeFrame(s.getPreffered().toLocalTime(),s.getId()));
			}
			else {
				this.timeframes.add(new TimeFrame(s.getPreffered().toLocalTime(),
						s.getPreffered().toLocalTime().plusMinutes(s.getDuration().toMinutes()),s.getId()));
			}
			
						
			tasksCount=tasksCount+1;
		
			
		}
		
		
		potentialTasks=DataBaseBridge.getSimpleTaskArray(CRUD.getPotentialTasks(this.date));
		
				
		
	}	
	
	
	public boolean checkNoOverlap() {		
		boolean isok =true;
		TimeFrame curent;
		TimeFrame someNext;
		ArrayList <TimeFrame> tmp= new ArrayList <TimeFrame>(this.timeframes);
		tmp.sort(byStart);
		for(int i=0;i<tmp.size()-1;i++) {			
			curent=tmp.get(i);
			
			for(int j=i+1;j<tmp.size();j++) {	
				someNext=tmp.get(j);				
				if(!(curent.getStart().compareTo(someNext.getStart())==-1 &&
						curent.getEnd().compareTo(someNext.getStart())==-1)) {	
		
						isok=false;		
				}							
			}				
		}				
		return isok;
		
	}
	
	
	public LocalDate getDate() {
		return this.date;
	}
	
	
	public Task[] TimeLineTasksToList(){	
		
		Task[] tmp= tasks.toArray(new Task[tasks.size()]);
		
		return tmp;
	}
	
	public TimeFrame[] TimeLineTimeToList(){	
		
		TimeFrame[] tmp=timeframes.toArray(new TimeFrame[timeframes.size()]);
		
		return tmp;
	}
	
	public int eventCount() {
		return this.eventsCount;
	}
	public int taskCount() {
		return this.tasksCount;
	}
	
	
	public Task getTask(int id) {
		return this.tasks.get(id);
	}	
	public TimeFrame getTime(int id) {
		return this.timeframes.get(id);
	}	
	public void addTask(Task t) {
		this.tasks.add(t);
	}	
	public void addTime(TimeFrame t) {
		this.timeframes.add(t);
	}	
	public void removeTask(int id) {
		this.tasks.remove(id);
	}	
	public void removetTime(int id) {
		this.timeframes.remove(id);
	}	
	public SimpleTask getPotentialTask(int id) {
		return this.potentialTasks.get(id);
	}	
	public void addPotentialTask(SimpleTask t) {
		this.potentialTasks.add(t);
	}
	public void removepotentialTask(int id) {
		this.potentialTasks.remove(id);
	}
	public void removepotentialTask(SimpleTask t) {
		this.potentialTasks.remove(t);
	}
	public int getPotentialTasksCount() {
		return this.potentialTasks.size();
	}
	public int getTasksCount() {
		return this.tasks.size();
	}
	public boolean containsPotentialTask(SimpleTask t) {
		return this.potentialTasks.contains(t);
	}
}
