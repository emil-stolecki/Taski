package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import DatabaseTools.CRUD;
import tasksPackage.EnumFolder.TaskType;



public class AllTasks extends JPanel{
	static Color fill1 = new Color(255, 0, 0);
	static Color fill2 = new Color(0, 155, 0);
	static Color fill3 = new Color(0, 0, 255);
	static Color fill4 = new Color(255, 0, 255);
      
	static Color[] colors = {fill1,fill2,fill3,fill4};   
	static Color theme;

	
	TaskType active;
	TableModel taskModel;
	TableModel eventModel;
	JTable tasksTable;
	JTable eventTable;
	int taskOffset;
	int eventOffset;
	ArrayList<int[]> modifiedTasks = new ArrayList<>();
	ArrayList<int[]> modifiedEvents = new ArrayList<>();
	public AllTasks() {
		
		setLayout(new BorderLayout());
		active=null;
		taskOffset=0;
		eventOffset=0;
		
		JPanel top =new JPanel();
		JPanel content = new JPanel();
		JPanel buttons = new JPanel();
		
		add(top,BorderLayout.NORTH);
		add(content,BorderLayout.CENTER);
		add(buttons,BorderLayout.SOUTH);
		
		top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		top.setAlignmentX(LEFT_ALIGNMENT);
		content.setLayout(new BorderLayout());
		
		JButton simpleTasks = new JButton("Simple Tasks");
		JButton events= new JButton("Event");
		
		simpleTasks.setPreferredSize(new Dimension(200,40));
		events.setPreferredSize(new Dimension(200,40));
		simpleTasks.setBackground(fill2);
		events.setBackground(fill1);		
		Object[][] simpleTaskList=CRUD.loadSimpleTasks(30, 0);
		Object[][] eventList =CRUD.loadEvents(30, 0);
	

	    // Column headers
	     String[] columnNamesEvents = {"Title", "Description", "Added","Importance",
	        	"Start Time","End Time","Mode","Closest","Id"};
	        
	     String[] columnNamesSimpleTasks = {"Title", "Description", "Added","Importance",
	    		 "Completed","Satisfaction","Deadline","Preferred Time","Duration","Id"};
	     
	     taskModel=new DefaultTableModel(simpleTaskList, columnNamesSimpleTasks){
             @Override
             public boolean isCellEditable(int row, int column) {
            	 
            	 if(column==0 ||column==1) {           		 
            		 return true;
            	 }
            	 
            	 else {
            		 
            		 int gototask=JOptionPane.showConfirmDialog(null,"Edit this task in the calendar?",
							 "", JOptionPane.YES_NO_OPTION);
					 if(gototask ==JOptionPane.YES_OPTION) {
						 LocalDateTime date=LocalDateTime.now();						
						if(taskModel.getValueAt(row, 7)!="-") {//preferred Time							
							date=(LocalDateTime) taskModel.getValueAt(row, 7);											
						}
						
						else if (taskModel.getValueAt(row, 6)!="-") {//deadline	
							date=(LocalDateTime) taskModel.getValueAt(row, 6);							
						}				
						MainWindow.findDay(date.toLocalDate(),Integer.parseInt((String)taskModel.getValueAt(row, 9)),TaskType.SimpleTask);
					 }
            	
            		 return false;
            	 }
             }
         };
         
         
         
         
         taskModel.addTableModelListener(new TableModelListener() {

             @Override
             public void tableChanged(TableModelEvent e) {
                 if (e.getType() == TableModelEvent.UPDATE) {
                     int row = e.getFirstRow();
                     int col = e.getColumn();
                     int[] cell = {row, col};
                     modifiedTasks.add(cell);
                     
                     
                 }
             }

             
         });
        	 
         
                 
	     tasksTable = new JTable(taskModel); 
	     
	     eventModel= new DefaultTableModel(eventList,columnNamesEvents){
	    	 @Override
             public boolean isCellEditable(int row, int column) {
            	 
            	 if(column==0 ||column==1) {           		 
            		 return true;
            	 }
            	 
            	 else {
            		 
            		 int gototask=JOptionPane.showConfirmDialog(null,"Edit this event in the calendar?",
							 "", JOptionPane.YES_NO_OPTION);
					 if(gototask ==JOptionPane.YES_OPTION) {
						 
						String[] d=eventModel.getValueAt(row, 7).toString().split("-");
						LocalDate date=LocalDate.of(Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2]));												
						MainWindow.findDay(date,Integer.parseInt((String)eventModel.getValueAt(row, 8)),TaskType.Event);
						
					 }
            	
            		 return false;
            	 }
             }
         };
         eventModel.addTableModelListener(new TableModelListener() {

             @Override
             public void tableChanged(TableModelEvent e) {
                 if (e.getType() == TableModelEvent.UPDATE) {
                     int row = e.getFirstRow();
                     int col = e.getColumn();
                     int[] cell = {row, col};
                     modifiedEvents.add(cell);                     
                     
                 }
             }

             
         });
        	 
         
	     eventTable = new JTable(eventModel); 
	     
	     JScrollPane scrollTasks = new JScrollPane(tasksTable);	
	     scrollTasks.setPreferredSize(new Dimension(980, 1000));
	     JScrollPane scrollEvents = new JScrollPane(eventTable);	
	     scrollEvents.setPreferredSize(new Dimension(980, 1000));
		
		
	     TableRowSorter<TableModel> taskSorter = new TableRowSorter<TableModel>(taskModel);
	     TableRowSorter<TableModel> eventSorter = new TableRowSorter<TableModel>(eventModel);
	     
	     tasksTable.setRowSorter(taskSorter);
	     eventTable.setRowSorter(eventSorter);
	     
	     JTextArea text = new JTextArea("Max number of tasks viewed at once");
	     text.setLineWrap(true);
	     text.setEditable(false);
	     text.setPreferredSize(new Dimension(200,30));
	     text.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
	     JSlider viewLimit = new JSlider(10,50);
	     viewLimit.setMinimumSize(new Dimension(200,30));
	     
	     JLabel number = new JLabel(Integer.toString(viewLimit.getValue()));
	     viewLimit.addChangeListener( new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				number.setText(Integer.toString(viewLimit.getValue()));
				
			}
	    	 
	     });
	     JButton confirm = new JButton("Confirm");
	     confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(active==TaskType.SimpleTask) {
					Object[][] simpleTaskList2 =CRUD.loadSimpleTasks(viewLimit.getValue(), taskOffset);
					DefaultTableModel model=(DefaultTableModel)tasksTable.getModel();					
					model.setDataVector(simpleTaskList2, columnNamesSimpleTasks);
				    ((AbstractTableModel) tasksTable.getModel()).fireTableDataChanged();
				    				    
				   
				}
				else if(active==TaskType.Event) {
					Object[][] eventList2 =CRUD.loadEvents(viewLimit.getValue(), eventOffset);
					DefaultTableModel model=(DefaultTableModel)eventTable.getModel();					
					model.setDataVector(eventList2, columnNamesEvents);
					((AbstractTableModel) eventTable.getModel()).fireTableDataChanged();
				}
			}
	    	 
	     });
	     JPanel confirmpanel = new JPanel();
	     confirmpanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
	     confirmpanel.add(confirm);
	     JPanel oldNewButtons = new JPanel();
	     oldNewButtons.setLayout(new BoxLayout(oldNewButtons,BoxLayout.X_AXIS));
	     JButton loadOlder=new JButton("Load older");
	     JButton loadNewer= new JButton("Load newer");
	     loadOlder.addActionListener(new ActionListener() {
	    	 	
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(active==TaskType.SimpleTask) {
						
										    				    
						Object[][] simpleTaskList2 =CRUD.loadSimpleTasks(viewLimit.getValue(),
								taskOffset+viewLimit.getValue());
						
							if(simpleTaskList2.length>0) {
								DefaultTableModel model=(DefaultTableModel)tasksTable.getModel();					
								model.setDataVector(simpleTaskList2, columnNamesSimpleTasks);
								((AbstractTableModel) tasksTable.getModel()).fireTableDataChanged();
								
								taskOffset=taskOffset+viewLimit.getValue();	
								
							}
							
							
					}
					else if(active==TaskType.Event) {
												
						Object[][] eventList2 =CRUD.loadEvents(viewLimit.getValue(), 
								eventOffset+viewLimit.getValue());
						if(eventList2.length>0) {
							DefaultTableModel model=(DefaultTableModel)eventTable.getModel();					
							model.setDataVector(eventList2, columnNamesEvents);
							((AbstractTableModel) eventTable.getModel()).fireTableDataChanged();
							
							eventOffset=eventOffset+viewLimit.getValue();
						}					
					}										
				}						    	 
		     });
	    
	     loadNewer.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(active==TaskType.SimpleTask ) {						
						if(taskOffset-viewLimit.getValue()<=0) {
							taskOffset=0;
						}
						else taskOffset=taskOffset-viewLimit.getValue();	
										    				    
						Object[][] simpleTaskList2 =CRUD.loadSimpleTasks(viewLimit.getValue(), taskOffset);
						DefaultTableModel model=(DefaultTableModel)tasksTable.getModel();					
						model.setDataVector(simpleTaskList2, columnNamesSimpleTasks);
						((AbstractTableModel) tasksTable.getModel()).fireTableDataChanged();
					    
						
					}
					else if(active==TaskType.Event) {
						if(eventOffset-viewLimit.getValue()<=0) {
							eventOffset=0;
						}
						else eventOffset=eventOffset-viewLimit.getValue();	
											
						Object[][] eventList2 =CRUD.loadEvents(viewLimit.getValue(), eventOffset);
						DefaultTableModel model=(DefaultTableModel)eventTable.getModel();					
						model.setDataVector(eventList2, columnNamesEvents);
						((AbstractTableModel) eventTable.getModel()).fireTableDataChanged();
						
					}
					
				}
		    	 
		     });
	     
	     loadOlder.setPreferredSize(new Dimension(90,30));
	     loadNewer.setPreferredSize(new Dimension(90,30));
	    
	     JButton save = new JButton("Save changes");
	     save.setPreferredSize(new Dimension(120,30));
	     
	     save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for(int[] field:modifiedTasks) {
				
				String value = (String) taskModel.getValueAt(field[0],field[1]);
				int id = Integer.parseInt((String) taskModel.getValueAt(field[0],9));
				
                CRUD.updateSingle(TaskType.SimpleTask,id, columnNamesSimpleTasks[field[1]],value);
				
                }
				
				for(int[] field:modifiedEvents) {
					String value = (String) taskModel.getValueAt(field[0],field[1]);
					int id = Integer.parseInt((String) taskModel.getValueAt(field[0],9));
					
	               CRUD.updateSingle(TaskType.Event,id, columnNamesSimpleTasks[field[1]],value);
				}
			}
	    	 
	     });
	     JPanel savepanel = new JPanel();
	     savepanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
	     savepanel.add(save);
	     JPanel options = new JPanel();	     
	     options.setLayout(new BoxLayout(options,BoxLayout.Y_AXIS));
	     options.add(text);
	     options.add(viewLimit);
	     options.add(number);
	     options.add(confirmpanel);
	     options.add(Box.createRigidArea(new Dimension(0,20)));
	     options.add(oldNewButtons);
	     oldNewButtons.add(loadNewer);	
	     oldNewButtons.add(loadOlder);	 
	     options.add(Box.createRigidArea(new Dimension(0,20)));
	     options.add(savepanel);
	     
	     simpleTasks.addActionListener(new ActionListener() {

		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				active=TaskType.SimpleTask;
				content.setBackground(fill2);
				
			        
					
				tasksTable.setGridColor(fill2);
				tasksTable.getTableHeader().setBackground(fill2);
				
				
				
				
				content.removeAll();
				content.add(scrollTasks,BorderLayout.WEST);
				content.add(options,BorderLayout.CENTER);
				content.revalidate();
				content.repaint();
			}
			
		});
		events.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				active=TaskType.Event;
				content.setBackground(fill1);
				
				
				eventTable.setGridColor(fill1);
				eventTable.getTableHeader().setBackground(fill1);
				
				
				content.removeAll();
				content.add(scrollEvents,BorderLayout.WEST);	
				content.add(options,BorderLayout.CENTER);
				content.revalidate();
				content.repaint();
				
			}
			
		});
		top.add(simpleTasks);
		
		
		top.add(events);
		JButton prev = new JButton("Previous month");
		JButton next= new JButton("Next month");
		JButton back= new JButton("Back");
				
		prev.setPreferredSize(new Dimension(200,40));
		next.setPreferredSize(new Dimension(200,40));
		back.setPreferredSize(new Dimension(200,40));
		
		prev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//MainWindow.changeMonth(month[0], 0);
				
			}
			
		});
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//.changeMonth(month[month.length-1], 1);
				
			}
			
		});
		
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.backtoDayView();
				
			}
			
		});
		
		
		buttons.add(back,BorderLayout.CENTER);
	}
}
