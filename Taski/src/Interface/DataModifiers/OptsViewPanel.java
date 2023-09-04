package Interface.DataModifiers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import tasksPackage.StoragesFolder.Day;


public class OptsViewPanel extends JPanel{

	public OptsTopBar topBar;	
	public Workspace workspace;
    public static Warnings warnings;
    public static JPanel asignedTasksReference;
	public static JPanel potentialTasksReference;
	
	public OptsViewPanel(Day day) {
		setLayout(new BorderLayout());			
		Border border = BorderFactory.createEtchedBorder();
        setBorder(BorderFactory.createTitledBorder(border));
       
               
        topBar = new OptsTopBar();
        workspace = new Workspace(day);       
        warnings = new Warnings();   
        ActionListener ac =new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(workspace.checkValidInpput()) {
		        	warnings.noBadInputlapWarning();	        	
		        }
		        else {
		        	warnings.badInputlapWarning();
		        	
		        }
		       
				
			}
        	
        };
        Workspace.addListnertoConfirm(ac);
      
        add(topBar,BorderLayout.NORTH);
        add(workspace,BorderLayout.CENTER);
       
        add(warnings,BorderLayout.SOUTH);
                		 
   		 
        
        
    }
	
	public static void displayAsignedTasks() {
		asignedTasksReference=new JPanel();
		asignedTasksReference.setLayout(new FlowLayout());
		
		asignedTasksReference.setBounds(new Rectangle(0,30,600,210));
		asignedTasksReference.setBackground(Color.red);
	}
	public static void hideAsignedTasks() {
		
		
	}
	
	
	public void displayPotentialTasks() {
		potentialTasksReference=new JPanel();
		potentialTasksReference.setLayout(new FlowLayout());
		
		potentialTasksReference.setBounds(new Rectangle(0,230,600,210));
	}
	
	
}
