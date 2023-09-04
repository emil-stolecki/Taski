package tasksPackage.other;

import java.time.LocalTime;

public class TimeFrame {

	private int taskId;
	private LocalTime start;
	private LocalTime end;
	
	public TimeFrame(LocalTime start,int taskId) {
		this.taskId=taskId;
		this.start = start;
		this.end = start.plusMinutes(30);
		}
	
	public TimeFrame(LocalTime start,LocalTime end,int taskId) {
		this.taskId=taskId;
		this.start = start;
		this.end = end;
		}
	
	public LocalTime getStart() {
		return this.start;
	}
	public LocalTime getEnd() {
		return this.end;
	}
	public int getId() {
		return this.taskId;
	}
	
	public void setStart(LocalTime s) {
		this.start=s;
	}
	public void setEnd(LocalTime e) {
		this.end=e;
	}
	
}
