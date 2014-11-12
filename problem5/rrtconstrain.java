import java.awt.*;
import javax.swing.*;
import Jama.*;


public class rrtconstrain extends JPanel {
	//the x and y values of each vertex in the tree. Also holds the parent vertex in each node.
	// {x,y} {parentx, parenty}
	private double[][] treeVertices = new double[300][6];
	//the pair x-y values of each two vertices connected by an edge.
	// each adjacency is stored as {{x1, y1, x2, y2}}
	private double[][] treeAdjacencies = new double[300][6];
	private double deltaT = 1;
	private double newOne;
	private double newTwo;
	private double newThree;
	
	public rrtconstrain() {
	
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		//draw guides
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
		//draw origin point
		g.setColor(Color.RED);
		
		//draw goal point
		g.setColor(Color.BLUE);
		
		//draw each vertex
		g.setColor(Color.GREEN);
		for (int i=0;i<treeVertices.length;i++) {
			double[] asVertex = getXYValues(treeVertices[i][0], treeVertices[i][1], treeVertices[i][2]);
			drawVertex(asVertex[0], asVertex[1], g);
		}
		
		//draw each edge
		g.setColor(Color.BLACK);
		for (int i=0;i<treeAdjacencies.length;i++) {
			double[] asVertexOne = getXYValues(treeAdjacencies[i][0], treeAdjacencies[i][1], treeAdjacencies[i][2]);
			double[] asVertexTwo = getXYValues(treeAdjacencies[i][3], treeAdjacencies[i][4], treeAdjacencies[i][5]);
			drawEdge(asVertexOne[0], asVertexOne[1], asVertexTwo[0], asVertexTwo[1],g);
		}
		
	}
	
