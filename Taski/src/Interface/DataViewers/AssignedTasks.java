package Interface.DataViewers;

import java.awt.Color;
import java.awt.Graphics;
import java.time.LocalTime;
import javax.swing.JLayeredPane;
import Interface.MainWindow;
import Interface.TaskInstance;
import Interface.DataModifiers.Sidebar;
import tasksPackage.GlobalVariables;
import tasksPackage.TaskFolder.Task;
import tasksPackage.other.TimeFrame;

//stores tasks for a day
public class AssignedTasks extends JLayeredPane {
	
	int margin;  
	int hourSpace;
	int width;
	int height;
	Task[] tasks;
	TimeFrame[] tf;

	
	public AssignedTasks ( Task[] tasks,TimeFrame[] tf){
			
		margin= GlobalVariables.margin;  
		hourSpace= GlobalVariables.hourSpace;
		this.tasks=tasks;
		this.tf=tf;
		
		setLayout(null);
    		
		  				
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
       width=getWidth();
                
        g.drawLine(margin,2*margin-5,getWidth(),2*margin-5);
        for (int i=0; i<24; i++) {
        	drawScale(g,margin,hourSpace,i);
        }
              
        
    }
	
	
	
	@Override
    public void addNotify() {
        super.addNotify();
        
        removeAll();
        Sidebar.removeshortcuts();
        for (int i=0;i<tasks.length; i++) {
   
			Task t=tasks[i];			
			LocalTime ts=tf[i].getStart();
        	LocalTime te=tf[i].getEnd();
			int offsetTop=ts.getHour()*hourSpace+ts.getMinute()*hourSpace/60;
        	int offsetBottom=te.getHour()*hourSpace+te.getMinute()*hourSpace/60;
        	
        	TaskInstance ti =new TaskInstance(t, margin, margin+offsetTop+5,					
    			this.width-margin,offsetBottom-offsetTop);
        	
        	add(ti,Integer.valueOf(0));
        	MainWindow.makeshortcut(ti);
        	
      		
		} 
              
       
    }
	
	private void drawScale(Graphics g, int margin, int hh, int i) {
		
 		g.setColor(Color.lightGray);
     	g.drawLine(margin,2*margin+i*hh+hh/4-5,getWidth(),2*margin+i*hh+hh/4-5);
     	g.drawLine(margin,2*margin+i*hh+hh/2-5,getWidth(),2*margin+i*hh+hh/2-5);
     	g.drawLine(margin,2*margin+i*hh+hh/4*3-5,getWidth(),2*margin+i*hh+hh/4*3-5);
		
     	g.setColor(Color.darkGray);   
 		g.drawLine(margin,2*margin-5+(i+1)*hh,getWidth(),2*margin-5+(i+1)*hh);
 		
	}
	
	public int getH() {
		return this.height;
	}
	
	
}
