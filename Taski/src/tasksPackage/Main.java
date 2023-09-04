package tasksPackage;



import Interface.MainWindow;

import tasksPackage.StoragesFolder.Day;



import java.time.LocalDate;

import DatabaseTools.TableControls;


/////ADDD CREDDDITTT FOR Jdatepicker




public class Main {
	public static void main(String[] args){
		
		
		TableControls.create();
	  		
		LocalDate today= LocalDate.now();//will be displayed first 
		
		Day [] cal= new Day[GlobalVariables.daysInMemory];
		// GlobalVariables.daysInMemory -how many days will be taken from DB and temporarily stored at one time - must be odd number
		for(int i=0;i<GlobalVariables.daysInMemory;i++) {
			long dayid =i+(GlobalVariables.daysInMemory-1)/-2;
			LocalDate day =today.plusDays(dayid);
			cal[i]= new Day(day);
		}
		
				
		
	
	    MainWindow mainWindow = new MainWindow(cal);
	    mainWindow.setVisible(true);
	    
	    
	}
}
