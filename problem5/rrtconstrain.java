import java.awt.*;
import javax.swing.*;


public class rrtconstrain extends JPanel {
	//the x and y values of each vertex in the tree. Also holds the parent vertex in each node.
	// {x,y} {parentx, parenty}
	private double[][] treeVertices = new double[300][4];
	//the pair x-y values of each two vertices connected by an edge.
	// each adjacency is stored as {{x1, y1, x2, y2}}
	private double[][] treeAdjacencies = new double[300][4];

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
			drawVertex(treeVertices[i][0], treeVertices[i][1], g);
		}
		
		//draw each edge
		g.setColor(Color.BLACK);
		for (int i=0;i<treeAdjacencies.length;i++) {
			drawEdge(treeAdjacencies[i][0], treeAdjacencies[i][1], treeAdjacencies[i][2], treeAdjacencies[i][3],g);
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
		double[] firstVertex = {300,300,300,300};
		treeVertices[0] = firstVertex;
		int j = 1;
		while (true) {
			//while the tree has not expanded to the endpoint, build the tree
			boolean goalLocated = false;
			
			while (j < 300) {
			//build the tree:
			//Step 1: sample random node
			double newX = Math.random() * 600;
			double newY = Math.random() * 600;
			System.out.println("chosen random point: " + newX + ", " + newY);
			//Step 2: find closest neighbor to that node
			double[] parentNode = nearestNeighbor(newX, newY, j);
			//Step 3: add that node to the tree
			double[] newVertex = {newX, newY, parentNode[0], parentNode[1]};
			System.out.println("test");
			treeVertices[j] = (newVertex);
			treeAdjacencies[j] = (newVertex);
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
	public double[] nearestNeighbor(double x, double y, double j) {
		double currentX = 300;
		double currentY = 300;
		double currentMinDistance = 99999;
		for (int i=0;i<j;i++) {
			double testingMinDistance = Math.sqrt(Math.pow(x - treeVertices[i][0],2) + Math.pow(y - treeVertices[i][1],2));
			if (testingMinDistance < currentMinDistance) {
				currentMinDistance = testingMinDistance;
				currentX = treeVertices[i][0];
				currentY = treeVertices[i][1];
			}
		}
		double[] neighbor = {currentX, currentY};
		return neighbor;
	}

}