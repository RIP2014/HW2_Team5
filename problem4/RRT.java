import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class RRT {
	private Node origin;
	public Node[] vertexList;
	public Node[][] edgeList;
	boolean ghettoBoolean;
	boolean ghettoBooleanTwoTheghettoerBoolean;
	public RRT(int x, int y, int iter, double[][] obstacles) {
		origin = new Node(x, y);
		origin.setParent(origin);
		int count = 1;
		int eCount = 0;
		vertexList = new Node[2*iter];
		edgeList = new Node[4*iter][2];
		vertexList[0] = origin;
		while (count < iter) {
			ghettoBoolean = true;
			Node p = new Node(Math.random()*500+50, Math.random()*500+50);
			if (Math.sqrt((300-p.x)*(300-p.x)+(300-p.y)*(300-p.y)) > 250) {
				continue;
			}
			for (int i = 0; i < obstacles.length; i++) {
				if (p.x >= obstacles[i][0] && p.x <= obstacles[i][1] && p.y >= obstacles[i][2] && p.y <= obstacles[i][3]) {
					ghettoBoolean = false;
				}
			}
			if(ghettoBoolean){
				holder np = findClose(p, origin, obstacles);
				if (np != null){
					if (np.mid == null && np.marker == 0) {
						np.young.addChild(np.rand);
						np.rand.parent = np.young;
						vertexList[count] = np.rand;
						count++;
						edgeList[eCount][0] = np.rand.parent;
						edgeList[eCount][1] = np.rand;
						eCount++;
					} else if(np.mid == null && np.marker == 1) {
						np.old.addChild(np.rand);
						np.rand.parent = np.old;
						vertexList[count] = np.rand;
						count++;
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
				}
			}
		}
	}
	//The loop that iterates through the whole tree to make sure we get as close as possible
	private holder findClose(Node p, Node o, double[][] obstacles) {
		holder ans = null;
		ghettoBooleanTwoTheghettoerBoolean = true;
		for (int i = 0; i < 3; i++) {
			if(o.children[i] != null) {
				holder pnp = findClose(p, o.children[i], obstacles);
				if (ans == null ) {
					ans = pnp;
				} else {
					if (ans != null && pnp != null){
						if (pnp.dist < ans.dist) {
							ans = pnp;
						}
					}
				}
			}

		}
		holder pnp = distP2S(p, o, o.parent);
		for (int l = 0; l < obstacles.length; l++){
			Rectangle.Double r1 = new Rectangle.Double(obstacles[l][0],obstacles[l][1],obstacles[l][2],obstacles[l][3]);
			if ( pnp.mid != null) {
				Line2D.Double l2 = new Line2D.Double(pnp.mid.x,pnp.mid.y,pnp.rand.x,pnp.rand.y);
				if(l2.intersects(r1)){//java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][2], obstacles[l][0], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.mid.x, pnp.mid.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][1], obstacles[l][2], obstacles[l][1], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.mid.x, pnp.mid.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][2], obstacles[l][1], obstacles[l][2], pnp.rand.x, pnp.rand.y, pnp.mid.x, pnp.mid.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][3], obstacles[l][1], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.mid.x, pnp.mid.y)) {
					ghettoBooleanTwoTheghettoerBoolean = false;
				}
			} else if (pnp.marker == 0) {
				Line2D.Double l2 = new Line2D.Double(pnp.young.x,pnp.young.y,pnp.rand.x,pnp.rand.y);
				if(l2.intersects(r1)){//java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][2], obstacles[l][0], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.young.x, pnp.young.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][1], obstacles[l][2], obstacles[l][1], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.young.x, pnp.young.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][2], obstacles[l][1], obstacles[l][2], pnp.rand.x, pnp.rand.y, pnp.young.x, pnp.young.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][3], obstacles[l][1], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.young.x, pnp.young.y)) {
					ghettoBooleanTwoTheghettoerBoolean = false;
				}
			}else if (pnp.marker == 1) {
				Line2D.Double l2 = new Line2D.Double(pnp.old.x,pnp.old.y,pnp.rand.x,pnp.rand.y);

				if(l2.intersects(r1)){//java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][2], obstacles[l][0], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.old.x, pnp.old.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][1], obstacles[l][2], obstacles[l][1], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.old.x, pnp.old.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][2], obstacles[l][1], obstacles[l][2], pnp.rand.x, pnp.rand.y, pnp.old.x, pnp.old.y) || java.awt.geom.Line2D.linesIntersect(obstacles[l][0], obstacles[l][3], obstacles[l][1], obstacles[l][3], pnp.rand.x, pnp.rand.y, pnp.old.x, pnp.old.y)) {
					ghettoBooleanTwoTheghettoerBoolean = false;
				}
			}
		}
		if (ans == null) {
			if (ghettoBooleanTwoTheghettoerBoolean){
				ans = pnp;
			}
		} else {
			if (pnp.dist < ans.dist && ghettoBooleanTwoTheghettoerBoolean) {
				ans = pnp;
			}
		}
		//ghettoBooleanTwoTheghettoerBoolean = true;
		return ans;
	}
	//The method to calculate the shortest distance to the tree
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
	//Vectors
	private class vec {
		public double x;
		public double y;
		public vec (Node b, Node a) {
			x = b.x - a.x;
			y = b.y - a.y;
		}
	}
	//The dot product
	private double dotP(vec A, vec B){
		return (A.x * B.x + A.y * B.y);
	}
	//The holder class for returning the full set of information
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
	//The nodes that represent states
	public class Node {
		public double x;
		public double y;
		public Node parent = null;
		private Node[] children = new Node[3];
		public Node(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public void setParent(Node o) {
			this.parent = o;
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
