import java.util.*;
import java.awt.*;

public class Robot {
	
	private double x,y,startX,startY;
	private double dist;
	
	public Robot(int x, int y){
		this.x = x;
		this.y = y;
		this.startX = x;
		this.startY = y;
		this.dist = 0;
	}
	
	public void draw(Graphics page)
	{
		page.setColor(Color.GREEN);
		page.fillOval((int)this.x, (int)this.y, 10, 10);	
	}

	public void move(ArrayList<PObject> list) {
		double oX = this.x;
		double oY = this.y;
		for(PObject a : list){
			this.x = this.x + a.deltaX(this.x, this.y);
			this.y = this.y + a.deltaY(this.x, this.y);
		}
		
		this.dist = this.dist + Math.sqrt(Math.pow((oX - this.x),2) + Math.pow((oY - this.y),2));
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getDist() {
		return this.dist;
	}

	public int getStartX() {
		return (int)this.startX;
	}
	
	public int getStartY() {
		return (int)this.startY;
	}



}
