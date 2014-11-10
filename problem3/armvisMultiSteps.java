
import java.awt.*;
import javax.swing.*;
import java.util.concurrent.*;

public class armvisMultiSteps extends JPanel {
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
	int moveOnToNextCoord = 2;
	
	
	//private double[] currCoords = {5, 0, 0};
	//private double[] currAngles = {0,0,0};
	//private double[] desiredCoords = {2.6, 1.3, 1.0};
	private double[] currCoords = {2.6,1.3,1.0};
	private double[] currAngles = {-.635, 1.507, 1};
	private double[] desiredCoords = {-1.4,1.6,-2.0};
	//private double[] finalPos = [2.6, 1.3, 1.0];
	private double kp = .05;
	private double[][] locations = {{-1.4,1.6,-2.0}, {-1.6, 3, -.7}, {1,1,0}};
	private int currentLocation = 0;
	

	public armvisMultiSteps() {
		
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
		g.fillOval(230-4, 170-4, 8, 8);
	}
	
	public static void main(String [] args) {
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		armvisMultiSteps vis = new armvisMultiSteps();
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

		double[][] ij = ij(desiredCoords);
		for (int i=0;i<3;i++) {
			double x = 0;
			for (int j=0;j<3;j++) {
				x = x + ij[i][j] * kp * (desiredCoords[i] - currCoords[i]);
			}
			currAngles[i] = currAngles[i] + x;
		}
		a1theta = currAngles[0];
		a2theta = currAngles[1];
		a3theta = currAngles[2];
		findLocation(a1x1, a1y1, 1, a1theta, armOneLength);
		findLocation(a2x1, a2y1, 2, a2theta, armTwoLength);
		findLocation(a3x1, a3y1, 3, a3theta, armThreeLength);
		currCoords[0] = (a3x2 - 300) / 50;
		currCoords[1] = (300 - a3y2) / 50;
		currCoords[2] = a3theta;
		try {
			Thread.sleep(20);
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