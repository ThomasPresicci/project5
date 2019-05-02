import javax.swing.JFrame;

public class HammingFrame extends JFrame {
	
	private static final long serialVersionUID = 2591096481466682426L;

	public HammingFrame() {
		super("Hamming Distance");
		HammingPanel panel = new HammingPanel();
		add(panel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new HammingFrame();
	}
}
