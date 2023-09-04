package Interface.DataViewers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Interface.MainWindow;
import tasksPackage.StoragesFolder.Day;

public class Month extends JPanel{

	public Month(Day[] month) {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JPanel top =new JPanel();
		JPanel content = new JPanel();
		JPanel buttons = new JPanel();
		
		add(top);
		add(content);
		add(buttons);
		
		JLabel name = new JLabel(month[0].getDate().getMonth().toString()+" "+month[0].getDate().getYear());
		top.add(name);
		
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		int numberofDays =month[0].getDate().lengthOfMonth();
		
		for(int i=0;i<numberofDays;i++) {
			Day currentday=month[i];
			if(i==0  || currentday.getDate().getDayOfWeek().getValue()==1) {
				
				JPanel week = new JPanel();				
				week.setLayout(new BoxLayout(week,BoxLayout.X_AXIS));
				
				week.add(Box.createVerticalStrut(10));
				for(int j=0;j<7;j++) {
					
					JPanel day = new JPanel();
					day.setPreferredSize(new Dimension(80,80));
					day.setBackground(Color.pink);
					day.setLayout(new BoxLayout(day,BoxLayout.Y_AXIS));
					JPanel dTop = new JPanel();
					day.add(dTop);
					dTop.add(new JLabel((DayOfWeek.of(j+1).toString())));
					dTop.setBackground(Color.pink);
					week.add(day);
					week.add(Box.createVerticalStrut(10));
				}
				
				content.add(week);	
				content.add(Box.createHorizontalStrut(10));
			}
			Container c=(Container) content.getComponent(content.getComponentCount()-2);

			c.setBackground(Color.black);
			int dayNumber= month[i].getDate().getDayOfWeek().getValue();
			JPanel d=(JPanel) c.getComponent(dayNumber*2-1);
			JPanel dTop = (JPanel) d.getComponent(d.getComponentCount()-1);
			
			dTop.add(new JLabel(currentday.getDate().getDayOfMonth()+""));
			dTop.setBackground(Color.red);			
			JPanel dBottom =new JPanel();
			d.add(dBottom);
			dBottom.setLayout(new BoxLayout(dBottom,BoxLayout.Y_AXIS));
			dBottom.setBackground(Color.red);
			d.setBackground(Color.red);
			
			dBottom.setPreferredSize(new Dimension(80,50));
			dBottom.add(new JLabel("Events: "+currentday.eventCount()));
			dBottom.add(new JLabel("Tasks: "+currentday.taskCount()));
			
			
			d.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					MainWindow.dayView(currentday);
					
				}
			});
		}
		
		buttons.setLayout(new BorderLayout());
		buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));		
		JButton prev = new JButton("Previous month");
		JButton next= new JButton("Next month");
		JButton back= new JButton("Back");
		
		
		prev.setPreferredSize(new Dimension(200,40));
		next.setPreferredSize(new Dimension(200,40));
		back.setPreferredSize(new Dimension(200,40));
		
		prev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.changeMonth(month[0], 0);
				
			}
			
		});
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.changeMonth(month[month.length-1], 1);
				
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
