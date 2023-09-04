package Interface.DataViewers;

import javax.swing.*;
import javax.swing.border.Border;

import Interface.DataModifiers.OptsViewPanel;
import tasksPackage.StoragesFolder.Day;
import tasksPackage.TaskFolder.Task;
import tasksPackage.other.TimeFrame;

import java.awt.*;


public class DayViewPanel extends JPanel  {
	
	private static Task[] tasks;
	private static TimeFrame[] time;
	private static JLabel date;
	private static TimeLine t;

	private  static JScrollPane scrollPane;
	
	public DayViewPanel(Day d) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));					
		Border border = BorderFactory.createEtchedBorder();
        setBorder(BorderFactory.createTitledBorder(border));
        setMinimumSize(new Dimension(400, 1500));          
        initiate(d);
          
    }
	
	private void initiate(Day d) {
		tasks =d.TimeLineTasksToList();
        time =d.TimeLineTimeToList();        
		date = new JLabel(d.getDate().toString()+" | "+d.getDate().getDayOfWeek());
		t= new TimeLine(tasks,time);
		add(date);				
    
	    date.setAlignmentX(Component.CENTER_ALIGNMENT);
        date.setAlignmentY(Component.TOP_ALIGNMENT);
        t.setPreferredSize(new Dimension(300,2000));
        
        scrollPane = new JScrollPane(t);
        scrollPane.setPreferredSize(new Dimension(400, 1000));
        add(scrollPane);
        scrollPane.setViewportView(t);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        int position=600;
        verticalScrollBar.setValue(position);
        
	}
	public void updateDay(Day newDay) {
		 remove(date);	
		 scrollPane.remove(t);
		 remove(scrollPane);
		 
		 revalidate(); 
	     repaint();	     
	     initiate(newDay);
	     
	     
	     if(newDay.checkNoOverlap()) {
	        	OptsViewPanel.warnings.noOverlapWarning(); 
	        	
	        }
	        else {
	        	OptsViewPanel.warnings.overlapWarning(); 
	        	
	        }
	    
	}

	public static void refreshTimeline() {
		t.revalidate();
		t.repaint();
	}
	
	public static JScrollPane getSrollPane() {
		return scrollPane;
	}
	public Task[] getTasks() {
		return tasks;
	}
}
