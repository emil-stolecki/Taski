package Interface.DataModifiers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import org.jdatepicker.impl.JDatePickerImpl;


public class TaskEditor extends JPanel{
	JPanel l;
	JPanel r;
	Font font;
	
	JTextPane title;
	JTextPane desc;
	JSlider importance;
	JTextPane startHour;
	JTextPane startMinute;
	JTextPane endHour;
	JTextPane endMinute;
	JComboBox mode;
	JPanel editModeLabel;
	JPanel editModeInput;
	JCheckBox mon;
	JCheckBox tue;
	JCheckBox wed;
	JCheckBox thu;
	JCheckBox fri;
	JCheckBox sat;
	JCheckBox sun;
	JTextPane setMonthDays;
	JTextPane setGap5;
	JDatePickerImpl setGap1;
	JDatePickerImpl deadline;
	JDatePickerImpl prefered;
	JTextPane deadlineHour;
	JTextPane deadlineMinute;
	JTextPane preferedHour;
	JTextPane preferedMinute;
	JComboBox type;
	JPanel typeLabel;
	JPanel typeInput;
	JTextPane hours;
	JTextPane minutes;
	JCheckBox useDeadline;
	JCheckBox usePrefered;
	JCheckBox useDuration;
	public TaskEditor() {
			
				
		setLayout(new BorderLayout());
		
		Border border = BorderFactory.createLineBorder(Color.gray, 2);
        setBorder(BorderFactory.createTitledBorder(border));
        setMinimumSize(new Dimension(500,500));
        setPreferredSize(new Dimension(500,500));
		font =new Font(Font.SERIF, Font.PLAIN, 20);
		
		l = new JPanel();
		r = new JPanel();
				
		l.setLayout(new BoxLayout(l, BoxLayout.Y_AXIS));
			
		l.add(Box.createRigidArea(new Dimension(0,10)));
		
		r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS));

		r.add(Box.createRigidArea(new Dimension(0,10)));
		
		
		add(l,BorderLayout.WEST);
		add(r, BorderLayout.EAST);
		
	}
	
	public void addField(JPanel l, String name, Font font, Color theme) {
		JLabel label = new JLabel(name);
		label.setFont(font);
		JPanel p =new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(label);
		p.setMaximumSize(new Dimension(200,30));
		p.setPreferredSize(new Dimension(200,30));
		l.add(p);
		l.add(Box.createRigidArea(new Dimension(0,5)));
		JPanel line = new JPanel();
		line.setBackground(theme);
		line.setPreferredSize(new Dimension(200,1));
		line.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
		line.setMinimumSize(new Dimension(200,1));
		l.add(line);
		l.add(Box.createRigidArea(new Dimension(0,5)));
		
	}	
	public JTextPane addInput(JPanel r, String name, Font font, Color theme) {
		JTextPane reference = new JTextPane();
		reference.setMaximumSize(new Dimension(300,30));
		reference.setPreferredSize(new Dimension(300,30));
        reference.setFont(new Font(font.getFamily(),font.getStyle(),font.getSize()-4));
        reference.setText(name);
        r.add(reference);
		r.add(Box.createRigidArea(new Dimension(0,5)));
		JPanel line = new JPanel();
		line.setBackground(theme);
		line.setPreferredSize(new Dimension(300,1));
		line.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
		r.add(line);
		r.add(Box.createRigidArea(new Dimension(0,5)));
		return reference;
	}	
	
	
}
