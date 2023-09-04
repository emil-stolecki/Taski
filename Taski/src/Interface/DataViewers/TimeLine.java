package Interface.DataViewers;
import javax.swing.*;
import Interface.MainWindow;
import Interface.TaskInstance;
import tasksPackage.TaskFolder.Task;
import tasksPackage.other.TimeFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.util.HashMap;


public class TimeLine extends JPanel{
	
	private static HashMap<TaskInstance,Description> descriptions=new HashMap<TaskInstance,Description>();
	static Time time;
	static AssignedTasks atasks;	
	private Task[] tasks;
	private TimeFrame[] tf;
	
	public TimeLine(Task[] tasks,TimeFrame[] tf) {
		
		this.tasks=tasks;
		this.tf=tf;
		
		
		time =new Time();
		atasks= new AssignedTasks(tasks, tf);
		setMinimumSize(new Dimension(320, 0));				
	 	setLayout(new BorderLayout());	
	 	addListener(atasks);
	 	add(time, BorderLayout.WEST);
	 	add(atasks,BorderLayout.CENTER);

	}
	
	 
	 private void addListener(AssignedTasks at) {
		 
		 at.addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {	            	
	            	at.width = getWidth();
	            	at.height =getHeight();
	            	Component[] c= at.getComponents();
	            	
	            		for(Component task:c) {
		            		Rectangle r=task.getBounds(getBounds());
		            		task.setBounds(r.x, r.y, at.width-140, r.height);	            
	            		
	            	}
	            }
	            
	        });
		 
		
	 }
	 public static void addDesc(String[] text, int x, int y, int w,int h, int offset, TaskInstance ti) {
			Description d=new Description(text);
			int width=atasks.width;
			int maxH=MainWindow.getMainHeigth();			
			
			if(y+h+16*text.length<maxH+offset) {
				d.setBounds(x,y+h,width,16*text.length);
				
			}
			else if(y-16*text.length>offset){
				d.setBounds(x,y-16*text.length,width,16*text.length);
			
			}
			else if(y>offset){
				d.setBounds(x,y,width,16*text.length);
			}
			else {
				d.setBounds(x,y+h-16*text.length,width,16*text.length);
			}
			
			atasks.add(d,Integer.valueOf(1));
			descriptions.put(ti, d);
			atasks.revalidate();
			
			
			
		}
	public static void removeDesc(TaskInstance ti) {
			
			atasks.remove(descriptions.get(ti));
			descriptions.remove(ti);
			atasks.revalidate();
			atasks.repaint();
			
		} 
	
	public static TaskInstance assignTask(Task t) {	
		TaskInstance ti =new TaskInstance(t, 0, 0,					
    			0,0);
		
		ti.updateInstance();
		atasks.add(ti);
		return ti;
	}
	
	
}
