package Interface.DataModifiers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import org.jdatepicker.DateModel;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import DatabaseTools.CRUD;
import Interface.MainWindow;
import Interface.TaskInstance;
import Interface.DataViewers.DayViewPanel;
import Interface.DataViewers.TimeLine;
import tasksPackage.GlobalVariables;
import tasksPackage.EnumFolder.Mode;
import tasksPackage.EnumFolder.TaskType;
import tasksPackage.StoragesFolder.Day;
import tasksPackage.TaskFolder.Event;
import tasksPackage.TaskFolder.SimpleTask;
import tasksPackage.other.DataBaseBridge;

public class Workspace extends JPanel{
	
	private static Day day;
	public static Sidebar sidebar;
	private static TaskEditor task;	
	private static JPanel buttons;
	private static JPanel caption;
	private static JButton deleteButton;
	private static JButton completeButton;
	private static JButton restoreButton;
	public static JButton confirmButton;
	private static JButton addButton;	
	private static ActionListener invalidinputListener;
      
	 private static Color[] colors = {GlobalVariables.fillRed,
	    								GlobalVariables.fillGreen,
	    								GlobalVariables.fillBlue,
	    								GlobalVariables.fillPurple,
	    								GlobalVariables.completedFillRed}; 
	private static Color theme;
	
