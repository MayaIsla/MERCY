package Main;

import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		
		JFrame window = new JFrame ("MERCY");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.pack();
		window.setVisible(true);
		
	}
}
