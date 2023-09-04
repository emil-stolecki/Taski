package Interface.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Interface.MainWindow;
import Interface.DataModifiers.OptsTopButton;
import Interface.DataModifiers.Workspace;

public class Actions implements ActionListener{

	OptsTopButton add,weekView,monthView,yearView,nextDay,prevDay;
	
	public Actions(OptsTopButton add, OptsTopButton weekView, OptsTopButton 
						monthView, OptsTopButton yearView, OptsTopButton nextDay, OptsTopButton prevDay){
		this.add=add;
		this.weekView=weekView;
		this.monthView=monthView;
		this.yearView=yearView;
		this.nextDay=nextDay;
		this.prevDay=prevDay;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		OptsTopButton source=(OptsTopButton) e.getSource();
		
		if(source==add) {
			Workspace.addtask();
			
		} else if(source==weekView){
			MainWindow.weekView();
			
		}else if(source==monthView){
			MainWindow.monthView();
			
		}else if(source==yearView){
			MainWindow.viewAllTasks();
			
		}else if(source==nextDay){			
			MainWindow.nextDay();		
			
		}else if(source==prevDay){	
			MainWindow.previousDay();
		}	
		
		
	}

}