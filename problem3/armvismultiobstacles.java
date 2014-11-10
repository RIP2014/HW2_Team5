
import java.awt.*;
import javax.swing.*;
import java.util.concurrent.*;

public class armvismultiobstacles extends JPanel {
	private float armOneLength = 100;
	private float armTwoLength = 100;
	private float armThreeLength = 50;
	private double a1x1 = 300;
	private double a1y1 = 300;
	private double a1x2 = 0;
	private double a1y2 = 0;
	private double a2x1 = 0;
	private double a2y1 = 0;
	private double a2x2 = 0;
	private double a2y2 = 0;
	private double a3x1 = 444.836;
	private double a3y1 = 282.75;
	private double a3x2 = 430;
	private double a3y2 = 235;
	private double a1theta = -.635;
	private double a2theta = 1.507;
	private double a3theta = 1;
	
	//private double[] currCoords = {5, 0, 0};
	//private double[] currAngles = {0,0,0};
	//private double[] desiredCoords = {2.6, 1.3, 1.0};
	private double[] currCoords = {1.09,2.8,1.0};
	private double[] currAngles = {.14, 1.45, 1.0};
	private double[] desiredCoords = {1.09,2.8,1.0};
	double[] fieldPotential = {0,0,0};
	double[] fieldx = {0,0,0};
	double[] fieldy = {0,0,0};
	double[] angleFromObstacle = {0,0,0};
	//private double[] finalPos = [2.6, 1.3, 1.0];
	private double kp = .05;
		private double[][] locations = {{-3.2, -.55, -2.1}};
	private int currentLocation = 0;
	int moveOnToNextCoord = 2;
	private double[][] obstacles = {{450, 550, 320, 370}, {145, 195, 310, 360}, {140, 390, 185, 285}};
	private double[][] fieldPotentialMulti = {{0,0,0}, {0,0,0}, {0,0,0}};
	
	public armvismultiobstacles() {
		
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(50, 0, 50, 599);
		g.drawLine(100, 0, 100, 599);
		g.drawLine(150, 0, 150, 599);
		g.drawLine(200, 0, 200, 599);
		g.drawLine(250, 0, 250, 599);
		g.drawLine(300, 0, 300, 599);
		g.drawLine(350, 0, 350, 599);
		g.drawLine(400, 0, 400, 599);
		g.drawLine(450, 0, 450, 599);
		g.drawLine(500, 0, 500, 599);
		g.drawLine(550, 0, 550, 599);
		g.drawLine(0, 50, 599, 50);
		g.drawLine(0, 100, 599, 100);
		g.drawLine(0, 150, 599, 150);
		g.drawLine(0, 200, 599, 200);
		g.drawLine(0, 250, 599, 250);
		g.drawLine(0, 300, 599, 300);
		g.drawLine(0, 350, 599, 350);
		g.drawLine(0, 400, 599, 400);
		g.drawLine(0, 450, 599, 450);
		g.drawLine(0, 500, 599, 500);
		g.drawLine(0, 550, 599, 550);
		
		g.setColor(Color.BLACK);
		//g.fillRect(5,5, (int) (armOneLength * lengthConversion), (int) width);
		g.drawLine((int) a1x1, (int) a1y1, (int) a1x2, (int) a1y2);
		g.drawLine((int) a2x1, (int) a2y1, (int) a2x2, (int) a2y2);
		g.drawLine((int) a3x1, (int) a3y1, (int) a3x2, (int) a3y2);
		g.setColor(Color.RED);
		g.fillOval((int) a1x1 - 4, (int) a1y1 - 4, 8, 8);
		g.setColor(Color.YELLOW);
		g.fillOval((int) a2x1 - 4, (int) a2y1 - 4, 8, 8);
		g.fillOval((int) a3x1 - 4, (int) a3y1 - 4, 8, 8);
		g.fillOval((int) a3x2 - 4, (int) a3y2 - 4, 8, 8);
		g.setColor(Color.GREEN);
		g.fillOval(230-4, 220-4, 8, 8);
		g.fillOval(430-4, 235-4, 8, 8);
		g.setColor(Color.BLUE);
		g.fillOval(265-4,235-4, 8, 8);
		g.fillOval(500-4, 345-4, 8, 8);
		g.fillOval(170-4, 335-4, 8, 8);
		g.setColor(Color.BLACK);
		//first rectangle: at 2x1
		g.drawLine(450, 370, 550, 370);
		g.drawLine(550, 370, 550, 320);
		g.drawLine(450, 320, 450, 370);
		g.drawLine(450, 320, 550, 320);
		
		//second rectangle at 1x1
		g.drawLine(195,310,195,360);
		g.drawLine(195,310,145,310);
		g.drawLine(145,310,145,360);
		g.drawLine(145,360,195,360);
		//third rectangle at 2x5
		g.drawLine(390,285,140,285);
		g.drawLine(390,185,390,285);
		g.drawLine(140,285,140,185);
		g.drawLine(140,185,390,185);
		
	}
	
