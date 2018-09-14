package Entity;


import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends MapObject{

	//player thingy
	private int health;
	private int maxHealth;
	private int magic;
	private int maxMagic;
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	//mana
	
	private boolean firing;
	private int fireCost;
	private int MagicDamage;
	//private ArrayList<mana>manas; yeah i'm not implementing mana yet
	
	
	//whip
	private boolean whip;
	private int whipDamage;
	private int whipRange;
	
	//flying
	
	private boolean flying;
	
	//animations
	private ArrayList<BufferedImage[]>sprites;
	private final int[] numFrames =  {
		2, 8, 1, 2, 4, 2, 5
	};
	
	//actions 
	
	private static final int IDLE = 0;
	private static final int WALKING =1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FLYING = 4;
	private static final int MAGIC = 5;
	private static final int WHIP = 6;
	
	public Player (TileMap tm) {
		super (tm);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		magic = maxMagic = 2500;
		
		fireCost = 200;
		MagicDamage = 5;
		//balls
		
		whipDamage = 8;
		whipRange = 40;
		
		//load sprites??
		
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/playersprites.png"));
			
			sprites = new ArrayList<BufferedImage[]>();
			for (int i =0; i < 7; i++ ) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					if (i != 6) {
					bi [j] = spritesheet.getSubimage(j*width, i*height, width, height);
				}
					else {
						bi [j] = spritesheet.getSubimage(j*width*2, i*height, width, height);
					}
					sprites.add(bi);
			}
		}
	}
				
		catch (Exception e ) {
			e.printStackTrace();
		}
		
		animation = new  Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
	}
			
			public int getHealth() { return health; }
			public int getMaxHealth() {return maxHealth;}
			public int getMagic() {return magic; }
			public int getMaxMagic() { return maxMagic;}
			
			public void setMagic() {
				firing = true;
				
			}
			
			public void setWhip() {
				whip = true;
			}
			public void setFlying(boolean b) {
				flying = b;
			}
			
			private void getNextPosition() {
				//mvmnt
				

				if(left)
				{
					dx -= moveSpeed;
					if(dx < -maxSpeed)
					{
						dx = -maxSpeed;
					}
				}
				else if(right)
				{
					dx += moveSpeed;
					if(dx > maxSpeed)
					{
						dx = maxSpeed;
					}
				}
				else 
				{
					if(dx > 0)
					{
						dx -= stopSpeed;
						if(dx < 0)
						{
							dx = 0;
						}
					}
					else if(dx < 0) 
					{
						dx += stopSpeed;
						if(dx > 0)
						{
							dx = 0;
						}
					}
				
				
				//cannot attack while moving
				
				if((currentAction == WHIP)  || (currentAction == MAGIC) && !(jumping || falling))
				{
					dx = 0;
				}
			if (jumping && !falling)
			{
				dy = jumpStart;
				falling = true;
			}
			if (falling)
			{
				if (dy > 0 && flying)
				{
					dy += fallSpeed * 0.1;
				}
				else
				{
					dy += fallSpeed;
				}
				if (dy> 0)
				{
					jumping = false;
				}
				if (dy < 0 && !jumping)
				{
					dy += stopJumpSpeed;
				}
				if (dy > maxFallSpeed)
				{
					dy *= maxFallSpeed;
				}
			}
		}
	}
			
			public void update() {
				//update position
				
				getNextPosition();
				checkTileMapCollision();
				setPosition(xtemp, ytemp);
				
				// set animation
				
				if (whip) {
					if (currentAction != WHIP) {
						currentAction = WHIP;
						animation.setFrames(sprites.get(WHIP));
						animation.setDelay(50);
						width = 60;
					}
				}
			
			else if (firing) {
				if (currentAction != MAGIC ) {
					currentAction = MAGIC;
					animation.setFrames(sprites.get(MAGIC));
					animation.setDelay(100);
					width = 30;
				}
					
				}
			else if (dy > 0) { 
				if (flying) {
					if (currentAction != FLYING) {
						currentAction = FLYING;
						animation.setFrames(sprites.get(FLYING));
						animation.setDelay(100);
						width= 30;
					}
				}
				else if (currentAction != FALLING) {
					currentAction = FALLING;
					animation.setFrames(sprites.get(FALLING));
					animation.setDelay(100);
					width = 30;
				}
			}
				
			else if (dy < 0) {
				if (currentAction != JUMPING) {
					currentAction = JUMPING;
					animation.setFrames(sprites.get(JUMPING));
					animation.setDelay(-1);
					width = 30;
				}
			}
				
			else if (left || right) {
				if(currentAction != WALKING) {
					currentAction = WALKING;
					animation.setFrames(sprites.get(WALKING));
					animation.setDelay(40);
					width = 30;
				}
			}
			else {
				if (currentAction != IDLE) {
					currentAction = IDLE;
					animation.setFrames(sprites.get(IDLE));
					animation.setDelay(400);
					width = 30;
				}
			}
				animation.update();
				
				//direction
				if (currentAction != WHIP &&  currentAction != MAGIC)
					if (right) facingRight = true;
				if (left) facingRight = false;
			}
		
		
		public void draw (Graphics2D g) {
			setMapPosition();
			
			//draw 
			
			if (flinching) {
				long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
						if (elapsed / 100 % 2 ==0) {
							return;
						}
			}
			if (facingRight) {
				g.drawImage(
						animation.getImage(),
						(int)(x + xmap - width /2),
						(int) (y + ymap - height /2),
						null);
			}
			else {
				g.drawImage(
						animation.getImage(),
						(int) (x + xmap - width /2 + width),
						(int) (y + ymap - height /2),
						-width,
						height,
						null);
				
			}
		}
	
	
	
	
	
	
	
}
