package Interface.DataViewers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import tasksPackage.GlobalVariables;

public class Time extends JPanel {
	
	//draws timeline
	public Time (){
		setOpaque(false);
		setPreferredSize(new Dimension(130,0));
		
	}
	
		
	@Override
	 protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int margin= GlobalVariables.margin;  
		int hh= GlobalVariables.hourSpace;
        
        g.setColor(Color.gray);        
        g.drawLine(getWidth()/2,margin,getWidth()/2,getHeight()-margin);
        
        for (int i=0; i<=24; i++) {
        	
        	if (i<10) {
        		drawHour(g,"0"+i+":00", i, hh, margin); 
        		drawMinutes(g, i, hh, margin);
        	}
        	else if (i<24){
        		drawHour(g,i+":00", i, hh, margin); 
        		drawMinutes(g, i, hh, margin);
        	}
        	else {
        		drawHour(g,"00:00", i, hh, margin); 
        	}
        	
        	
        }
        
    }
	 private void drawHour(Graphics g, String label, int i, int hh, int margin) {
		g.drawString(label, margin,2*margin+i*hh);
 		g.setColor(Color.black);   
 		g.drawLine(getWidth()/2-margin,2*margin-5+i*hh,getWidth(),2*margin-5+i*hh);
 		g.setColor(Color.gray);   
     }  
	 
	 private void drawMinutes(Graphics g, int i, int hh, int margin) {
		g.drawString(":15",getWidth()/2+5,2*margin+i*hh+hh/4);
     	g.drawLine(getWidth()/2+30,2*margin+i*hh+hh/4-5,getWidth(),2*margin+i*hh+hh/4-5);
     	g.drawString(":30",getWidth()/2+5,2*margin+i*hh+hh/2);
     	g.drawLine(getWidth()/2+30,2*margin+i*hh+hh/2-5,getWidth(),2*margin+i*hh+hh/2-5);
     	g.drawString(":45",getWidth()/2+5,2*margin+i*hh+hh/4*3);
     	g.drawLine(getWidth()/2+30,2*margin+i*hh+hh/4*3-5,getWidth(),2*margin+i*hh+hh/4*3-5);
	 }
}