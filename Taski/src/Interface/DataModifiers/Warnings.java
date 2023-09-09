package Interface.DataModifiers;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Warnings extends JPanel{
	
	private JLabel overlap;
	private JLabel badInput;
	boolean isoverlap=false;
	boolean isBadInput=false;
	public Warnings() {
		overlap= new JLabel("Warning: Some events or Tasks overlap eachother");
		badInput= new JLabel("Warning: Invalid input");
		setPreferredSize(new Dimension(600, 100));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));	
		removeAll();
	}
	
	public void overlapWarning() {
		
		if (!isoverlap) {
			
			//overlap= new JLabel("Warning: Some events or Tasks overlap eachother");
			add(overlap);
			isoverlap=true;
			revalidate(); 
		    repaint();
		}		
		
	}
	public void noOverlapWarning() {
		isoverlap=false;
		//remove(overlap);
		removeAll();
		revalidate(); 
	    repaint();
	}
	public void noBadInputlapWarning() {
			
	    isBadInput=false;
		remove(badInput);
		add(new JLabel("Task added"));
		revalidate(); 
	    repaint();	 
		
	}
	public void badInputlapWarning() {
		
		if (!isBadInput) {
			
			//badInput= new JLabel("Warning: Invalid input");
			add(badInput);
			isBadInput=true;
			revalidate(); 
		    repaint();	 
			
		}
		
		
	}
	
	
	
}
