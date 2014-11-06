import java.awt.*;

public class Obstacle implements PObject{
	
	private double x,y,radius;
	private static int s = 10;
	private static double alpha = 0.1;
	
	public Obstacle(int x, int y, int radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void draw(Graphics page)
	{
		page.setColor(Color.YELLOW);
		page.fillOval((int)this.x, (int)this.y, (int)this.radius, (int)this.radius);	
	}

	@Override
	public double deltaX(double x, double y) {
		double eDistance = Math.sqrt(Math.pow((x - this.x),2) + Math.pow((y - this.y),2));
		double eGoal = Math.atan2((this.y - y), (this.x - x));
		
		if(eDistance < radius){
			return -(Math.cos(eGoal))*Double.POSITIVE_INFINITY;
		}else if(eDistance > (s + radius)){
			return 0;
		}else{
			return -(alpha*((s+radius) - eDistance)*Math.cos(eGoal)); 
		}
	}

	@Override
	public double deltaY(double x, double y) {
		double eDistance = Math.sqrt(Math.pow((x - this.x),2) + Math.pow((y - this.y),2));
		double eGoal = Math.atan2((this.y - y), (this.x - x));
		
		if(eDistance < radius){
			return -(Math.sin(eGoal))*Double.POSITIVE_INFINITY;
		}else if(eDistance > (s + radius)){
			return 0;
		}else{
			return -(alpha*((s+radius) - eDistance)*Math.sin(eGoal)); 
		}
	}
}
