package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;


import TileMap.Background;
public class MenuState extends GameState {

	private Background bg;
	private int currentChoice =0;
	
	private String[] options = {
			"Start",
			"Help",
			"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg = new Background("/Backgrounds/menubg.png", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color (0, 0, 128);
			titleFont = new Font("Times new roman", Font.PLAIN, 28);
			
			font = new Font( "Times new roman", Font.PLAIN, 12);
			
		}
		catch(Exception e ) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	public void update() {
		bg.update();
	}
	public void draw (Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		//title
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("MERCY", 80, 70);
		
		//options
		
		g.setFont(font);
		for (int i= 0; i<options.length; i++) {
			if (i == currentChoice ) {
				g.setColor(Color.WHITE);
			}
			else {
				g.setColor(Color.CYAN);
			}
			g.drawString(options[i], 145, 140+ i*15);
		}
	}
	private void select() {
		if (currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if (currentChoice == 1) {
			//help
		}
		if (currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k ==KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length -1;
			}
			if (k==KeyEvent.VK_DOWN) {
				currentChoice++;
				if (currentChoice == options.length) {
					currentChoice =0;
				}
			}
		}
	}
	public void keyReleased (int k) {}

	
}
