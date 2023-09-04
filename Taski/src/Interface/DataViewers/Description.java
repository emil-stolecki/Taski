package Interface.DataViewers;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Description extends JPanel {

	public Description(String[] text) {
		Border border = BorderFactory.createEtchedBorder();
        setBorder(BorderFactory.createTitledBorder(border));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBackground(new Color(247,236,183));
	
	
		for(String s:text) {
			add(new JLabel(s));
		}
		
	     setVisible(true);
	}
	
}
