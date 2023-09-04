package Interface.DataModifiers;

import java.awt.Color;
import java.awt.Component;


import javax.swing.*;


import Interface.actionListeners.Actions;


public class OptsTopBar extends JPanel{
	
	OptsTopButton add = new OptsTopButton("new");
	OptsTopButton weekView = new OptsTopButton("Week");		
	OptsTopButton monthView = new OptsTopButton("Month");
	OptsTopButton yearView = new OptsTopButton("All Tasks");
	OptsTopButton nextDay = new OptsTopButton("Tomorrow");		
	OptsTopButton prevDay = new OptsTopButton("Yesterday");
	
	Actions ac = new Actions(add,weekView,monthView,yearView,nextDay,prevDay);
	OptsTopBar(){
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//setPreferredSize(new Dimension(600, 210));
						
		OptsTopContainer editOpts = new OptsTopContainer();
		OptsTopContainer viewOpts = new OptsTopContainer();
		OptsTopContainer dayOpts = new OptsTopContainer();	
		
		add(editOpts);
		add(viewOpts);
		add(dayOpts);
		

		add.addActionListener(ac);
		weekView.addActionListener(ac);
		monthView.addActionListener(ac);
		yearView.addActionListener(ac);
		nextDay.addActionListener(ac);
		prevDay.addActionListener(ac);
		
		
		editOpts.addButtons(Color.pink,add);		
		viewOpts.addButtons(Color.orange,weekView,monthView,yearView);			
		dayOpts.addButtons(Color.yellow,prevDay,nextDay);
		
		
		}
}
