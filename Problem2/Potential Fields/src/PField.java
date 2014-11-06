import javax.swing.JFrame;

public class PField {
	//Main Function just run this to run the program. Nothing else special

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Test!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new PFieldPanel());
		frame.pack();
		frame.setVisible(true);
	}

}