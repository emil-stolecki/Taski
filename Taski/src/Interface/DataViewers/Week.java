package Interface.DataViewers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Interface.MainWindow;
import tasksPackage.StoragesFolder.Day;

public class Week extends JPanel{
	
	public Week(Day[]week) {
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JPanel top =new JPanel();
		JPanel content = new JPanel();
		JPanel buttons = new JPanel();
		
		add(top);
		add(content);
		add(buttons);
		
		
		JLabel dates = new JLabel(week[0].getDate()+" - "+week[6].getDate());
		top.add(dates);
		top.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
		content.setLayout(new BoxLayout(content,BoxLayout.X_AXIS));
		
		for(int i=0;i<7;i++) {
			DayDisplay day = new DayDisplay(week[i]);
			day.setName(DayOfWeek.of(i+1).toString());	
				
			content.add(day);
		}
		
		buttons.setLayout(new BorderLayout());		
		buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
		JButton prev = new JButton("Previous week");
		JButton next= new JButton("Next week");
		JButton back= new JButton("Back");
		
		
		prev.setPreferredSize(new Dimension(200,40));
		next.setPreferredSize(new Dimension(200,40));
		back.setPreferredSize(new Dimension(200,40));
		
		prev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.changeWeek(week[0], 0);
				
			}
			
		});
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.changeWeek(week[6], 1);
				
			}
			
		});
		
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.backtoDayView();
				
			}
			
		});
		
		buttons.add(prev,BorderLayout.WEST);
		buttons.add(next,BorderLayout.EAST);
		buttons.add(back,BorderLayout.CENTER);
	}
}