	public static void main(String [] args) {
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		armvismultiobstacles vis = new armvismultiobstacles();
		vis.setPreferredSize(new Dimension (600,600));
		frame.getContentPane().add(vis, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		vis.run();
	}
	
	public void run() {
		while (true) {
		
		moveOnToNextCoord = 2;
		for (int i=0;i<2;i++) {
			if (Math.abs(currCoords[i] - desiredCoords[i]) < .0001) {
				moveOnToNextCoord = moveOnToNextCoord - 1;
			}
		}
		if (moveOnToNextCoord == 0) {
			System.out.println("x: " + currCoords[0] + ", y: " + currCoords[1] + ", theta: " + currCoords[2]);
			System.out.println("x: " + desiredCoords[0] + ", y: " + desiredCoords[1] + ", theta: " + desiredCoords[2]);
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
			
			}
			currentLocation = currentLocation += 1;
			if(currentLocation < locations.length) {
				desiredCoords = locations[currentLocation];
			 } 
		}
		
		
		
		double distToGoal = Math.sqrt(Math.pow((desiredCoords[0] * 50) - currCoords[0] * 50,2) + Math.pow(desiredCoords[1] - currCoords[1], 2));
		double[] fieldx = {0,0,0};
		double[] fieldy = {0,0,0};
		//go through each obstacle and add it to the potential field
		for (int i=0;i<obstacles.length;i++) {
		double[] currObstacle = obstacles[i];
			//first calculate the distance each arm is from the field.
			for (int j=0;j<3;j++) {
				double x = 0;
				double y = 0;
				switch (j) {
					case 0: 	x = a1x2;
								y = a1y2;
								break;
					case 1:		x = a2x2;
								y = a2y2;
								break;
					case 2:		x = a3x2;
								y = a3y2;
								break;
				}
				//there are 8 cases that we will handle for each rectangle. First we find the case:
				int currCase = 0;
				if (x < currObstacle[0] && y < currObstacle[2]) {
					currCase = 1;
				} else if (x > currObstacle[1] && y < currObstacle[2]) {
					currCase = 2;
				} else if (x > currObstacle[1] && y > currObstacle[3]) {
					currCase = 3;
				} else if (x < currObstacle[0] && y > currObstacle[3]) {
					currCase = 4;
				} else if (x < currObstacle[0] && y < currObstacle[3] && y > currObstacle[2]) {
					currCase = 5;
				} else if (x > currObstacle[0] && x < currObstacle[1] && y < currObstacle[2]) {
					currCase = 6;
				} else if (x > currObstacle[1] && y < currObstacle[3] && y > currObstacle[2]) {
					currCase = 7;
				} else if (x > currObstacle[0] && x < currObstacle[1] && y > currObstacle[3]) {
					currCase = 8;
				} else if (x > currObstacle[0] && x < currObstacle[1] && y > currObstacle[2] && y < currObstacle[3]) {
					System.out.println("inside");
					currCase = 0;
				}
				//now calculate the field potential for each case:
				double lenx = 0;
				double leny = 0;
				//length of x and y for each object:
				switch (i) {
					case 0: lenx = 2.5;
							leny = 1;
							break;
					case 1: lenx = 1;
							leny = .5;
							break;
					case 2: lenx = .5;
							leny = .5;
							break;
				}
				
				switch (currCase) {
						case 0: fieldPotentialMulti[i][j] = 0;
								break;
						case 1: fieldPotentialMulti[i][j] = Math.sqrt((x-currObstacle[0])*(x-currObstacle[0]) + (y-currObstacle[2])*(y-currObstacle[2]));
								break;
						case 2: fieldPotentialMulti[i][j] = Math.sqrt((x-currObstacle[1])*(x-currObstacle[1]) + (y-currObstacle[2])*(y-currObstacle[2]));
								break;
						case 3: fieldPotentialMulti[i][j] = Math.sqrt((x-currObstacle[1])*(x-currObstacle[1]) + (y-currObstacle[3])*(y-currObstacle[3]));
								break;
						case 4: fieldPotentialMulti[i][j] = Math.sqrt((x-currObstacle[0])*(x-currObstacle[0]) + (y-currObstacle[3])*(y-currObstacle[3]));
								break;
						case 5: fieldPotentialMulti[i][j] = Math.abs(x-currObstacle[0]) - lenx;
								break;
						case 6: fieldPotentialMulti[i][j] = Math.abs(y-currObstacle[2]) - leny;
								break;
						case 7: fieldPotentialMulti[i][j] = Math.abs(x-currObstacle[1]) - lenx;
								break;
						case 8: fieldPotentialMulti[i][j] = Math.abs(y-currObstacle[3]) - leny;
								break;
				}
				
				//calculate the angle from the obstacle from the midpoint of each obstacle
				angleFromObstacle[j] = Math.atan2(x-((currObstacle[0] + currObstacle[1])/2), y-((currObstacle[2] + currObstacle[3])/2));
				int z = 0;
				if (fieldPotentialMulti[i][j] == 0) {
					z=1;
				} else if (fieldPotentialMulti[i][j] < 20) {
					z=2;
				}
				
				switch (z) {
					case 1:		fieldx[i] = Double.POSITIVE_INFINITY;
								fieldy[i] = Double.POSITIVE_INFINITY;
								break;
					case 0:		fieldx[i] = fieldx[i];
								fieldy[i] = fieldy[i];
								break;
					case 2:		fieldx[i] = fieldx[i] + -.1 * ((20 - fieldPotentialMulti[i][j])*Math.cos(distToGoal)) + .1*(20-fieldPotentialMulti[i][j]) * Math.cos(angleFromObstacle[j]);
								fieldy[i] = fieldy[i] + -.1 * ((20 - fieldPotentialMulti[i][j])*Math.sin(distToGoal)) + .1*(20-fieldPotentialMulti[i][j]) * Math.sin(angleFromObstacle[j]);
								break;
				
				}
				
			}
		}
		System.out.println(" for 0: x: " + fieldx[0] + ", y: " + fieldy[0]);
		System.out.println(" for 1: x: " + fieldx[1] + ", y: " + fieldy[1]);
		System.out.println(" for 2: x: " + fieldx[2] + ", y: " + fieldy[2]);
		System.out.println("======================================================");
		
		double[][] ij = ij(desiredCoords);
		for (int i=0;i<3;i++) {
			double x = 0;
			for (int j=0;j<3;j++) {
				x = x + (ij[i][j] + Math.sqrt(Math.pow(fieldx[j],2) + Math.pow(fieldy[j],2))) * kp * (desiredCoords[i] - currCoords[i]);
				
			}
			currAngles[i] = currAngles[i] + x;
		}
		
		a1theta = currAngles[0];
		a2theta = currAngles[1];
		a3theta = currAngles[2] ;
		findLocation(a1x1, a1y1, 1, a1theta, armOneLength);
		findLocation(a2x1, a2y1, 2, a2theta, armTwoLength);
		findLocation(a3x1, a3y1, 3, a3theta, armThreeLength);
				currCoords[0] = (a3x2 - 300) / 50;
		currCoords[1] = (300 - a3y2) / 50;
		currCoords[2] = a3theta;
		try {
			Thread.sleep(50);
		}
		catch (InterruptedException e) {
			
		}
		repaint();
		}
	}
	
