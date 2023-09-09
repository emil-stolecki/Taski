package Interface;



import javax.swing.*;

import Interface.DataModifiers.OptsViewPanel;
import Interface.DataModifiers.Sidebar;
import Interface.DataModifiers.Workspace;
import Interface.DataViewers.DayViewPanel;
import Interface.DataViewers.Month;
import Interface.DataViewers.Week;
import tasksPackage.GlobalVariables;
import tasksPackage.EnumFolder.TaskType;
import tasksPackage.StoragesFolder.Day;
import tasksPackage.other.Reloader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MainWindow extends JFrame{
	
	
	private static int displayDay;
	private static DayViewPanel day;
	private static OptsViewPanel opts;
	

	private static JPanel main;
	public static int range;//how many days can be viewed forward and backwards, 
	//full range is 2*range+1 - amount of days that are loaded from the database at once
	private static Day[] days;
	private static LocalDate originalDay;//when switching to other views and back
	public MainWindow(Day[] d) {
		days=d;		
		range=(GlobalVariables.daysInMemory-1)/2;
		displayDay=range;
		
				
	
		setTitle("Main Window");
		setSize(new Dimension(1200,900));
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
       
        main = new JPanel();
      
        day = new DayViewPanel(d[displayDay]); 
        opts = new OptsViewPanel(d[displayDay]);
        
        
        if(d[displayDay].checkNoOverlap()) {
        	OptsViewPanel.warnings.noOverlapWarning(); 
        }
        else {
        	OptsViewPanel.warnings.overlapWarning(); 
        }
        
        add(main);
        main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
        main.add(day);
        main.add(opts);
      
        
       
        opts.setBackground(Color.GRAY);
      
      ; 
        
	}
	
	
	public static void previousDay() {
		
		if(displayDay>0) {
			if(Workspace.getReload()==true) {
				Workspace.resetReload();
				days=Reloader.reload(days);
				main.revalidate(); 
			    main.repaint();
			}
			displayDay=displayDay-1;
			
		}
		else {
			days=Reloader.previousDays(days);
			displayDay=range-2;
			
		}
		day.updateDay(days[displayDay]);
		Sidebar.refreshPotentialTasks(days[displayDay]);
		
		Workspace.updateDay(days[displayDay]);
		
		if(days[displayDay].checkNoOverlap()) {
        	OptsViewPanel.warnings.noOverlapWarning(); 
        	
        }
        else {
        	OptsViewPanel.warnings.overlapWarning(); 
        	
        	
        }
	}
	public static void nextDay() {
		if(displayDay<2*range) {
			if(Workspace.getReload()==true) {
				Workspace.resetReload();
				days=Reloader.reload(days);
				main.revalidate(); 
			    main.repaint();
			
			}
			displayDay=displayDay+1;			
			
		}
		else {
			days=Reloader.nextDays(days);
			displayDay=range+1;
			
		}
		day.updateDay(days[displayDay]);
		Workspace.updateDay(days[displayDay]);
		Sidebar.refreshPotentialTasks(days[displayDay]);
		
		if(days[displayDay].checkNoOverlap()) {
        	OptsViewPanel.warnings.noOverlapWarning(); 
        	
        }
        else {
        	OptsViewPanel.warnings.overlapWarning(); 
        	
        }
	}
	
	public static void makeshortcut(TaskInstance ti) {		
		JButton button = new JButton(ti.getTitle());
		Sidebar.addTaskToday(button);		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Workspace.viewTask(ti);
				
			}
			
		});
		opts.workspace.sidebar.revalidate();
		opts.workspace.sidebar.repaint();
		
	}
	
	
	public static void weekView() {
		originalDay=days[displayDay].getDate();
		main.removeAll();
		//change display day to monday		
		
		int today =days[displayDay].getDate().getDayOfWeek().getValue();
		LocalDate date=days[displayDay].getDate();
		displayDay=displayDay-today+1;				
		Day[]weekDays =new Day[7];
		
	
		if(displayDay>=days.length-6) {
			
			
			days=Reloader.nextDays(days);
			int ii=0;
			boolean found=false;
			
			while(!found&&ii<days.length) {	
				
				if(days[ii].getDate().equals(date)) {
					
					found=true;
				}
				else ii=ii+1;
				
			}
			
			displayDay=ii;
		}
		
		
		for(int i=0;i<7;i++) {
			weekDays[i]=days[displayDay+i];			
		}
		Week week = new Week(weekDays);
		main.add(week);
		main.revalidate(); 
	    main.repaint();
	}
	public static void dayView(Day d) {
		main.removeAll();
		day = new DayViewPanel(d); 
		main.add(day);
        main.add(opts);
          
        int offset=1+(int) ChronoUnit.DAYS.between(days[displayDay].getDate(), d.getDate());
      
        displayDay=displayDay+offset-1;
        Workspace.updateDay(days[displayDay]);
		main.revalidate(); 
	    main.repaint();
	}
	public static void backtoDayView() {
		main.removeAll();
		
		if(originalDay.compareTo(days[0].getDate())<0 || originalDay.compareTo(days[days.length-1].getDate())>0){
			  days=Reloader.find(originalDay);
			  displayDay=range;
		}
		else {
			displayDay = (int)ChronoUnit.DAYS.between(days[0].getDate(),originalDay);
			
		}
		day = new DayViewPanel(days[displayDay]); 
		Workspace.updateDay(days[displayDay]);
		main.add(day);
        main.add(opts);
        
		main.revalidate(); 
	    main.repaint();
	}
	public static void changeWeek(Day day, int mode) {
		//mode 0 ->previous week
		//mode 1->next week
		
		main.removeAll();
			
		Day[]weekDays =new Day[7];	
		//PREVIOUS WEEK
		if(mode==0){
						
			if(displayDay>6){
					
				displayDay=displayDay-7;				
				for(int i=0;i<7;i++) {
					weekDays[i]=days[displayDay+(i)];					
				}	
				
			}
			else {	
				
				days=Reloader.previousDays(days);
				
				
				int ii=days.length-1;
				boolean found=false;
				
				while(!found&&ii>=0) {					
					if(days[ii].getDate().equals(day.getDate())) {
						found=true;
					}
					else ii=ii-1;
				}
				
				displayDay=ii-1;
				
				for(int i=0;i<7;i++) {
					weekDays[6-i]=days[displayDay-i];					
				}
				displayDay=displayDay-6;
			}
			
		}//NEXT WEEK
		else {
			//display day is set to the monday of current week
			//day is the sunday of current week
			if(displayDay<days.length-7-6) {
				
				displayDay=displayDay+7;
				
				for(int i=0;i<7;i++) {
					weekDays[i]=days[displayDay+(i)];	
					
				}					
			}
			else {
				
				days=Reloader.nextDays(days);								
				int ii=0;
				boolean found=false;
				
				while(!found&&ii<days.length) {	
					
					if(days[ii].getDate().equals(day.getDate())) {
						
						found=true;
					}
					else ii=ii+1;
				}
								
				displayDay=ii+1;
				
				
				for(int i=0;i<7;i++) {
					weekDays[i]=days[displayDay+i];	
					
				}	
				
			}
			
		}
		
		
		Week week = new Week(weekDays);		
		main.add(week);
		main.revalidate(); 
	    main.repaint();
	}
	
	public static void monthView() {
		originalDay=days[displayDay].getDate();
		
		main.removeAll();
		int today =days[displayDay].getDate().getDayOfMonth();
		LocalDate date=days[displayDay].getDate();
		int numberofDays =date.lengthOfMonth();
		Day[] monthDays = new Day[numberofDays];
		
		
		
		if(displayDay-today+1<0) {
					
			days=Reloader.previousDays(days);
			int ii=0;
			boolean found=false;
			
			while(!found&&ii<days.length) {	
				
				if(days[ii].getDate().equals(date)) {
					
					found=true;
				}
				else ii=ii+1;
				
			}
			
			displayDay=ii;
			
		}
		
		
		displayDay=displayDay-today+1;//sets display day as the first day of the month
		LocalDate firstDay=days[displayDay].getDate();
		
		
		if (days.length-displayDay<numberofDays) {
			
			days=Reloader.nextDays(days);
			int ii=0;
			boolean found=false;
			
			while(!found&&ii<days.length) {	
				
				if(days[ii].getDate().equals(firstDay)) {
					
					found=true;
				}
				else ii=ii+1;
				
			}
			
			displayDay=ii;
			
		}
		
		for(int i=0;i<numberofDays;i++) {
			monthDays[i]=days[displayDay+i];			
		}
		
		monthDays[0]=days[displayDay];
		Month month= new Month(monthDays);
		main.add(month);
		main.revalidate(); 
	    main.repaint();
	}
	
	public static void changeMonth(Day d,int mode) {
		//mode 0 ->previous month
		//mode 1->next month	
		
		main.removeAll();
		
		
		if(mode==0) {
			//d->first day of current month	
			//days[displayDay] is d
			int numberofDays =d.getDate().minusDays(1).lengthOfMonth();//last month length
			LocalDate  firstDay=d.getDate().minusDays(numberofDays);//first day of last month
			Day[] monthDays=new Day[numberofDays];
			if(displayDay-numberofDays<=0) {								
				days=Reloader.previousDays(days);
				int ii=days.length-1;
				boolean found=false;			
				while(!found&&ii>=0) {					
					if(days[ii].getDate().equals(firstDay)) {
						found=true;
					}
					else ii=ii-1;
				}				
				displayDay=ii;
				
			}
			else {				
				
				displayDay=displayDay-numberofDays;
				
				
			}
			for(int i=0;i<numberofDays;i++) {
				monthDays[i]=days[displayDay+i];			
			}
			
			monthDays[0]=days[displayDay];
			Month month= new Month(monthDays);
			main.add(month);
			
			
		}
		else if(mode==1) {
			//d last day of current month
			
			int numberofDays =d.getDate().plusDays(1).lengthOfMonth();//next month length 
			LocalDate  firstDay=d.getDate().plusDays(1);//first day of next month
			
			Day[] monthDays=new Day[numberofDays];
			
			if(days.length-1<displayDay+d.getDate().lengthOfMonth()+numberofDays) {
				days=Reloader.nextDays(days);
				int ii=0;
				boolean found=false;				
				while(!found&&ii<days.length) {	
					
					if(days[ii].getDate().equals(firstDay)) {
						
						found=true;
					}
					else ii=ii+1;
					
				}
				
				displayDay=ii;
			}
			else {
				displayDay=displayDay+d.getDate().lengthOfMonth()+numberofDays;				
				
			}
			for(int i=0;i<numberofDays;i++) {					
				monthDays[i]=days[displayDay+i];			
			}
			
			monthDays[0]=days[displayDay];
			Month month= new Month(monthDays);
			main.add(month);
			
			
		}
		
		main.revalidate(); 
	    main.repaint();
		
	}
	
	public static void viewAllTasks() {
		
		main.removeAll();
		originalDay=days[displayDay].getDate();		
		AllTasks all= new AllTasks();
		main.add(all);
		main.revalidate(); 
	    main.repaint();
	}
	public static void findDay(LocalDate date,int id, TaskType t) {
		
		
		if(date.compareTo(days[0].getDate())<0 || date.compareTo(days[days.length-1].getDate())>0){
			  days=Reloader.find(date);
			  displayDay=range;
		}
		else {
			displayDay = (int)ChronoUnit.DAYS.between(days[0].getDate(),date);
			
		}
		
		
		main.removeAll();		
		day = new DayViewPanel(days[displayDay]); 
		main.add(day);
        main.add(opts);
        
        int taskId=-1;
        boolean isfound=false;
        int i=0;       
        	while (i<day.getTasks().length&&!isfound) {        		
            	if(day.getTasks()[i].getId()==id&&day.getTasks()[i].getType()==t) {
            		taskId=i;
            		isfound=true;            		
            	}
            	i++;
        	}
                
        TaskInstance ti=null;
        if(taskId!=-1) {
        	ti=new TaskInstance(day.getTasks()[taskId],0,0,0,0);
        	
        }
        else {
        	i=0;       
        	while (i<days[displayDay-1].getPotentialTasksCount()&&!isfound) {        		
            	if(days[displayDay-1].getPotentialTask(i).getId()==id) {
            		taskId=i;
            		isfound=true;
            		
            	}
            	i++;
        	}
        	
        	ti=new TaskInstance(days[displayDay-1].getPotentialTask(i-1),0,0,0,0);
        }
       
        Workspace.viewTask(ti);
        Workspace.updateDay(days[displayDay]);
		main.revalidate(); 
	    main.repaint();
		
	}
	
	public static int getMainHeigth() {
		return main.getHeight();
	}
	public static void reloadDays() {
		days=Reloader.reload(days);
	}
}
