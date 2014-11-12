import java.awt.*;
import javax.swing.*;
import Jama.*;
import java.util.Arrays;


public class rrtconstrain extends JPanel {
	//the theta1, theta2, theta3 in the tree. Also holds the parent vertex in each node.
	// {x,y} {parentx, parenty}
	private double[][] treeVertices = new double[300][6];
	//the pair x-y values of each two vertices connected by an edge.
	// each adjacency is stored as {{x1, y1, x2, y2}}
	private double[][] treeAdjacencies = new double[300][6];
	private double deltaT = 1;
	private double newOne;
	private double newTwo;
	private double newThree;
	int lastcounter = 1;
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
		//  
		g.setColor(Color.RED);
		double[] asVertex = getXYValues(treeVertices[0][0], treeVertices[0][1], treeVertices[0][2]);

			drawVertex(asVertex[0]*50 + 250, asVertex[1]*50 + 250, g);
		//draw goal point
		g.setColor(Color.BLUE);
		asVertex = getXYValues(1.5707, 1.2708, 0);
			drawVertex(asVertex[0]*50 + 250, asVertex[1]*50 + 250, g);
		//draw each vertex
		double[] pre2;
		double[] pre3;
		double[] pre4;
		for (int i=0;i<treeVertices.length;i++) {
			if (i == 0) {continue;}
			int waste = 0;
			while (waste < 2000000000) { waste++;}
			waste = 0;
			while (waste < 2000000000) { waste++;}
			waste = 0;
			while (waste < 2000000000) { waste++;}
			waste = 0;
			while (waste < 2000000000) { waste++;}
			g.setColor(Color.GREEN);
			double[] asVertexs = getXYValues(treeVertices[i][0], treeVertices[i][1], treeVertices[i][2]);
			pre2 = asVertexs;
			drawVertex(asVertexs[0]*50 + 250, asVertexs[1]*50 + 250, g);
			waste = 0;
		// 	while (waste < 2000000000) { waste++;}
			g.setColor(Color.YELLOW);
			double[] asVertexs1 = getXYValues1(treeVertices[i][0], treeVertices[i][1], treeVertices[i][2]);
			pre3 = asVertexs1;
			drawVertex(asVertexs1[0]*50 + 250, asVertexs1[1]*50 + 250, g);
		// 	waste = 0;
		// 	while (waste < 2000000000) { waste++;}
			g.setColor(Color.RED);
			double[] asVertexs22 = getXYValues2(treeVertices[i][0], treeVertices[i][1], treeVertices[i][2]);
			pre4 = asVertexs22;
			drawVertex(asVertexs22[0]*50 + 250, asVertexs22[1]*50 + 250, g);
		

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

		double[] firstVertex = {1.5707, -1.2708,0,1.5707, -1.2708,0};
		treeVertices[0] = firstVertex;
		int j = 1;
		double[] goalVertex = {1.5707, 1.2708, 0};
		double[] goalCoords = getXYValues(goalVertex[0], goalVertex[1], goalVertex[2]);
		while (true) {
			//while the tree has not expanded to the endpoint, build the tree
			double newThetaOne = Math.random() * 2 * Math.PI - Math.PI;
				double newThetaTwo = Math.random() * 2 * Math.PI -  Math.PI;
				double newThetaThree = Math.random() * 2 * Math.PI - Math.PI;
				
								//Step 2: find closest neighbor to that node
				double[] parentNode = nearestNeighbor(newThetaOne, newThetaTwo, newThetaThree, j);
				
				
				double[] parentNodeAsVertices = getXYValues(parentNode[0], parentNode[1], parentNode[2]);
				double dirVectorOne = (newThetaOne - parentNode[0]);
				double dirVectorTwo = (newThetaTwo - parentNode[1]);
				double dirVectorThree = (newThetaThree - parentNode[2]);
				double sumSq = Math.sqrt(Math.pow(dirVectorOne,2) + Math.pow(dirVectorTwo,2) + Math.pow(dirVectorThree,2));
	
				newOne = parentNode[0] + dirVectorOne * deltaT/sumSq;
				newTwo = parentNode[1] + dirVectorTwo * deltaT/sumSq;
				newThree = parentNode[2] + dirVectorThree * deltaT/sumSq;
				
				//step 3
				double[] coords = getXYValues(newOne, newTwo, newThree);
				double newX = coords[0];
				double newY = coords[1];
				double [] tempCoords = {newOne, newTwo, newThree};

				double error = Math.abs(newY - 3);		
			int k = 0;	
			while (k < 1000) {
				//build the tree:
				//Step 1: sample random node
				k = k+1;
				
				if (error < 0.0005) { break;}
				coords = getXYValues(newOne, newTwo, newThree);
				newX = coords[0];
				newY = coords[1];
				error = Math.abs(newY - 3);
				//System.out.println(error);


				double dx = 0;
				double dy = error*deltaT;
				double dtheta = 0;
				
				double[][] vals = {{dx},{dy},{dtheta}};
				
				Matrix endEffector = new Matrix(vals);
				
				Matrix a = new Matrix(j(newOne,newTwo,newThree));
				Matrix velocity = a.inverse().times(endEffector);		
				
				
				double[][] d = {{newOne},{newTwo},{newThree}};
				Matrix newNode = new Matrix(d);
	
				newNode = newNode.minus(velocity);
				newOne = newNode.get(0,0);
				newTwo = newNode.get(1,0);
				newThree = newNode.get(2,0);
			}	
				if(Math.abs(error) < 3){
					
					if (lastcounter == 299) {break;}
					//System.out.println("J");
					double[] newVertex = {newOne, newTwo, newThree, parentNode[0], parentNode[1], parentNode[2]};
					treeVertices[j] = newVertex;
					treeAdjacencies[j] = newVertex;
					j++;
					lastcounter++;
					repaint();

				}
				double[] newNodecoords =  getXYValues(newOne, newTwo, newThree);
				if (Math.sqrt(Math.pow(goalCoords[0] - newNodecoords[0], 2) + Math.pow(goalCoords[1] - newNodecoords[1], 2) + Math.pow(1.5707+1.2708-(newOne + newTwo + newThree),2)) < 0.2) {
					
					break;
				}
				
			
			
			
			//System.out.println(j);
			
			
		repaint();	
		}
	}
	/*
	*	Draws a vertex in the RRT
	*/
	public void drawVertex(double x, double y, Graphics g) {
		g.fillOval((int)x-2, (int)y-2, 7, 7);
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
		double currentThetaOne  = 300;
		double currentThetaTwo = 300;
		double currentThetaThree = 300;
		double currentMinDistance = 99999;
		/*for (int i=0;i<j;i++) {
			if (null != treeVertices[i]){
		*/
		for (double[] i : treeVertices) {
				
			
			double testingMinDistance = Math.sqrt(Math.pow(i[0] - thetaOne,2) + Math.pow(i[1] - thetaTwo,2) + Math.pow(i[2] - thetaOne,2));
			if (testingMinDistance < currentMinDistance) {
				currentMinDistance = testingMinDistance;
				currentThetaOne = i[0];
				currentThetaTwo = i[1];
				currentThetaThree = i[2];
			}
		}
		
		double[] neighbor = {currentThetaOne, currentThetaTwo, currentThetaThree};
		return neighbor;
	}
	
	
	//get the x y values of the end effector using forward kinematics
	public double[] getXYValues(double thetaOne, double thetaTwo, double thetaThree) {
		double[] coord = {300,300};
		coord[0] = 2 * Math.cos(thetaOne) + 2 * Math.cos(thetaOne + thetaTwo) +  Math.cos(thetaOne + thetaTwo + thetaThree) ;
		coord[1] = 2 * Math.sin(thetaOne) + 2 * Math.sin(thetaOne + thetaTwo) +  Math.sin(thetaOne + thetaTwo + thetaThree) ;
		return coord;
	}
	public double[] getXYValues1(double thetaOne, double thetaTwo, double thetaThree) {
		double[] coord = {300,300};
		coord[0] = 2 * Math.cos(thetaOne) + 2 * Math.cos(thetaOne + thetaTwo) ;
		coord[1] = 2 * Math.sin(thetaOne) + 2 * Math.sin(thetaOne + thetaTwo) ;
		return coord;
	}
	public double[] getXYValues2(double thetaOne, double thetaTwo, double thetaThree) {
		double[] coord = {300,300};
		coord[0] = 2 * Math.cos(thetaOne) ;
		coord[1] = 2 * Math.sin(thetaOne) ;
		return coord;
	}
	
	
	public double[][] j(double thetaOne, double thetaTwo, double thetaThree) {
		double[] coords = getXYValues(thetaOne, thetaTwo, thetaThree);
		double[][] j = new double[3][3];
		
		j[0][0] = -2*Math.sin(thetaOne) -2 * Math.sin(Math.sin(thetaOne + thetaTwo)) - 1 * Math.sin(Math.sin(thetaOne + thetaTwo + thetaThree));
		j[0][1] = -2 * Math.sin(Math.sin(thetaOne + thetaTwo)) - 1 * Math.sin(Math.sin(thetaOne + thetaTwo + thetaThree));
		j[0][2] = - 1 * Math.sin(Math.sin(thetaOne + thetaTwo + thetaThree));
		j[1][0] = -2 * Math.cos(thetaOne) + 2 * Math.cos(Math.cos(thetaOne + thetaTwo)) + 1 * Math.sin(Math.sin((thetaOne + thetaTwo + thetaThree)));
		j[1][1] =  2 * Math.cos(Math.cos((thetaOne + thetaTwo)) + 1 * Math.cos(Math.cos(thetaOne + thetaTwo + thetaThree)));
		j[1][2] = 1 * Math.cos(Math.cos(thetaOne + thetaTwo + thetaThree));
		j[2][0] = 1;
		j[2][1] = 1;
		j[2][2] = 1;
		
		return j;
	}
}	
	
	