	private static JComboBox<String> comboBox;
	private static boolean isCorrectInput=true;	
	private static boolean shouldReload=false;//true after each change that affects other days in memory, not just current day
	public Workspace(Day d) {
		
		day=d;
		Border border = BorderFactory.createEtchedBorder();
		setBorder(BorderFactory.createTitledBorder(border));
		setLayout(new BorderLayout());
		task = new TaskEditor();
		sidebar = new Sidebar(day);
		buttons =new JPanel();
		caption =new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.LEFT));		
		caption.setLayout(new FlowLayout(FlowLayout.LEFT));	
		
		deleteButton = new JButton("Delete");
		completeButton = new JButton("Complete");
		restoreButton = new JButton("Restore");
		confirmButton = new JButton("Confirm");
		addButton=  new JButton("Add");
		
		
		
		buttons.setPreferredSize(new Dimension(500,35));
		add(buttons,BorderLayout.SOUTH);
		add(task,BorderLayout.WEST);
		add(sidebar, BorderLayout.EAST);
		add(caption,BorderLayout.NORTH);		
		caption.add(new JLabel("|"));
		
	}
	
	public boolean checkValidInpput() {
		return isCorrectInput;
	}
	
	public static void addtask() {
		caption.removeAll();
		caption.add(new JLabel("Add new task:"));
		caption.revalidate();
		caption.repaint();
		buttons.removeAll();
		buttons.add(addButton);		
		buttons.revalidate();
		buttons.repaint();
		task.r.removeAll();
		task.l.removeAll();
		task.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		
		task.l.add(Box.createRigidArea(new Dimension(0,10)));
		task.r.add(Box.createRigidArea(new Dimension(0,10)));
		
		task.addField(task.l,"Name ",task.font, Color.gray);
		task.addField(task.l,"Description ",task.font, Color.gray);
		task.addField(task.l,"Importance ",task.font, Color.gray);
		
		task.title=task.addInput(task.r,"",task.font, Color.gray);
		task.desc=task.addInput(task.r,"",task.font, Color.gray);
		task.importance=new JSlider(JSlider.HORIZONTAL,1,5,3);
		task.importance.setPaintTicks(true);
		task.importance.setPaintLabels(true);
		task.importance.setMaximumSize(new Dimension(300,30));
		task.importance.setPreferredSize(new Dimension(300,30));
		task.r.add(task.importance);
		
		task.r.add(Box.createRigidArea(new Dimension(0,5)));
		JPanel line = new JPanel();
		line.setBackground(Color.gray);
		line.setPreferredSize(new Dimension(200,1));
		line.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
		line.setMinimumSize(new Dimension(200,1));
		task.r.add(line);
		task.r.add(Box.createRigidArea(new Dimension(0,5)));
		
		task.addField(task.l,"Type ",task.font, Color.gray);
		String[] options = {"Choose type of task","SimpleTask","Event"};		
		task.type=new JComboBox<>(options);
		task.type.setSelectedItem("Choose type of task");
				
		
		task.type.setPreferredSize(new Dimension(250,27));
		task.type.setMaximumSize(new Dimension(250,27));
		task.r.add(task.type);
		
		task.r.add(Box.createRigidArea(new Dimension(0,8)));
		JPanel line1 = new JPanel();
		line1.setBackground(Color.gray);
		line1.setPreferredSize(new Dimension(200,1));
		line1.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
		line1.setMinimumSize(new Dimension(200,1));
		task.r.add(line1);
		
		task.typeLabel=new JPanel();
		task.typeLabel.setLayout(new BoxLayout(task.typeLabel, BoxLayout.Y_AXIS));
        task.typeInput=new JPanel(); 
        task.typeInput.setLayout(new BoxLayout(task.typeInput, BoxLayout.Y_AXIS));
        task.l.add(Box.createRigidArea(new Dimension(0,2)));
        task.l.add(task.typeLabel);
        task.r.add(task.typeInput);
        
		task.type.addActionListener(new ActionListener() {
			 
			@Override
			public void actionPerformed(ActionEvent ev) {
				JComboBox<String> source = (JComboBox<String>) ev.getSource();
	            String selectedMode = (String) source.getSelectedItem();
	            task.typeLabel.removeAll();
	            task.typeInput.removeAll();
	            if(selectedMode=="SimpleTask") {
	            	task.addField(task.typeLabel,"Deadline ",task.font,Color.gray);
	            	task.typeLabel.add(Box.createRigidArea(new Dimension(0,5)));
	            	task.addField(task.typeLabel,"Preferred Time",task.font,Color.gray);	            	
	            	task.addField(task.typeLabel,"Time to fulfil ",task.font,Color.gray);
	            	
	    			UtilDateModel modelD = new UtilDateModel();
	    			UtilDateModel modelP = new UtilDateModel(java.sql.Date.valueOf(
	    					LocalDate.of(day.getDate().getYear(), day.getDate().getMonthValue(), day.getDate().getDayOfMonth())));	
	    			
	    			
	     			Properties p = new Properties();	     			
	     			JDatePanelImpl deadline = new JDatePanelImpl(modelD, p);	     			
	     			JDatePanelImpl prefered = new JDatePanelImpl(modelP, p);
	     			task.deadline = new JDatePickerImpl(deadline,  new DateComponentFormatter());
	     			task.prefered = new JDatePickerImpl(prefered,  new DateComponentFormatter());
	     			task.useDuration = new JCheckBox();
	     			task.useDeadline = new JCheckBox();
	     			task.usePrefered = new JCheckBox();
	     			
	     			task.useDuration.setSelected(true);
	     			task.useDeadline.setSelected(true);
	     			task.usePrefered.setSelected(true);
	     			
	     			
	     			
	     			
	     			task.deadline.setPreferredSize(new Dimension(80,27));
	     			task.prefered.setPreferredSize(new Dimension(80,27));
	     			
	     			JPanel boxD = new JPanel();//flow [checkBox|dateBoxD|timeBoxD]
	     			JPanel boxP = new JPanel();//flow [checkBoxtimeBoxP|timeBoxP]
	     			JPanel dateBoxD = new JPanel();//box [datepicker]
	     			JPanel timeBoxD = new JPanel();//flow [hh:mm]
	     			JPanel dateBoxP = new JPanel();//box [datepicker]
	     			JPanel timeBoxP = new JPanel();//flow [hh:mm]
	     			
	    			
	     			boxD.setMaximumSize(new Dimension(400,40));
	     			boxP.setMaximumSize(new Dimension(400,40));
	     			dateBoxD.setLayout(new BoxLayout(dateBoxD,BoxLayout.Y_AXIS));
	     			dateBoxP.setLayout(new BoxLayout(dateBoxP,BoxLayout.Y_AXIS));		     			     			
	     			task.typeInput.add(boxD);
	     			boxD.add(task.useDeadline);
	     			boxD.add(dateBoxD);
	     			boxD.add(timeBoxD);	     			
	     			dateBoxD.add(task.deadline);
	     			
	     			task.deadlineHour = new JTextPane();
	     			task.deadlineMinute= new JTextPane();
	     			task.deadlineHour.setPreferredSize(new Dimension(30,25));
	     			task.deadlineMinute.setPreferredSize(new Dimension(30,25));
	     			
	     			timeBoxD.add(task.deadlineHour);
	     			timeBoxD.add(new JLabel(" : "));
	     			timeBoxD.add(task.deadlineMinute);
	     			
	     			    				     			
	     			JPanel line2 = new JPanel();
	    			line2.setBackground(Color.gray);
	    			line2.setPreferredSize(new Dimension(200,1));
	    			line2.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
	    			line2.setMinimumSize(new Dimension(200,1));
	    			task.typeInput.add(line2);
	    			
	     			
	    			task.typeInput.add(boxP); 
	    			boxP.add(task.usePrefered);
	     			boxP.add(dateBoxP);
	     			boxP.add(timeBoxP);
	     				
	     			dateBoxP.add(task.prefered);	     			
	     			task.preferedHour = new JTextPane();
	     			task.preferedMinute = new JTextPane(); 
	     			task.preferedHour.setPreferredSize(new Dimension(30,25));
	     			task.preferedMinute.setPreferredSize(new Dimension(30,25));
	     			
	     			     			
	     			timeBoxP.add(task.preferedHour);
	     			timeBoxP.add(new JLabel(" : "));
	     			timeBoxP.add(task.preferedMinute);
	     			
	     			task.typeInput.add(Box.createRigidArea(new Dimension(0,3)));
	     			JPanel line3 = new JPanel();
	    			line3.setBackground(Color.gray);
	    			line3.setPreferredSize(new Dimension(200,1));
	    			line3.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
	    			line3.setMinimumSize(new Dimension(200,1));
	    			task.typeInput.add(line3);
	    			task.typeInput.add(Box.createRigidArea(new Dimension(0,2)));
	    			
	    			JPanel timeBox = new JPanel();
	    			timeBox.setMaximumSize(new Dimension(300,40));
	    			task.hours = new JTextPane();
	    			task.minutes = new JTextPane();
	    			task.minutes.setPreferredSize(new Dimension(30,30));
	    			task.hours.setPreferredSize(new Dimension(30,30));
	    			
	    			timeBox.add(task.useDuration);
	    			timeBox.add(task.hours);
	    			JLabel label=new JLabel(" : ");
	    			timeBox.add(label);
	    			timeBox.add(task.minutes);	    			
	    			task.typeInput.add(timeBox);
	    				    			
	    			task.typeInput.add(Box.createRigidArea(new Dimension(0,4)));
	    			JPanel line4 = new JPanel();
	    			line4.setBackground(Color.gray);
	    			line4.setPreferredSize(new Dimension(200,1));
	    			line4.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
	    			line4.setMinimumSize(new Dimension(200,1));
	    			task.typeInput.add(line4);
	    			task.typeInput.add(Box.createRigidArea(new Dimension(0,2)));
	    			
	    			
	    			task.useDeadline.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							
							if(task.useDeadline.isSelected()) {
								boxD.add(dateBoxD);
				     			boxD.add(timeBoxD);
				     			boxD.revalidate();
				     			boxD.repaint();
							}
							else {
								boxD.remove(dateBoxD);
				     			boxD.remove(timeBoxD);
				     			boxD.revalidate();
				     			boxD.repaint();
							}
							
						}});
	    			
	    			task.useDuration.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							
							if(task.useDuration.isSelected()) {
								timeBox.add(task.hours);
				    			timeBox.add(label);
				    			timeBox.add(task.minutes);	    
				    			timeBox.revalidate();
				    			timeBox.repaint();
							}
							else {
								timeBox.remove(task.hours);
				    			timeBox.remove(label);
				    			timeBox.remove(task.minutes);	 
				     			timeBox.revalidate();
				     			timeBox.repaint();
							}
							
						}});
	    		    task.usePrefered.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							if(task.usePrefered.isSelected()) {
								boxP.add(dateBoxP);
				     			boxP.add(timeBoxP);
				     			boxP.revalidate();
				     			boxP.repaint();
							}
							else {
								boxP.remove(dateBoxP);
				     			boxP.remove(timeBoxP);
				     			boxP.revalidate();
				     			boxP.repaint();
							}
							
						}});
	    			
	            }
	            else if(selectedMode=="Event") {
	            	task.addField(task.typeLabel,"Duration  ",task.font, Color.gray);
	    			task.addField(task.typeLabel,"Mode ",task.font, Color.gray);
	    				    			    			
	    			task.typeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    			    			
	    			task.startHour = new JTextPane();
	    			task.startMinute = new JTextPane();
	    			task.endHour = new JTextPane();
	    			task.endMinute = new JTextPane();
	    			
	    			task.startHour.setPreferredSize(new Dimension(30,25));
	    			task.startMinute.setPreferredSize(new Dimension(30,25));
	    			task.endHour.setPreferredSize(new Dimension(30,25));
	    			task.endMinute.setPreferredSize(new Dimension(30,25));	    			
	    			JPanel cont = new JPanel();
	    			cont.setMaximumSize(new Dimension(300,37));	    			
	    			cont.setMinimumSize(new Dimension(300,37));		    			
	    			task.typeInput.add(cont);
	    			cont.add(task.startHour);	    			
	    			cont.add(new JLabel(":"));
	    			cont.add(task.startMinute);	    			
	    			cont.add(new JLabel(" - "));
	    			cont.add(task.endHour);	    			
	    			cont.add(new JLabel(":"));
	    			cont.add(task.endMinute);	    
	    			
	    			
	    			
	    			JPanel line5 = new JPanel();
	    			line5.setBackground(Color.gray);
	    			line5.setMinimumSize(new Dimension(200,1));
	    			line5.setPreferredSize(new Dimension(200,1));
	    			line5.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
	    			task.typeInput.add(line5);
	    			task.typeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    			
	    			String[] options = {"oneTime","daily","weekly","monthly","custom"};			
	    		    task.mode = new JComboBox<>(options);	    		    
	    		    task.mode.setMinimumSize(new Dimension(250,30));
	    		    task.mode.setMaximumSize(new Dimension(250,30));
	    		    task.typeInput.add(task.mode);
	    		    
	    		    task.typeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    			JPanel line6 = new JPanel();
	    			line6.setBackground(Color.gray);
	    			line6.setMinimumSize(new Dimension(300,1));
	    			line6.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
	    			task.typeInput.add(line6);
	    			task.typeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    		    
	    		    task.editModeLabel=new JPanel();
	    		    task.editModeInput=new JPanel();
	    		    task.editModeLabel.setLayout(new BoxLayout(task.editModeLabel, BoxLayout.Y_AXIS));
	    		    task.editModeInput.setLayout(new BoxLayout(task.editModeInput, BoxLayout.Y_AXIS));		    
	    		    task.typeLabel.add(task.editModeLabel);			    		    
	    		    task.typeInput.add(task.editModeInput);
	    		    
	    		    task.addField(task.editModeLabel,"When", task.font, Color.gray );		     			
	     			UtilDateModel model = new UtilDateModel();
	     			Properties p = new Properties();
	     			JDatePanelImpl datePanel = new JDatePanelImpl(model, p);		     			
	     			task.setGap1 = new JDatePickerImpl(datePanel,  new DateComponentFormatter());
	     			
	     			task.editModeInput.add(task.setGap1);	    		    
	    			
	    		    task.mode.addActionListener(new ActionListener() {
	    		    
	    				@Override
	    				public void actionPerformed(ActionEvent ev) {
	    					JComboBox<String> source = (JComboBox<String>) ev.getSource();
	    		            String selectedMode = (String) source.getSelectedItem();
	    		            task.editModeLabel.removeAll();
	    		            task.editModeInput.removeAll();
	    		            
	    		            if(selectedMode=="weekly") {
	    		     			task.addField(task.editModeLabel,"Select days ", task.font, theme );
	    		     			JPanel row=new JPanel();
	    		     			JPanel col1=new JPanel();
	    		     			col1.setLayout(new BoxLayout(col1,BoxLayout.Y_AXIS));
	    		     			JPanel col2=new JPanel();
	    		     			col2.setLayout(new BoxLayout(col2,BoxLayout.Y_AXIS));
	    		     			JPanel col3=new JPanel();
	    		     			col3.setLayout(new BoxLayout(col3,BoxLayout.Y_AXIS));
	    		     			JPanel col4=new JPanel();
	    		     			col4.setLayout(new BoxLayout(col4,BoxLayout.Y_AXIS));
	    		     			JPanel col5=new JPanel();
	    		     			col5.setLayout(new BoxLayout(col5,BoxLayout.Y_AXIS));
	    		     			JPanel col6=new JPanel();
	    		     			col6.setLayout(new BoxLayout(col6,BoxLayout.Y_AXIS));
	    		     			JPanel col7=new JPanel();
	    		     			col7.setLayout(new BoxLayout(col7,BoxLayout.Y_AXIS));
	    		     			row.setMaximumSize(new Dimension(200,40));
	    		     			task.editModeInput.add(row);
	    		     			
	    		     			JPanel line6 = new JPanel();
	    		    			line6.setBackground(theme);
	    		    			line6.setPreferredSize(new Dimension(300,1));
	    		    			line6.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
	    		    			task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    		    			task.editModeInput.add(line6);
	    		    			task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    		    			
	    		     			task.mon= new JCheckBox();
	    		     			task.tue= new JCheckBox();
	    		     			task.wed= new JCheckBox();
	    		     			task.thu= new JCheckBox();
	    		     			task.fri= new JCheckBox();		     		
	    		     			task.sat= new JCheckBox();
	    		     			task.sun= new JCheckBox();
	    		     				    		     			
		     			
	    		     			row.add(col1);
	    		     			row.add(col2);
	    		     			row.add(col3);
	    		     			row.add(col4);
	    		     			row.add(col5);
	    		     			row.add(col6);
	    		     			row.add(col7);		     			
	    		     			col1.add(new JLabel("Mon"));
	    		     			col1.add(task.mon);
	    		     			col2.add(new JLabel("Tue"));
	    		     			col2.add(task.tue);
	    		     			col3.add(new JLabel("Wed"));
	    		     	        col3.add(task.wed);
	    		     	        col4.add(new JLabel("Thu"));
	    		     	        col4.add(task.thu);
	    		     	        col5.add(new JLabel("Fri"));
	    		     	        col5.add(task.fri);
	    		     	        col6.add(new JLabel("Sat"));
	    		     	        col6.add(task.sat);
	    		     	        col7.add(new JLabel("Sun"));
	    		     	        col7.add(task.sun);
	    		     		}
	    		     		else if (selectedMode=="monthly") {
	    		     			task.addField(task.editModeLabel,"Select days ", task.font, theme );
	    		     			task.setMonthDays = new JTextPane();
	    		     			String display ="";	    		     			
	    		     			
	    		     			
	    		     			if(display.length()>1) {
	    		     				task.setMonthDays.setText(display.substring(0,display.length()-1));
	    		     			}
	    		     			task.setMonthDays.setMaximumSize(new Dimension(300,25));
	    		     			task.editModeInput.add(task.setMonthDays);
	    		     			task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    		     			//task.editModeInput.add(new JLabel("Enter days of month separated by ,"));
	    		     		}
	    		     		else if (selectedMode=="custom") {
	    		     			task.addField(task.editModeLabel,"Days to skip ", task.font, Color.gray );
	    		     			task.setGap5 = new JTextPane();
	    		     			task.setGap5.setMaximumSize(new Dimension(200,25));
	    		     			task.editModeInput.add(task.setGap5);
	    		     			task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
	    		     		}
	    		     		else if (selectedMode=="oneTime") {
	    		     			task.addField(task.editModeLabel,"When", task.font, theme );
	    		     					     			
	    		     			UtilDateModel model = new UtilDateModel();
	    		     			Properties p = new Properties();
	    		     			JDatePanelImpl datePanel = new JDatePanelImpl(model, p);		     			
	    		     			task.setGap1 = new JDatePickerImpl(datePanel,  new DateComponentFormatter());
	    		     			
	    		     			task.editModeInput.add(task.setGap1);
	    		     			
	    		     			
	    		     		}
	    		           
	    		            
	    		            task.editModeInput.revalidate();
	    		            task.editModeInput.repaint();
	    				}
	    		    	
	    		    });
	    		    
	    		    
	            }
	            if(addButton.getActionListeners().length>0) {
	            	addButton.removeActionListener(addButton.getActionListeners()[0]);
	    		}
	            addButton.addActionListener(invalidinputListener);
	            addButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						shouldReload=true;
						
						if(task.type.getSelectedIndex()==0||task.type.getSelectedItem()==null){
							System.out.println("Select type");
						}
						else if(TaskType.valueOf(task.type.getSelectedItem().toString())==TaskType.SimpleTask) {
							try {
								
								DateModel tmpD= task.deadline.getModel();
								int hourD;
								int minuteD;
								if(task.deadlineHour.getText()=="") {
									hourD=0;									
								}
								else {
									hourD=Integer.parseInt(task.deadlineHour.getText());
								}
								if(task.deadlineMinute.getText()=="") {
									minuteD=0;
								}
								else {
									minuteD=Integer.parseInt(task.deadlineMinute.getText());
								}
								
								LocalDateTime tmpLDTD= LocalDateTime.of(tmpD.getYear(),tmpD.getMonth()+1,tmpD.getDay(),
										hourD,minuteD);
								
								
								DateModel tmpP= task.prefered.getModel();		
								int hourP;
								int minuteP;
								if(task.preferedHour.getText()=="") {
									hourP=0;									
								}
								else {
									hourP=Integer.parseInt(task.preferedHour.getText());
								}
								if(task.preferedMinute.getText()=="") {
									minuteP=0;
								}
								else {
									minuteP=Integer.parseInt(task.preferedMinute.getText());
								}
								LocalDateTime tmpLDTP= LocalDateTime.of(tmpP.getYear(),tmpP.getMonth()+1,tmpP.getDay(),
										hourP,minuteP);
								
								int h;
								int m;
								if(task.hours.getText()=="") {
									h=0;
								}
								else {
									h=Integer.parseInt(task.hours.getText());
								}
								if(task.minutes.getText()=="") {
									m=0;
								}
								else{
									m=Integer.parseInt(task.minutes.getText());
								}
								Duration d=Duration.ofSeconds(m*60+
										h*3600);
								
								
								SimpleTask t = new SimpleTask(task.title.getText(),task.desc.getText(),
										task.importance.getValue(),tmpLDTD,tmpLDTP,d);
								
								String[] st =DataBaseBridge.simpleTaskToArray(t);
								
								
								
								if(!task.useDeadline.isSelected()) {
									st[7]="0";
								}
								if(!task.usePrefered.isSelected()) {
									st[8]="0";
								}
								if(!task.useDuration.isSelected()) {
									st[9]="0";
								}
								
								
								CRUD.add(TaskType.SimpleTask,st);
								
								if(tmpLDTP.toLocalDate().equals(Sidebar.getDay().getDate())&&task.usePrefered.isSelected()) {	
									
									TaskInstance ti=TimeLine.assignTask(t);	
									MainWindow.makeshortcut(ti);
									
								}
									
								
							
						}
						catch(Exception exc){					
							isCorrectInput=false;
							System.out.println("Fill out all fields");
						}
						}	
					
						else if(TaskType.valueOf(task.type.getSelectedItem().toString())==TaskType.Event){
							try {
								LocalTime start= LocalTime.of(Integer.parseInt(task.startHour.getText()),
								Integer.parseInt(task.startMinute.getText()));
								LocalTime end= LocalTime.of(Integer.parseInt(task.endHour.getText()),
								Integer.parseInt(task.endMinute.getText()));
								
								Event ev =new Event(task.title.getText(),task.desc.getText(),task.importance.getValue(),start,end);
								
								
								if (task.mode.getSelectedIndex()+1==1) {					
									int y=task.setGap1.getModel().getYear();
									int m=task.setGap1.getModel().getMonth()+1;
									int d =task.setGap1.getModel().getDay();
									LocalDate date= LocalDate.of(y,m,d);
									long diff = ChronoUnit.DAYS.between(LocalDate.now(), date);
									if(diff>=0) {
										ev.editCyclic(Mode.oneTime);
										ev.editCyclicXdays((int) diff);
										
									}
									 
								}
								else if(task.mode.getSelectedIndex()+1==2){
									ev.editCyclic(Mode.daily);
								}
								else if(task.mode.getSelectedIndex()+1==3){
									Boolean[] newData= new Boolean[7];
									if(task.mon.isSelected()==true) {
										newData[0]=true;
									}						
									else {
										newData[0]=false;
									}
									if(task.tue.isSelected()==true) {
										newData[1]=true;
									}
									else {
										newData[1]=false;
															}
									if(task.wed.isSelected()==true) {
										newData[2]=true;
									}
									else {
										newData[2]=false;					}
									if(task.thu.isSelected()==true) {
										newData[3]=true;
									}
									else {
										newData[3]=false;
									}
									if(task.fri.isSelected()==true) {
										newData[4]=true;
									}
									else {
										newData[4]=false;
									}
									if(task.sat.isSelected()==true) {
										newData[5]=true;
									}
									else {
										newData[5]=false;
									}
									if(task.sun.isSelected()==true) {
										newData[6]=true;
									}
									else {
										newData[6]=false;
									}
									ev.editCyclic(Mode.weekly);
									ev.editCyclicWeek(newData);
								}
								else if(task.mode.getSelectedIndex()+1==4) {
									
									
									Boolean[] pattern=new Boolean[31];
									for (int i=0;i<31;i++) {
										pattern[i]=false;
									}
									
									String[] input =task.setMonthDays.getText().split(",");	
										for(String day:input) {
											
											pattern[Integer.parseInt(day)-1]=true;
										}
									
									ev.editCyclic(Mode.monthly);
									ev.editCyclicMonth(pattern);
									
									
								}
								else if(task.mode.getSelectedIndex()+1==5) {
									ev.editCyclic(Mode.custom);
									ev.editCyclicXdays(Integer.parseInt(task.setGap5.getText()));
									
									
								}
								

							
							CRUD.add(TaskType.Event,DataBaseBridge.eventToArray(ev));
														
							isCorrectInput=true;
							}
							catch(Exception exc){					
								isCorrectInput=false;
								System.out.println("Fill out all fields");
							}
							
											
							
						}
						
						shouldReload=true;
						DayViewPanel.refreshTimeline();
					}
	            	
	            });
	            task.typeLabel.revalidate();
	            task.typeLabel.repaint();
	            task.typeInput.revalidate();
	            task.typeInput.repaint();
	        }
			
		});
	}
	public static void viewTask(TaskInstance ti) {
		caption.removeAll();
		caption.add(new JLabel("Edit:"));
		caption.revalidate();
		caption.repaint();
		task.l.removeAll();
		task.r.removeAll();	
		task.removeAll();
		
		if(ti.getType()==TaskType.Event){
			theme=GlobalVariables.Red;
			
			Event t =ti.event;
			
			task.l.add(Box.createRigidArea(new Dimension(0,10)));
			task.addField(task.l,"Name ",task.font, theme);
			task.addField(task.l,"Description ",task.font, theme);
			task.addField(task.l,"Added ",task.font, theme);
			task.addField(task.l,"Importance ",task.font, theme);
			task.addField(task.l,"Duration  ",task.font, theme);
			task.addField(task.l,"Mode ",task.font, theme);
			
		
			task.r.add(Box.createRigidArea(new Dimension(0,10)));
			task.title=task.addInput(task.r,t.getTitle(),task.font, theme);
			task.desc=task.addInput(task.r,t.getDesc(),task.font, theme);
			
			LocalDate date=t.getDateAdded();
			
				
			task.addField(task.r,date.toString(), task.font, theme );
						
			task.importance=new JSlider(JSlider.HORIZONTAL,1,5,t.getImportance());
			task.importance.setPaintTicks(true);
			task.importance.setPaintLabels(true);
			task.importance.setMaximumSize(new Dimension(300,30));
			task.importance.setMinimumSize(new Dimension(300,30));
			task.r.add(task.importance);
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
			JPanel line3 = new JPanel();
			line3.setBackground(theme);
			line3.setPreferredSize(new Dimension(200,1));
			line3.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
			line3.setMinimumSize(new Dimension(200,1));
			task.r.add(line3);
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
			
			LocalTime start = t.getStart();
			LocalTime end = t.getEnd();
			task.startHour = new JTextPane();
			task.startMinute = new JTextPane();
			task.endHour = new JTextPane();
			task.endMinute = new JTextPane();
			task.startHour.setPreferredSize(new Dimension(30,30));
			task.startMinute.setPreferredSize(new Dimension(30,30));
			task.endHour.setPreferredSize(new Dimension(30,30));
			task.endMinute.setPreferredSize(new Dimension(30,30));
			JPanel cont = new JPanel();
			cont.setMaximumSize(new Dimension(300,30));
			task.r.add(cont);
			cont.add(task.startHour);
			task.startHour.setText(Integer.toString(start.getHour()));
			cont.add(new JLabel(":"));
			cont.add(task.startMinute);
			task.startMinute.setText(Integer.toString(start.getMinute()));
			cont.add(new JLabel(" - "));
			cont.add(task.endHour);
			task.endHour.setText(Integer.toString(end.getHour()));
			cont.add(new JLabel(":"));
			cont.add(task.endMinute);
			task.endMinute.setText(Integer.toString(end.getMinute()));
			
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
			JPanel line = new JPanel();
			line.setBackground(theme);
			line.setPreferredSize(new Dimension(300,1));
			line.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
			task.r.add(line);
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
			String[] options = {"oneTime","daily","weekly","monthly","custom"};			
		    task.mode = new JComboBox<>(options);
		    task.mode.setSelectedItem(t.getMode().toString());
		    task.mode.setPreferredSize(new Dimension(250,20));
		    task.mode.setMaximumSize(new Dimension(250,20));
		    task.editModeLabel=new JPanel();
		    task.editModeInput=new JPanel();
		    task.editModeLabel.setLayout(new BoxLayout(task.editModeLabel, BoxLayout.Y_AXIS));
		    task.editModeInput.setLayout(new BoxLayout(task.editModeInput, BoxLayout.Y_AXIS));		    
		    task.l.add(task.editModeLabel);	
		    
		    if(t.getMode()==Mode.oneTime) {
		    	renderOneTimeMode(t);
		    }
		    else if(t.getMode()==Mode.daily) {
		    	
		    }
		    else if(t.getMode()==Mode.weekly) {
		    	renderWeekMode(t);
		    }
		    else if(t.getMode()==Mode.monthly) {
		    	renderMonthMode(t);
		    }
		    else if(t.getMode()==Mode.custom) {
		    	renderCustomMode(t);
		    }
		    
		    
		    
		    
		    task.mode.addActionListener(new ActionListener() {		    
				@Override
				public void actionPerformed(ActionEvent ev) {
					JComboBox<String> source = (JComboBox<String>) ev.getSource();
		            String selectedMode = (String) source.getSelectedItem();
		            task.editModeLabel.removeAll();
		            task.editModeInput.removeAll();
		            if(selectedMode=="weekly") {
		            	renderWeekMode(t);
		     		}
		     		else if (selectedMode=="monthly") {
		     			renderMonthMode(t);
		     			
		     		}
		     		else if (selectedMode=="custom") {
		     			renderCustomMode(t);
		     		}
		     		else if (selectedMode=="oneTime") {
		     			renderOneTimeMode(t);
		     		}
		            
					task.revalidate();
					task.repaint();
				}
		    	
		    });
		    task.r.add(task.mode);
		    task.r.add(Box.createRigidArea(new Dimension(0,15)));
			JPanel line2 = new JPanel();
			line2.setBackground(theme);
			line2.setPreferredSize(new Dimension(300,1));
			line2.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
			task.r.add(line2);
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
			task.r.add(task.editModeInput);
		}
		else if (ti.getType()==TaskType.SimpleTask) {
			//theme=colors[1];
			theme=GlobalVariables.Green;
			SimpleTask t =ti.task;
			task.l.add(Box.createRigidArea(new Dimension(0,10)));
			task.addField(task.l,"Name ",task.font, theme);
			task.addField(task.l,"Description ",task.font, theme);
			task.addField(task.l,"Added ",task.font, theme);
			task.addField(task.l,"Importance ",task.font, theme);
			task.addField(task.l,"Deadline ",task.font, theme);
			task.addField(task.l,"Prefered Time ",task.font, theme);
			task.addField(task.l,"Time to fulfil",task.font, theme);
			
			task.r.add(Box.createRigidArea(new Dimension(0,10)));
			task.title=task.addInput(task.r,t.getTitle(),task.font, theme);
			task.desc=task.addInput(task.r,t.getDesc(),task.font, theme);
			
			LocalDate date = t.getDateAdded();			
						
			task.addField(task.r,date.toString(), task.font, theme );
			
			task.importance=new JSlider(JSlider.HORIZONTAL,1,5,t.getImportance());
			task.importance.setPaintTicks(true);
			task.importance.setPaintLabels(true);
			task.importance.setMaximumSize(new Dimension(300,30));
			task.importance.setMinimumSize(new Dimension(300,30));
			task.r.add(task.importance);
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
			JPanel line3 = new JPanel();
			line3.setBackground(theme);
			line3.setPreferredSize(new Dimension(200,1));
			line3.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
			line3.setMinimumSize(new Dimension(200,1));
			task.r.add(line3);
			task.r.add(Box.createRigidArea(new Dimension(0,3)));
			
			LocalDateTime oldDeadline=t.getDeadline();
			LocalDateTime oldPrefered=t.getPreffered();	
			UtilDateModel modelD;
			UtilDateModel modelP;
			if(oldDeadline.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()==0) {
				modelD = new UtilDateModel();
			}
			else {
				modelD = new UtilDateModel(java.sql.Date.valueOf(oldDeadline.toLocalDate()));
			}
			if(oldPrefered.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli()==0) {
				modelP = new UtilDateModel();			
			}
			else {
				modelP = new UtilDateModel(java.sql.Date.valueOf(oldPrefered.toLocalDate()));			
			}
			
			
 			Properties p = new Properties();			
 			JDatePanelImpl deadline = new JDatePanelImpl(modelD, p);			
 			JDatePanelImpl prefered = new JDatePanelImpl(modelP, p);
 			task.deadline = new JDatePickerImpl(deadline,  new DateComponentFormatter());
 			task.prefered = new JDatePickerImpl(prefered,  new DateComponentFormatter()); 
 			
 			JPanel boxD = new JPanel();//flow [dateBoxD|timeBoxD]
 			JPanel boxP = new JPanel();//flow [timeBoxP|timeBoxP]
 			JPanel dateBoxD = new JPanel();//box [datepicker]
 			JPanel timeBoxD = new JPanel();//flow [hh:mm]
 			JPanel dateBoxP = new JPanel();//box [datepicker]
 			JPanel timeBoxP = new JPanel();//flow [hh:mm]
			
 			boxD.setMaximumSize(new Dimension(400,32));
 			boxP.setMaximumSize(new Dimension(400,32));
 			dateBoxD.setLayout(new BoxLayout(dateBoxD,BoxLayout.Y_AXIS));
 			dateBoxP.setLayout(new BoxLayout(dateBoxP,BoxLayout.Y_AXIS));	
 			
 			task.r.add(boxD);
 			
 			boxD.add(dateBoxD);
 			boxD.add(timeBoxD);
 			
 			dateBoxD.add(task.deadline);
 			
 			task.deadlineHour = new JTextPane();
 			task.deadlineMinute= new JTextPane();
 			task.deadlineHour.setPreferredSize(new Dimension(30,30));
 			task.deadlineMinute.setPreferredSize(new Dimension(30,30));
 			task.deadlineHour.setText(Integer.toString(oldDeadline.toLocalTime().getHour()));
 			task.deadlineMinute.setText(Integer.toString(oldDeadline.toLocalTime().getMinute()));
 			timeBoxD.add(task.deadlineHour);
 			timeBoxD.add(new JLabel(" : "));
 			timeBoxD.add(task.deadlineMinute);
 			
 			
 			task.r.add(Box.createRigidArea(new Dimension(0,5)));
 			JPanel line4 = new JPanel();
			line4.setBackground(theme);
			line4.setPreferredSize(new Dimension(200,1));
			line4.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
			line4.setMinimumSize(new Dimension(200,1));
			task.r.add(line4);
			task.r.add(Box.createRigidArea(new Dimension(0,3)));
 			
 			task.r.add(boxP); 			
 			boxP.add(dateBoxP);
 			boxP.add(timeBoxP);
 			dateBoxP.add(task.prefered);
 			
 			task.r.add(Box.createRigidArea(new Dimension(0,5)));
 			JPanel line5 = new JPanel();
			line5.setBackground(theme);
			line5.setPreferredSize(new Dimension(200,1));
			line5.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
			line5.setMinimumSize(new Dimension(200,1));
			task.r.add(line5);
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
 			
 			task.preferedHour = new JTextPane();
 			task.preferedMinute = new JTextPane(); 
 			task.preferedHour.setPreferredSize(new Dimension(30,30));
 			task.preferedMinute.setPreferredSize(new Dimension(30,30));
 			task.preferedHour.setText(Integer.toString(oldPrefered.toLocalTime().getHour()));
 			task.preferedMinute.setText(Integer.toString(oldPrefered.toLocalTime().getMinute()));
 			
 			timeBoxP.add(task.preferedHour);
 			timeBoxP.add(new JLabel(" : "));
 			timeBoxP.add(task.preferedMinute);
 			
 			JPanel timeBox = new JPanel();
			timeBox.setMaximumSize(new Dimension(300,30));
			task.hours = new JTextPane();
			task.minutes = new JTextPane();
			task.minutes.setPreferredSize(new Dimension(30,30));
			task.hours.setPreferredSize(new Dimension(30,30));
			Duration d=t.getDuration();
	    	task.hours.setText(Long.toString(d.toHours()));
	    	task.minutes.setText(Long.toString(d.toMinutes()-d.toHours()*60));
			timeBox.add(task.hours);
			timeBox.add(new JLabel(" : "));
			timeBox.add(task.minutes);	    			
			task.r.add(timeBox);
			
			task.r.add(Box.createRigidArea(new Dimension(0,5)));
			JPanel line6 = new JPanel();
			line6.setBackground(theme);
			line6.setPreferredSize(new Dimension(200,1));
			line6.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
			line6.setMinimumSize(new Dimension(200,1));
			task.r.add(line6);
			task.r.add(Box.createRigidArea(new Dimension(0,2)));
		}
		else if (ti.getType()==TaskType.Project) {
			
		}
		else {
			
		}
		buttons.removeAll();
		
		if (ti.getType()==TaskType.SimpleTask) {
			if(!ti.task.isDone()) {
				buttons.add(deleteButton);
				buttons.add(completeButton);
				buttons.add(confirmButton);
			}
			else {
				buttons.add(restoreButton);
			}
		}
		else {
			buttons.add(deleteButton);
			buttons.add(confirmButton);
		}
		
			
		
		deleteButton.setBackground(theme);
		completeButton.setBackground(theme);
		restoreButton.setBackground(theme);
		confirmButton.setBackground(theme);
		buttons.revalidate();
		buttons.repaint();
		if(deleteButton.getActionListeners().length>0) {
			deleteButton.removeActionListener(deleteButton.getActionListeners()[0]);
		}
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {					
				if(ti.getType()==TaskType.SimpleTask){
					String [] options= {"Remove from today's schedule","Delete completely"};
					int choice=JOptionPane.showOptionDialog(null,"","Delete", 
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
			                null,options,options[0]);
					
					try {
					if (choice == 0) {
						
						
						if(!day.containsPotentialTask(ti.task)) {
							CRUD.updateSingle(TaskType.SimpleTask,ti.task.getId(),"PreferredTime","0");
							day.addPotentialTask(ti.task);
							Sidebar.refreshPotentialTasks(day);
							Sidebar.removeAsignedTask(ti.task.getTitle());
						 	ti.setVisible(false);
						 	shouldReload=true;
						}						
						else {
							//cant delete, doesnt ecixst
						}
						
						
					 }
					else if (choice == 1) {
						 
						 	CRUD.delete(ti.getType(),ti.getId());
						 	Sidebar.removeAsignedTask(ti.task.getTitle());
						 	ti.setVisible(false);
						 	shouldReload=true;
						 }
					 
					}
					catch(Exception ex) {
						 System.out.println("Error: ");
						 ex.printStackTrace();
					} 
				}	
				else if(ti.getType()==TaskType.Event){
					int choice=JOptionPane.showConfirmDialog(null,"Delete forever?","Delete", 
							JOptionPane.YES_NO_OPTION );
					
					try {
						if (choice ==JOptionPane.YES_OPTION) {
							
							

						 	CRUD.delete(ti.getType(),ti.getId());
						 	
						 	ti.setVisible(false);
						 	shouldReload=true;
							
							
						 }
						
						 
						}
						catch(Exception ex) {
							 System.out.println("Error: ");
							 ex.printStackTrace();
						} 
				}	
					
						 								       
					     
				
								
			}
			
		});
		if(completeButton.getActionListeners().length>0) {
			completeButton.removeActionListener(completeButton.getActionListeners()[0]);
		}
		completeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {	
				//theme changes to grayer
				
				if(ti.getType()==TaskType.SimpleTask){
					
					
					
				
					String [] options= {"Great","Ok","Rushed","Bad"};
					int choice=JOptionPane.showOptionDialog(null,"Rate your efforts","Complete Task", 
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
		                null,options,options[0]);
				 
					if (choice == 0) {
						CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"isDone","1");
						CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"satisfaction","4");
						ti.setBackground(colors[5]);
						
						buttons.removeAll();
						buttons.add(restoreButton);
						buttons.revalidate();
						buttons.repaint();
						JOptionPane.showMessageDialog(null, "Congrats! Keep it up", "", JOptionPane.INFORMATION_MESSAGE);					 
				    } 
					else if (choice ==1) {
						CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"isDone","1");
					 	CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"satisfaction","3");
					 	ti.setBackground(colors[4]);
					 	buttons.removeAll();
						buttons.add(restoreButton);
						buttons.revalidate();
						buttons.repaint();
					 	JOptionPane.showMessageDialog(null, "Progress is what matters", "", JOptionPane.INFORMATION_MESSAGE);	
					}
					else if (choice ==2) {
						int choice2=JOptionPane.showConfirmDialog(null,"Do you wish to reapeat the task when you have more time?",
							 "", JOptionPane.YES_NO_OPTION);
						CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"satisfaction","2");
						if(choice2 ==JOptionPane.YES_OPTION) {
							///choose time						 
							ti.setBounds(0,0,0,0);
							CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"preferredTime","0");
						 	Sidebar.removeAsignedTask(ti.getTitle());
						 	day.addPotentialTask(ti.task);
						 
						 	Sidebar.refreshPotentialTasks(day);
						}
						else {
							CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"isDone","1");
							ti.setBackground(colors[4]);
							buttons.removeAll();
							buttons.add(restoreButton);
							buttons.revalidate();
							buttons.repaint();
							JOptionPane.showMessageDialog(null, "You can still come back to this later", "", JOptionPane.INFORMATION_MESSAGE);		
						}
					 	
					}
					else if (choice ==3) {
						int choice2=JOptionPane.showConfirmDialog(null,"Do you wish to reapeat the task when you have more time?",
							 "", JOptionPane.YES_NO_OPTION);
						CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"satisfaction","1");
						if(choice2 ==JOptionPane.YES_OPTION) {
							///choose time
							ti.setBounds(0,0,0,0);
							CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"preferredTime","0");
							Sidebar.removeAsignedTask(ti.getTitle());
							day.addPotentialTask(ti.task);
						 
							Sidebar.refreshPotentialTasks(day);
						}
						else {
							CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"isDone","1");
							ti.setBackground(colors[4]);
							buttons.removeAll();
							buttons.add(restoreButton);
							buttons.revalidate();
							buttons.repaint();
							JOptionPane.showMessageDialog(null, "You can still come back to this later", "", JOptionPane.INFORMATION_MESSAGE);		
						}	 
					}
					
					
				}
			}	
			
		});
		restoreButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				ti.setBackground(colors[1]);
				CRUD.updateSingle(TaskType.SimpleTask,ti.getId(),"isDone","0");
				buttons.removeAll();
				buttons.add(deleteButton);
				buttons.add(completeButton);
				buttons.add(confirmButton);
				buttons.revalidate();
				buttons.repaint();
			}});
		if(confirmButton.getActionListeners().length>0) {
			confirmButton.removeActionListener(confirmButton.getActionListeners()[0]);
		}
		confirmButton.addActionListener(invalidinputListener);
		confirmButton.addActionListener(new ActionListener() {
				
		@Override
		public void actionPerformed(ActionEvent e) {
			shouldReload=true;
				
			if(ti.getType()==TaskType.SimpleTask) {
				try {	
				ti.task.editTitle(task.title.getText());				
				ti.task.editDesc(task.desc.getText());				
				ti.task.editImportance(task.importance.getValue());
				
				
				DateModel tmp= task.deadline.getModel();				
				LocalDateTime tmpLDT= LocalDateTime.of(tmp.getYear(),tmp.getMonth()+1,tmp.getDay(),
						Integer.parseInt(task.deadlineHour.getText()),Integer.parseInt(task.deadlineMinute.getText()));
				
				ti.task.updateDeadline(tmpLDT);
				tmp= task.prefered.getModel();				
				tmpLDT= LocalDateTime.of(tmp.getYear(),tmp.getMonth()+1,tmp.getDay(),
						Integer.parseInt(task.preferedHour.getText()),Integer.parseInt(task.preferedMinute.getText()));
				
				ti.task.changePrefferedTime(tmpLDT);
				int h;
				int m;
				if(task.hours.getText()=="") {
					h=0;
				}
				else {
					h=Integer.parseInt(task.hours.getText());
				}
				if(task.minutes.getText()=="") {
					m=0;
				}
				else{
					m=Integer.parseInt(task.minutes.getText());
				}
				Duration d=Duration.ofSeconds(m*60+
						h*3600);
				
				ti.task.changeDuration(d);
				
				CRUD.updateWhole(TaskType.SimpleTask,ti.task.getId(),DataBaseBridge.simpleTaskToArray(ti.task));
				if(ti.task.getPreffered().toLocalDate().equals(day.getDate())) {
					ti.updateInstance();
				}
				else {
					ti.setBounds(0, 0,0,0);
				}
				
				isCorrectInput=true;
			}
			catch(Exception exc){					
				isCorrectInput=false;
				
			}
			}	
		
			else if(ti.getType()==TaskType.Event){
				try {
				ti.event.editTitle(task.title.getText());
				
				
				ti.event.editDesc(task.desc.getText());
				
				
				ti.event.editImportance(task.importance.getValue());
				
				LocalTime start= LocalTime.of(Integer.parseInt(task.startHour.getText()),
				Integer.parseInt(task.startMinute.getText()));
				LocalTime end= LocalTime.of(Integer.parseInt(task.endHour.getText()),
				Integer.parseInt(task.endMinute.getText()));
				ti.event.editDuration(start,end);	
				
				ti.event.editCyclic(Enum.valueOf(Mode.class, task.mode.getSelectedItem().toString()) );
				
				if (task.mode.getSelectedIndex()+1==1) {					
					int y=task.setGap1.getModel().getYear();
					int m=task.setGap1.getModel().getMonth()+1;
					int d =task.setGap1.getModel().getDay();
					LocalDate date= LocalDate.of(y,m,d);
					long diff = ChronoUnit.DAYS.between(ti.event.getDateAdded(), date);
					if(diff>=0) {
						ti.event.editCyclicXdays((int) diff);
						ti.event.editCyclicMonth(new Boolean[31]);
						ti.event.editCyclicWeek(new Boolean[7]);
					}
					 
				}
				else if(task.mode.getSelectedIndex()+1==2){
					ti.event.editCyclicXdays(0);
					ti.event.editCyclicMonth(new Boolean[31]);
					ti.event.editCyclicWeek(new Boolean[7]);
				}
				else if(task.mode.getSelectedIndex()+1==3){
					Boolean[] newData= new Boolean[7];
					if(task.mon.isSelected()==true) {
						newData[0]=true;
					}						
					else {
						newData[0]=false;
					}
					if(task.tue.isSelected()==true) {
						newData[1]=true;
					}
					else {
						newData[1]=false;
											}
					if(task.wed.isSelected()==true) {
						newData[2]=true;
					}
					else {
						newData[2]=false;					}
					if(task.thu.isSelected()==true) {
						newData[3]=true;
					}
					else {
						newData[3]=false;
					}
					if(task.fri.isSelected()==true) {
						newData[4]=true;
					}
					else {
						newData[4]=false;
					}
					if(task.sat.isSelected()==true) {
						newData[5]=true;
					}
					else {
						newData[5]=false;
					}
					if(task.sun.isSelected()==true) {
						newData[6]=true;
					}
					else {
						newData[6]=false;
					}
					ti.event.editCyclicXdays(0);
					ti.event.editCyclicMonth(new Boolean[31]);
					ti.event.editCyclicWeek(newData);
				}
				else if(task.mode.getSelectedIndex()+1==4) {
					
					
					Boolean[] pattern=new Boolean[31];
					for (int i=0;i<31;i++) {
						pattern[i]=false;
					}
					
					String[] input =task.setMonthDays.getText().split(",");	
						for(String day:input) {
							
							pattern[Integer.parseInt(day)-1]=true;
						}
					
					ti.event.editCyclicXdays(0);
					ti.event.editCyclicMonth(pattern);
					ti.event.editCyclicWeek(new Boolean[7]);
					
				}
				else if(task.mode.getSelectedIndex()+1==5) {
					ti.event.editCyclicXdays(Integer.parseInt(task.setGap5.getText()));
					ti.event.editCyclicMonth(new Boolean[31]);
					ti.event.editCyclicWeek(new Boolean[7]);
					
				}
				
				CRUD.updateWhole(TaskType.Event,ti.event.getId(),DataBaseBridge.eventToArray(ti.event));
				ti.updateInstance();			
				isCorrectInput=true;
				}
				catch(Exception exc){					
					isCorrectInput=false;
					System.out.println("Error:" );
					exc.printStackTrace();
				}
				
								
				
			}
		}
		});
		
		
		task.setBorder(BorderFactory.createLineBorder(theme, 2));
		task.add(task.l,BorderLayout.WEST);
		task.add(task.r,BorderLayout.EAST);
			
		task.revalidate();
		task.repaint();
		
		
	}
	
	
	public static void reload() {
		shouldReload=true;
	}
	public static void updateDay(Day d) {
		day=d;
	}
	public static boolean getReload() {
		return shouldReload;
	}
	public static void resetReload() {
		shouldReload=false;
	}
	public static void addListnertoConfirm(ActionListener ac) {
		invalidinputListener=ac;
	}
	private static void renderWeekMode(Event e) {

		task.addField(task.editModeLabel,"Select days ", task.font, theme );
			JPanel row=new JPanel();
			JPanel col1=new JPanel();
			col1.setLayout(new BoxLayout(col1,BoxLayout.Y_AXIS));
			JPanel col2=new JPanel();
			col2.setLayout(new BoxLayout(col2,BoxLayout.Y_AXIS));
			JPanel col3=new JPanel();
			col3.setLayout(new BoxLayout(col3,BoxLayout.Y_AXIS));
			JPanel col4=new JPanel();
			col4.setLayout(new BoxLayout(col4,BoxLayout.Y_AXIS));
			JPanel col5=new JPanel();
			col5.setLayout(new BoxLayout(col5,BoxLayout.Y_AXIS));
			JPanel col6=new JPanel();
			col6.setLayout(new BoxLayout(col6,BoxLayout.Y_AXIS));
			JPanel col7=new JPanel();
			col7.setLayout(new BoxLayout(col7,BoxLayout.Y_AXIS));
			row.setMaximumSize(new Dimension(200,40));
			task.editModeInput.add(row);
			JPanel line = new JPanel();
		line.setBackground(theme);
		line.setPreferredSize(new Dimension(300,1));
		line.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
		task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
		task.editModeInput.add(line);
		task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
			task.mon= new JCheckBox();
			task.tue= new JCheckBox();
			task.wed= new JCheckBox();
			task.thu= new JCheckBox();
			task.fri= new JCheckBox();		     		
			task.sat= new JCheckBox();
			task.sun= new JCheckBox();
			
			boolean[] pattern =e.getWeekPattern();
			task.mon.setSelected(pattern[0]);
			task.tue.setSelected(pattern[1]);
			task.wed.setSelected(pattern[2]);
			task.thu.setSelected(pattern[3]);
			task.fri.setSelected(pattern[4]);	     		
			task.sat.setSelected(pattern[5]);
			task.sun.setSelected(pattern[6]);

			
			
			row.add(col1);
			row.add(col2);
			row.add(col3);
			row.add(col4);
			row.add(col5);
			row.add(col6);
			row.add(col7);		     			
			col1.add(new JLabel("Mon"));
			col1.add(task.mon);
			col2.add(new JLabel("Tue"));
			col2.add(task.tue);
			col3.add(new JLabel("Wed"));
	        col3.add(task.wed);
	        col4.add(new JLabel("Thu"));
	        col4.add(task.thu);
	        col5.add(new JLabel("Fri"));
	        col5.add(task.fri);
	        col6.add(new JLabel("Sat"));
	        col6.add(task.sat);
	        col7.add(new JLabel("Sun"));
	        col7.add(task.sun);
	}

	private static void renderMonthMode(Event e) {
		task.addField(task.editModeLabel,"Select days ", task.font, theme );
			task.setMonthDays = new JTextPane();
			String display ="";
			boolean[] b =e.getMonthPattern();
			for(int i=0;i<b.length;i++) {
				if(b[i]==true)display=display+(i+1)+",";
			}
			
			if(display.length()>1) {
				task.setMonthDays.setText(display.substring(0,display.length()-1));
			}
			task.setMonthDays.setMaximumSize(new Dimension(300,25));
			task.editModeInput.add(task.setMonthDays);
			task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
	}
	private static void renderCustomMode(Event e) {
			task.addField(task.editModeLabel,"Days to skip ", task.font, theme );
			task.setGap5 = new JTextPane();
			task.setGap5.setMaximumSize(new Dimension(200,25));
			task.setGap5.setText(Integer.toString(e.getCustomPattern()));
			task.editModeInput.add(task.setGap5);
			task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
	}
	private static void renderOneTimeMode(Event e) {
		task.addField(task.editModeLabel,"When", task.font, theme );
			LocalDate oldDate=e.getDateAdded().plusDays(e.getCustomPattern());		     			
			UtilDateModel model = new UtilDateModel(java.sql.Date.valueOf(oldDate));
			Properties p = new Properties();
			JDatePanelImpl datePanel = new JDatePanelImpl(model, p);		     			
			task.setGap1 = new JDatePickerImpl(datePanel,  new DateComponentFormatter());
			
			task.editModeInput.add(task.setGap1);
			task.editModeInput.add(Box.createRigidArea(new Dimension(0,5)));
	}
}
