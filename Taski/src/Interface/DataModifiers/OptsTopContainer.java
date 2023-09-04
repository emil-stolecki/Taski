package Interface.DataModifiers;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;


public class OptsTopContainer extends JPanel{

	OptsTopContainer(){
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));		
		setPreferredSize(new Dimension(600, 70));
		setBackground(Color.MAGENTA);
		
	}
	
	public void addButtons( Color c, OptsTopButton...buttons) {
		for (OptsTopButton button : buttons) {
			button.setBackground(c);
			add(button);
		}
	}
}
