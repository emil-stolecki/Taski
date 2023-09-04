package Interface.DataViewers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Interface.MainWindow;
import tasksPackage.StoragesFolder.Day;
import tasksPackage.TaskFolder.Task;

public class DayDisplay extends JPanel {
	//day instance for the week view
	private JLabel name;
	private JLabel date;
	public DayDisplay(Day day) {
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));		
		setBackground(Color.pink);
		
		JPanel nameHolder =  new JPanel();
		JPanel dateHolder =  new JPanel();
		nameHolder.setMaximumSize(new Dimension(Integer.MAX_VALUE,50));
		
		name= new JLabel();
		nameHolder.add(name);		
		add(nameHolder);
		date=new JLabel(day.getDate().toString());
		dateHolder.add(date);
		add(dateHolder);
		
		JPanel taskHolder = new JPanel();
		taskHolder.setLayout(new BoxLayout(taskHolder,BoxLayout.Y_AXIS));
		
		taskHolder.setPreferredSize(new Dimension(200,900));
		taskHolder.setMinimumSize(new Dimension(100,500));
				
		taskHolder.setBackground(Color.yellow);
		add(taskHolder);
		
		for(int i=0;i<day.getTasksCount();i++) {
			JPanel task = new JPanel();
			task.add(new JLabel(day.getTask(i).getTitle()));			
			task.setMaximumSize(new Dimension(150,30));
			task.setBackground(Color.yellow);
			
			taskHolder.add(task);
		}
		
		taskHolder.addMouseListener(new MouseAdapter() {


			@Override
			public void mouseClicked(MouseEvent e) {
				MainWindow.dayView(day);
				
			}

			
			
			
		});
	}
	public void setName(String title) {
		this.name.setText(title);
	}
}
