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
	private double[][] obstacles = {{450, 550, 320, 370}, {145, 195, 310, 360}, {140, 150, 185, 195}};
	private double[][] fieldPotentialMulti = {{0,0,0}, {0,0,0}, {0,0,0}};
	public RRT theTree;
	public armvismultiobstacles() {
		
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
		if(this.theTree != null){
			for (int i=0;i<this.theTree.vertexList.length;i++) {
				if (this.theTree.vertexList[i] != null) {
					drawVertex((int)this.theTree.vertexList[i].x,(int)this.theTree.vertexList[i].y, g);
				}
			}
		}
		
		//draw each edge
		g.setColor(Color.BLACK);
		if(this.theTree != null){
			for (int i=0;i<this.theTree.edgeList.length;i++) {
				if (this.theTree.edgeList[i][0] != null){
					drawEdge(this.theTree.edgeList[i][0].x, this.theTree.edgeList[i][1].x, this.theTree.edgeList[i][0].y, this.theTree.edgeList[i][1].y,g);
				}
			}
		}
		
	}
	
	public void drawVertex(double x, double y, Graphics g) {
		g.fillOval((int)x-2, (int)y-2, 4, 4);
	}
	
	public void drawEdge(double x1, double x2, double y1, double y2, Graphics g) {
		g.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
	}
	
	public static void main(String [] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		armvismultiobstacles vis = new armvismultiobstacles();
		vis.setPreferredSize(new Dimension (600,600));
		frame.getContentPane().add(vis, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		vis.run2();
		//vis.run();
	}
	
	public void run2() {
		Node o = new Node(250, 250);
		o.parent = o;
		this.theTree = new RRT(o, 10000);
		repaint();
	}
	
	public void run() {
		
		while (currCoords[0]<1000000) {
		
		moveOnToNextCoord = 2;
		for (int i=0;i<2;i++) {
			if (Math.abs(currCoords[i] - desiredCoords[i]) < .0001) {
				moveOnToNextCoord = moveOnToNextCoord - 1;
			}
		}
		if (moveOnToNextCoord == 0) {
			System.out.println("Current Coords: x: " + currCoords[0] + ", y: " + currCoords[1] + ", theta: " + currCoords[2]);
			System.out.println("Desired Coords: x: " + desiredCoords[0] + ", y: " + desiredCoords[1] + ", theta: " + desiredCoords[2]);
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
			Thread.sleep(5);
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
	
	//Origin must have its parent set as itself!!!
	private class RRT {
		private Node origin;
		public Node[] vertexList;
		public Node[][] edgeList;
		public RRT(Node o, int iter) {
			origin = o;
			int count = 1;
			int eCount = 0;
			vertexList = new Node[2*iter];
			edgeList = new Node[4*iter][2];
			vertexList[0] = o;
			while (count < iter) {
				Node p = new Node(Math.random()*500+50, Math.random()*500+50);
				if (Math.sqrt((300-p.x)*(300-p.x)+(300-p.y)*(300-p.y)) > 250) {
					continue;
				}
				holder np = findClose(p, o);
				if (np.mid == null && np.marker == 0) {
					np.young.addChild(np.rand);
					np.rand.parent = np.young;
					vertexList[count] = np.rand;
					edgeList[eCount][0] = np.rand.parent;
					edgeList[eCount][1] = np.rand;
					eCount++;
				} else if(np.mid == null && np.marker == 1) {
					np.old.addChild(np.rand);
					np.rand.parent = np.old;
					vertexList[count] = np.rand;
					edgeList[eCount][0] = np.rand.parent;
					edgeList[eCount][1] = np.rand;
					eCount++;
				} else {
					np.old.replaceChild(np.mid, np.young);
					np.young.parent = np.mid;
					np.mid.addChild(np.rand);
					np.mid.addChild(np.young);
					np.mid.parent = np.old;
					np.rand.parent = np.mid;
					vertexList[count] = np.rand;
					edgeList[eCount][0] = np.rand.parent;
					edgeList[eCount][1] = np.rand;
					eCount++;
					edgeList[eCount][0] = np.young.parent;
					edgeList[eCount][1] = np.young;
					eCount++;
					vertexList[count+1] = np.mid;
					count++;
					//vertexList[count+2] = np.mid;
					//count++;
				}
				repaint();
				count++;
			}
		}
		
		private holder findClose(Node p, Node o) {
			holder ans = null;
			for (int i = 0; i < 3; i++) {
				if(o.children[i] != null) {
					holder pnp = findClose(p, o.children[i]);
					if (ans == null ) {
						ans = pnp;
					} else {
						if (pnp.dist < ans.dist) {
							ans = pnp;
						}
					}
				}
				
			}
			holder pnp = distP2S(p, o, o.parent);
			if (ans == null) {
				ans = pnp;
			} else {
				if (pnp.dist < ans.dist) {
					ans = pnp;
				}
			}
			return ans;
		}
		
	}
	
	/*double sqr(double x) { return x*x; }
	double dist2(Node v, Node w) { return sqr(v.x - w.x) + sqr(v.y - w.y); }
	holder dist(Node p, Node v, Node w) {
		double d = 0;
		double l1 = dist2(v,w);
		if (l1 == 0) {
			d = Math.sqrt(dist2(p,v));
			return new holder(null, d, v, w, p);
		}
		double t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l1;
		Node temp = new Node(v.x + t * (w.x - v.y), v.y + t * (w.x - v.y));
		d = Math.sqrt(dist2(p,temp));
		return new holder(temp, d, v, w, p);
	}*/
	
	private holder distP2S(Node P, Node A, Node B) {
		vec v = new vec(B, A);
		vec w = new vec(P, A);
		
		double c1 = dotP(w,v);
		if (c1 <= 0) {
			return new holder(null, Math.sqrt(((w.x)*(w.x))+((w.y)*(w.y))), A, B, P, 0);
		}
		double c2 = dotP(v,v);
		vec h = new vec(P, B);
		if (c2 <= c1) {
			return new holder(null, Math.sqrt(((h.x)*(h.x))+((h.y)*(h.y))), A, B, P, 1);
		}
		double b = c1/c2;
		Node Pb = new Node(A.x+b*(v.x), A.y+b*(v.y));
		h = new vec(P, Pb);
		return new holder(Pb, Math.sqrt(((h.x)*(h.x))+((h.y)*(h.y))), A, B, P, 2);
	}
	
	private class vec {
		public double x;
		public double y;
		public vec (Node b, Node a) {
			x = b.x - a.x;
			y = b.y - a.y;
		}
	}
	
	private double dotP(vec A, vec B){
		return (A.x * B.x + A.y * B.y);
	}
	
	private class holder {
		public Node mid;
		public double dist;
		public Node old;
		public Node young;
		public Node rand;
		public int marker;
		public holder(Node p, double d, Node A, Node B, Node rand, int n) {
			mid = p;
			dist = d;
			young = A;
			old = B;
			this.rand = rand;
			marker = n;
		}
	}
	
	public class Node {
		public double x;
		public double y;
		private Node parent = null;
		private Node[] children = new Node[3];
		public Node(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean addChild(Node c){
			for (int i = 0; i < 3; i++) {
				if (children[i] == null) {
					children[i] = c;
					return true;
				}
			}
			return false;
		}
		
		public boolean replaceChild(Node n, Node o) {
			for (int i = 0; i < 3; i++) {
				if (children[i].equals(o)) {
					children[i] = n;
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Node && this.x == ((Node) o).x && this.y == ((Node) o).y) {
				return true;
			}
			return false;
		}
	}
}