	public void findLocation(double startx, double starty, int numArm, double theta, float armLength) {
		switch (numArm) {
			case 1: theta = theta;
					break;
			case 2: theta = theta + this.a1theta;
					break;
			case 3: theta = theta + this.a2theta + this.a1theta;
					break;
		}
	
		double newEndx = startx + (double) armLength * Math.cos(theta);
		double newEndy = starty - (double) armLength  * Math.sin(theta);
		switch (numArm) {
			case 1: a1x2 = newEndx;
					a2x1 = newEndx;
					a1y2 = newEndy;
					a2y1 = newEndy;
					break;
			case 2: a2x2 = newEndx;
					a3x1 = newEndx;
					a2y2 = newEndy;
					a3y1 = newEndy;
					break;
			case 3: a3x2 = newEndx;
					a3y2 = newEndy;
		}
		
	}
	//finds the inverse jacobian
	public double[][] ij(double[] desired) {
			double x = desired[0];
			double y = desired[1];
			double theta = desired[2];
			double[][] ij = new double[3][3];
			double sinx = Math.sin(x);
			double cosx = Math.cos(x);
			double siny = Math.sin(y);
			double cosxy = Math.cos(x + y);
			double sinxy = Math.sin(x + y);
			double f = 1.0/(5.0*siny);
			
			ij[0][0] = f * -sinxy;
			ij[0][1] = f * cosxy;
			ij[0][2] = f * 4.0 * Math.sin(theta);
			ij[1][0] = f * (sinx + sinxy);
			ij[1][1] = f * -(cosx + cosxy);
			ij[1][2] = f * 4.0 * Math.sin(y + theta);
			ij[2][0] = f * -sinx;
			ij[2][1] = f * cosx;
			ij[2][2] = f * (5.0 * siny + 4.0 * Math.sin(y + theta));
			
			return ij;
	}
	
	

	

}