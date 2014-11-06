import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;



public class PFieldPanel extends JPanel{

	public static int WIDTH = 220, HEIGHT = 150;
	
	private ArrayList<PObject> list;
	private ArrayList<Robot> bots;
	
	private Timer timer;
	private int timerCount;
	private Goal goal;
	private boolean run = true;
	
	public PFieldPanel()
	{	
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.WHITE);
		
		list = new ArrayList<PObject>();
		bots = new ArrayList<Robot>();
		
		goal = initialize();

		timer = new Timer(10, new TimerListener());
		timer.start();
		timerCount = 0;
		
	}
	


	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); 	
		
		for (PObject a : list)
		{
			a.draw(g);
		}
		
		for (Robot a : bots)
		{
			a.draw(g);
		}
	}
	
	public Goal initialize()
	{
		Goal goal = new Goal(210,75);
		list.add(goal);
		
		//Original Obstacle
		list.add(new Obstacle(50,60,20));
		list.add(new Obstacle(150,75,35));
		
		
		//Local Minimum where a solution exists but doesn't find it. 
		//list.add(new Obstacle(100,20,40));
		//list.add(new Obstacle(70,75,35));
		
		bots.add(new Robot(10,60));
		
		return goal;	
	}
	
	public void moveAll()
	{	
		for (Robot a : bots)
		{
			a.move(list);
		}
	}	
	
	
	public void checkTest(){
		for(Robot a : bots){
			if(goal.match(a)){
				double eDistance = Math.sqrt(Math.pow((a.getStartX() - goal.getX()),2) + Math.pow((a.getStartY() - goal.getY()),2));
				double cRatio = a.getDist() / eDistance;
				System.out.println("WE WON! Ratio is " + cRatio);
				run = false;
				
			}
		}
		
	}
	
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(run&&(timerCount%2 == 0)){
				moveAll();
				repaint();
				checkTest();
			}
			
			timerCount++;
		}
	}
}


