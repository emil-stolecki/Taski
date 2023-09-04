package tasksPackage;

import java.awt.Color;

public class GlobalVariables {

	 private static GlobalVariables INSTANCE;
	 
	 public final static int daysInMemory =65;// how many days will be taken from DB and temporarily stored at one time - must be odd number
	 public final static Color fillRed = new Color(255, 0, 0, 128);
	 public final static Color fillGreen = new Color(0, 155, 0, 128);
	 public final static Color fillBlue = new Color(0, 0, 255, 128);
	 public final static Color fillPurple = new Color(255, 0, 255, 128);
	 public final static Color completedFillRed=new Color(150, 200, 150);
	 public final static Color Red = new Color(255, 0, 0);
	 public final static Color Green = new Color(0, 155, 0); 	  
	 public static final int hourSpace=80;
	 public static final int margin=10;
	 private GlobalVariables() {
	       
	    }
	 
	 public static GlobalVariables getInstance() {
	        if (INSTANCE == null) {
	        	INSTANCE = new GlobalVariables();
	        }
	        return INSTANCE;
	    }
}
