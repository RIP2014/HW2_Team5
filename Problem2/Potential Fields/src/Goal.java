import java.awt.*;

public class Goal implements PObject {
	
private double x,y;

private static int radius = 1;
private static int s = 10;
private static double alpha = 0.1;
	
	public Goal(int x, int y){
		super();
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics page)
	{
		page.setColor(Color.RED);
		page.fillOval((int)this.x, (int)this.y, 10, 10);	
	}
	
	public boolean match(Robot bot){
		double x = bot.getX();
		double y = bot.getY();
		
		double eDistance = Math.sqrt(Math.pow((x - this.x),2) + Math.pow((y - this.y),2));
		System.out.println(eDistance);
		if(eDistance < 5){
			return true;
		}
		return false;
		
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	@Override
	public double deltaX(double x, double y) {
	
		double eDistance = Math.sqrt(Math.pow((x - this.x),2) + Math.pow((y - this.y),2));
		double eGoal = Math.atan2((this.y - y), (this.x - x));
		
		if(eDistance < radius){
			return 0;
		}else if(eDistance > (s + radius)){
			return (alpha*s*Math.cos(eGoal));
		}else{
			return (alpha*(eDistance - radius)*Math.cos(eGoal)); 
		}
	}

	@Override
	public double deltaY(double x, double y) {
		
		double eDistance = Math.sqrt(Math.pow((x - this.x),2) + Math.pow((y - this.y),2));
		double eGoal = Math.atan2((this.y - y), (this.x - x));
		
		if(eDistance < radius){
			return 0;
		}else if(eDistance > (s + radius)){
			return (alpha*s*Math.sin(eGoal));
		}else{
			return (alpha*(eDistance - radius)*Math.sin(eGoal)); 
		}
	}

}
