package tasksPackage.other;

import java.time.LocalDate;

import Interface.MainWindow;
import tasksPackage.StoragesFolder.Day;

public class Reloader {

	public static Day[] nextDays(Day[] old) {
		
		Day[] days = new Day[old.length];
		
		for(int i=0; i<(old.length-1)/2;i++) {
			days[i]=old[(old.length-1)/2+i];
			
		}
		LocalDate reference =days[0].getDate();
		for(int i=(old.length-1)/2; i<old.length;i++) {
			days[i]=new Day(reference.plusDays(Long.valueOf(i))) ;
			
		}
				
		return days;
	}
	
	public static Day[] previousDays(Day[] old) {
		
		Day[] days = new Day[old.length];
		
		for(int i=1+(old.length-1)/2; i<old.length;i++) {
			days[i]=old[i-(old.length-1)/2+1];
			
		}
		LocalDate reference =days[1+(old.length-1)/2].getDate();
		

		for(int i=1; i<=1+(old.length-1)/2;i++) {
			days[(old.length-1)/2+1-i]=new Day(reference.minusDays(Long.valueOf(i))) ;			
			
		}
				
		return days;
	}
	
	public static Day[] reload(Day[] old) {
		Day[] days = new Day[old.length];
		
		for(int i=0;i<old.length;i++) {
			days[i]= new Day(old[i].getDate());
		}
		
		return days;
	}
	
	public static Day[] find(LocalDate date) {
		int range=MainWindow.range;
		Day[] days = new Day[1+range*2];
		
		days[range]=new Day(date);
		for(int i=1;i<=range;i++) {
			
			days[range-i]=new Day(date.minusDays((long)i));
		}
		for(int i=1;i<range+1;i++) {
			
			days[range+i]=new Day(date.plusDays((long)i));
		}
		
		
		return days;
	}
	
	
}
