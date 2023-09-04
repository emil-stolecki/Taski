package Interface.DataModifiers;




import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import DatabaseTools.CRUD;
import Interface.MainWindow;
import Interface.TaskInstance;
import Interface.DataViewers.TimeLine;
import tasksPackage.EnumFolder.TaskType;
import tasksPackage.StoragesFolder.Day;
import tasksPackage.TaskFolder.SimpleTask;


public class Sidebar extends JPanel {
		
		private static JPanel asignedTasksReference;
		public static JPanel potentialTasksReference;
		private static Day day;
		private static ArrayList<JButton> todaysTasks;
		
public Sidebar(Day d) {
		
		day=d;
		todaysTasks= new ArrayList<JButton>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Border border = BorderFactory.createEtchedBorder();
		setBorder(BorderFactory.createTitledBorder(border));
		setMaximumSize(new Dimension(500,Integer.MAX_VALUE));
		setPreferredSize(new Dimension(200,600));
		setMinimumSize(new Dimension(200,200));
		asignedTasksReference= new JPanel();
		potentialTasksReference= new JPanel();
		
			  
		add(new JLabel("Tasks today"));	  
        add(asignedTasksReference);
        add(new JLabel("Add Tasks"));
        add(potentialTasksReference);
        refreshPotentialTasks(day);
        
        
        
    }

	public static void refreshPotentialTasks(Day d) {
				
		day =d;
		
		potentialTasksReference.removeAll();		
		for(int i=0;i<d.getPotentialTasksCount();i++) {
			SimpleTask task=d.getPotentialTask(i);
        	JButton button = new JButton(task.getTitle());		
        	potentialTasksReference.add(button);
    		button.addActionListener(new ActionListener() {
    			
    			@Override
    			public void actionPerformed(ActionEvent e) {
    				  				 
						 try {
							 int choice=JOptionPane.showConfirmDialog(null,"Are you sure you want to add this to today's schedule?","", JOptionPane.YES_NO_OPTION);
							 
							 if (choice == JOptionPane.YES_OPTION) {
								
								JButton jb=(JButton) e.getSource();							 	
			    				TaskInstance ti=TimeLine.assignTask(task);
			    				ti.task.changePrefferedTime(LocalDateTime.of(day.getDate(), LocalTime.now()));
			    				CRUD.updateSingle(TaskType.SimpleTask,ti.task.getId(),"PreferredTime",Long.toString(ti.task.getPreffered().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()));
			    				Workspace.viewTask(ti);
			    					
			    				
			    				MainWindow.makeshortcut(ti);
			    				potentialTasksReference.remove(jb);
			    				potentialTasksReference.revalidate();
			    				potentialTasksReference.repaint();
			    				Workspace.reload();			    				
			    				day.removepotentialTask(ti.task);
						 }}
							catch(Exception ex) {
								 System.out.println("Error: ");
								 ex.printStackTrace();
							}
						 							       
					     
    				
    				
    			}
    			
    		});		
            	
    		potentialTasksReference.revalidate();
    		potentialTasksReference.repaint();
        }
        
       
        
        
	}
	public static void removeAsignedTask(String title) {
		Boolean isFound=false;
		int i=0;
		
		while(isFound==false && i<asignedTasksReference.getComponentCount()) {
			
		if(todaysTasks.get(i).getText().equals(title)) {
			isFound=true;			
			asignedTasksReference.remove(todaysTasks.get(i));			
			todaysTasks.remove(i);
		}	
			
		
			i=i+1;
		}		
		asignedTasksReference.revalidate();
		asignedTasksReference.repaint();
	}
	public static void addTaskToday(JButton button){
		todaysTasks.add(button);
		asignedTasksReference.add(button);
	}
	
	public static void removeshortcuts() {
		todaysTasks.clear();
		asignedTasksReference.removeAll();
		asignedTasksReference.revalidate();
		asignedTasksReference.repaint();

	}
	public static Day getDay() {
		return day;
	}
}
