package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Interface.DataModifiers.Workspace;
import Interface.DataViewers.DayViewPanel;
import Interface.DataViewers.TimeLine;
import tasksPackage.GlobalVariables;
import tasksPackage.EnumFolder.TaskType;
import tasksPackage.TaskFolder.Event;
import tasksPackage.TaskFolder.SimpleTask;
import tasksPackage.TaskFolder.Task;

public class TaskInstance extends JPanel {
	
	
	//represents a simple task or event on the timeline 
	//holds information about the type of the task and id for accesability
	  
    private Color[] colors = {GlobalVariables.fillRed,
    		GlobalVariables.fillGreen,
    		GlobalVariables.fillBlue,
    		GlobalVariables.fillPurple,
    		GlobalVariables.completedFillRed};
    
    private JLabel title;
    
    private final TaskType type;
    private final int id;
    public SimpleTask task;
    public Event event;
      
	public TaskInstance(Task t, int xPos,int yPos, int width, int height) {
	    
	   
		type=t.getType();
		id=t.getId(); 
		Border border = BorderFactory.createEtchedBorder();
        setBorder(BorderFactory.createTitledBorder(border));
        setLayout(new BorderLayout());
		title=new JLabel(t.getTitle());
		
		
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(title);
		
		
		
		setOpaque(false);
		if(type==TaskType.Event){
			this.event=(Event) t;
			this.setBackground(colors[0]);
		}
		else if (type==TaskType.SimpleTask) {
			this.task=(SimpleTask) t;
			if(!t.isDone()) {
				this.setBackground(colors[1]);
				
			}
			else {
				this.setBackground(colors[4]);
			}
			
		}
		else if (type==TaskType.Project) {
			this.setBackground(colors[2]);
		}
		else {
			this.setBackground(colors[3]);
		}
		
		setBounds(xPos ,yPos, width, height);
		
		addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	int offset = DayViewPanel.getSrollPane().getViewport().getViewPosition().y; 
            	
            	if(type==TaskType.Event){
            		TimeLine.addDesc(tHIS().event.preview(), xPos, yPos, width, height, offset, tHIS());  
        		}
        		else if (type==TaskType.SimpleTask) {
        			TimeLine.addDesc(tHIS().task.preview(), xPos, yPos, width, height, offset, tHIS());  
        		}
        		else if (type==TaskType.Project) {
        			
        		}
        		else {
        			
        		}
            	
            	
           	
            	
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	TimeLine.removeDesc(tHIS());
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            	
            	Workspace.viewTask(tHIS());
            	
            }
        });
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }
	
	private TaskInstance tHIS() {
		return this;
	}
	
	
	public void updateInstance() {
		Rectangle r=getBounds(getBounds());
				
		int margin= GlobalVariables.margin;
		int hourSpace=GlobalVariables.hourSpace;
		if(r.x==0) r.x=margin;
		if(r.width==0) r.width=250;		
		if(type==TaskType.Event){
    		this.title.setText(event.getTitle());   		
    		int offsetTop=event.getStart().getHour()*hourSpace+event.getStart().getMinute()*hourSpace/60;
        	int offsetBottom=event.getEnd().getHour()*hourSpace+event.getEnd().getMinute()*hourSpace/60;
    		setBounds(r.x ,margin+offsetTop+5, r.width, offsetBottom-offsetTop);
		}
		else if (type==TaskType.SimpleTask) {			
			this.title.setText(task.getTitle());
			int offsetTop=task.getPreffered().getHour()*hourSpace+task.getPreffered().getMinute()*hourSpace/60; 
			int duration=(int)task.getDuration().toMinutes()*hourSpace/60;
			if(duration==0)duration=30;//default
    		setBounds(r.x ,margin+offsetTop+5, r.width, duration);
			
		}
		else if (type==TaskType.Project) {
			
		}
		else {
			
		}
		
		this.revalidate();
		this.repaint();
	}
	
	public String getTitle() {
		return this.title.getText();
	}
	public int getId() {
		return this.id;
	}
	public TaskType getType() {
		return this.type;
	}
	public void setTitle(String text) {
		this.title.setText(text);
	}
	
	
}