	public static void main(String[] args) {
	JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rrtconstrain vis = new rrtconstrain();
		vis.setPreferredSize(new Dimension (600,600));
		frame.getContentPane().add(vis, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		vis.run();
	}
	
	
	public void run() {

		double[] firstVertex = {1.5707,1.2708,0,1.5707,1.2708,0};
		treeVertices[0] = firstVertex;
		int j = 1;
		while (true) {
			//while the tree has not expanded to the endpoint, build the tree
			boolean goalLocated = false;
			
			while (j < 300) {
				//build the tree:
				//Step 1: sample random node
				double newThetaOne = Math.random() * 2 * Math.PI;
				double newThetaTwo = Math.random() * 2 * Math.PI;
				double newThetaThree = Math.random() * 2 * Math.PI;
				
				double[] coords = getXYValues(newThetaOne, newThetaTwo, newThetaThree);
				double newX = coords[0];
				double newY = coords[1];
				//Step 2: find closest neighbor to that node
				double[] parentNode = nearestNeighbor(newThetaOne, newThetaTwo, newThetaThree, j);
				
				//step 3
				double error = Math.abs(newY - 3);
				
				double dx = 0;
				double dy = error*deltaT;
				double dtheta = 0;
				
				double[][] vals = {{dx,dy,dtheta}};
				
				Matrix endEffector = new Matrix(vals);
				
				Matrix a = new Matrix(j(newThetaOne,newThetaTwo,newThetaThree));
				
				Matrix velocity = a.inverse().times(endEffector);		
				
				double[] parentNodeAsVertices = getXYValues(parentNode[0], parentNode[1], parentNode[2]);
				double dirVectorOne = (newThetaOne - parentNode[0]);
				double dirVectorTwo = (newThetaTwo - parentNode[1]);
				double dirVectorThree = (newThetaThree - parentNode[2]);
				double sumSq = Math.sqrt(Math.pow(dirVectorOne,2) + Math.pow(dirVectorTwo,2) + Math.pow(dirVectorThree,2));
	
				newOne = parentNode[0] + dirVectorOne * deltaT/sumSq;
				newTwo = parentNode[1] + dirVectorTwo * deltaT/sumSq;
				newThree = parentNode[2] + dirVectorThree * deltaT/sumSq;
				
				double[][] d = {{newOne,newTwo,newThree}};
				Matrix newNode = new Matrix(d);
	
				newNode = newNode.minus(velocity);
				
				
				
				
			
			
			
			
			
			
			
			/*
			//Step 3: Calculate q(s) that is a set distance along the vertex between closest to random
			double[] parentNodeAsVertices = getXYValues(parentNode[0], parentNode[1], parentNode[2]);
			double dirVectorOne = (newThetaOne - parentNode[0]);
			double dirVectorTwo = (newThetaTwo - parentNode[1]);
			double dirVectorThree = (newThetaThree - parentNode[2]);
			double sumSq = Math.sqrt(Math.pow(dirVectorOne,2) + Math.pow(dirVectorTwo,2) + Math.pow(dirVectorThree,2));

			newOne = parentNode[0] + dirVectorOne * deltaT/sumSq;
			newTwo = parentNode[1] + dirVectorTwo * deltaT/sumSq;
			newThree = parentNode[2] + dirVectorThree * deltaT/sumSq;
			
			
			//Step 4: Compute the error between pose of candidate node and workspace criteria
			if (newConfig(newOne, newTwo, newThree, parentNode[0], parentNode[1], parentNode[2])) {
				double[] newVertex = {newThetaOne, newThetaTwo, newThetaThree, parentNode[0], parentNode[1], parentNode[2]};
				treeVertices[j] = (newVertex);
				treeAdjacencies[j] = (newVertex);
			}
			//Step 5: Move node to goal node with jacobian control such that error is diminished
			
			//Step 6: add that new node to the tree
*/
			j = j + 1;
			System.out.println(j);
			}
			
		repaint();	
		}
	}
	/*
	*	Draws a vertex in the RRT
	*/
	public void drawVertex(double x, double y, Graphics g) {
		g.fillOval((int)x-2, (int)y-2, 4, 4);
	}
	/*
	*	Draws an edge in the RRT
	*/
	public void drawEdge(double x1, double x2, double y1, double y2, Graphics g) {
		g.drawLine((int)x1,(int)x2,(int)y1,(int)y2);
	}
	/*
	*	Finds the nearest vertex in the tree to the x y coordinates.
	*	@return {x,y} of nearest vertex
	*/
	public double[] nearestNeighbor(double thetaOne, double thetaTwo, double thetaThree, double j) {
		double[] xyNew = getXYValues(thetaOne, thetaTwo, thetaThree);
		double currentThetaOne = 300;
		double currentThetaTwo = 300;
		double currentThetaThree = 300;
		double currentMinDistance = 99999;
		for (int i=0;i<j;i++) {
			double[] xyTest = getXYValues(treeVertices[i][0], treeVertices[i][1], treeVertices[i][2]);
			double testingMinDistance = Math.sqrt(Math.pow(xyNew[0] - xyTest[0],2) + Math.pow(xyNew[1] - xyTest[1],2));
			if (testingMinDistance < currentMinDistance) {
				currentMinDistance = testingMinDistance;
				currentThetaOne = treeVertices[i][0];
				currentThetaTwo = treeVertices[i][1];
				currentThetaThree = treeVertices[i][2];
			}
		}
		double[] neighbor = {currentThetaOne, currentThetaTwo, currentThetaThree};
		return neighbor;
	}
	//compute the task error
	public double task_error(double thetaOne, double thetaTwo, double thetaThree) {
		//retrieve constraint
		double constraint = 0;
		//forward kinematics
		double[][] m1 = transformMatrix(thetaOne,100);
		double[][] m2 = transformMatrix(thetaTwo,100);
		double[][] m3 = transformMatrix(thetaThree,50);
		double[][] fullTransform = matrixMultiply(m1, m2);
		fullTransform = matrixMultiply(fullTransform, m3);
		double error = fullTransform[1][0] + fullTransform[1][1] + fullTransform[1][2];
		return error;
	}
	
	public double[][] transformMatrix(double theta, double l) {
		double[][] t = new double[4][4];
		
		t[0][0] = Math.cos(theta);
		t[0][1] = -Math.sin(theta);
		t[0][2] = 0;
		t[0][3] = -l;
		t[1][0] = Math.sin(theta);
		t[1][1] = Math.cos(theta);
		t[1][2] = 0;
		t[1][3] = 0;
		t[2][0] = 0;
		t[2][1] = 0;
		t[2][2] = 1;
		t[2][3] = 0;
		t[3][0] = 0;
		t[3][1] = 0;
		t[3][2] = 0;
		t[3][3] = 1;
		return t;
	
	}
	
	public double[][] matrixMultiply(double[][] m1, double[][] m2) {
		double[][] m = new double[4][4];
		for (int i=0;i<3;i++) {
			for (int j=0;j<4;j++) {
				m[i][j] = 0.0;
			}
		}
		for (int i=0;i<4;i++) {
			for (int j=0;j<4;j++) {
				for (int k=0;k<4;k++) {
					m[i][j] = m[i][j] + m1[i][k] + m2[k][j];
				}
			}
		}
		return m;
	}
	
	//get the x y values of the end effector using forward kinematics
	public double[] getXYValues(double thetaOne, double thetaTwo, double thetaThree) {
		double[] coord = {300,300};
		coord[0] = 100 * Math.cos(thetaOne) + 100 * Math.cos(thetaOne + thetaTwo) + 50 * Math.cos(thetaOne + thetaTwo + thetaThree) + 300;
		coord[1] = 100 * Math.sin(thetaOne) + 100 * Math.sin(thetaOne + thetaTwo) + 50 * Math.sin(thetaOne + thetaTwo + thetaThree) + 300;
		return coord;
	}
	
	public boolean newConfig(double thetaOne, double thetaTwo, double thetaThree, double nearThetaOne, double nearThetaTwo, double nearThetaThree) {
		
		double e = task_error(thetaOne, thetaTwo, thetaThree);
		double max_displacement = 5;
		while (Math.abs(e) > 5) {
			double[] coords = getXYValues(newOne, newTwo, newThree);
			System.out.println("coords:" + coords[0] + ", " + coords[1]);
			double[] chErr = ji(thetaOne, thetaTwo, thetaThree);
			System.out.println("error: " + chErr[0]);
			System.out.println("old: " + newOne);
			newOne = newOne - chErr[0];
			System.out.println("new: " + newOne);
			newTwo = newTwo - chErr[1];
			newThree = newThree - chErr[2];
			//if |qs - qr | > |qr-qnear|
			if (Math.abs(newOne-thetaOne+newTwo-thetaTwo+newThree-thetaThree) > Math.abs(thetaOne-nearThetaOne+thetaTwo-nearThetaTwo+thetaThree-nearThetaThree)) {
				System.out.println("a");
				return false;
			}
			e = task_error(newOne, newTwo, newThree);	
			System.out.println(e);
		}
		return true;
	}
	
	public double[][] j(double thetaOne, double thetaTwo, double thetaThree) {
		double[] coords = getXYValues(thetaOne, thetaTwo, thetaThree);
		double[][] j = new double[3][3];
		
		j[0][0] = -100*Math.sin(thetaOne) -50 * Math.sin(Math.sin(thetaOne + thetaTwo)) - 300 * Math.sin(Math.sin(thetaOne + thetaTwo + thetaThree));
		j[0][1] = -50 * Math.sin(Math.sin(thetaOne + thetaTwo)) - 300 * Math.sin(Math.sin(thetaOne + thetaTwo + thetaThree));
		j[0][2] = - 300 * Math.sin(Math.sin(thetaOne + thetaTwo + thetaThree));
		j[1][0] = -100 * Math.cos(thetaOne) + 50 * Math.cos(Math.cos(thetaOne + thetaTwo)) + 300 * Math.sin(Math.sin((thetaOne + thetaTwo + thetaThree)));
		j[1][1] =  50 * Math.cos(Math.cos((thetaOne + thetaTwo)) + 300 * Math.cos(Math.cos(thetaOne + thetaTwo + thetaThree)));
		j[1][2] = 300 * Math.cos(Math.cos(thetaOne + thetaTwo + thetaThree));
		j[2][0] = 1;
		j[2][1] = 1;
		j[2][2] = 1;
		
		return j;
	}
	
	public double[] ji(double thetaOne, double thetaTwo, double thetaThree) {
			double e = task_error(thetaOne, thetaTwo, thetaThree);
			double[] ij = new double[3];
			double sinx = Math.sin(thetaOne);
			double cosx = Math.cos(thetaOne);
			double siny = Math.sin(thetaTwo);
			double cosxy = Math.cos(thetaOne + thetaTwo);
			double sinxy = Math.sin(thetaOne + thetaTwo);
			double f = 50/(250*siny);
			
			
			ij[0] = e * f * cosxy;
			ij[1] = e * f * -(cosx + cosxy);
			ij[2] = e * f * cosx;
			
			return ij;
	}
	
	

}