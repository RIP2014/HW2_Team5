import java.awt.Graphics;


public interface PObject {

	void draw(Graphics g);

	double deltaX(double x, double y);

	double deltaY(double x, double y);

}
