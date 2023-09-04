package Interface.DataModifiers;


import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;

public class OptsTopButton extends JButton{

	OptsTopButton(String text){
		setAlignmentY(Component.CENTER_ALIGNMENT);
		setMaximumSize(new Dimension(2000,70));
		setText(text);
	}
}
