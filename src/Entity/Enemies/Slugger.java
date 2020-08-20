package Entity.Enemies;

import Audio.AudioPlayer;

import Entity.*;
import TileMap.TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Slugger extends Enemy {

	private BufferedImage[] sprites;
	
	private HashMap<String, AudioPlayer> slugsounds;
	
	// CONSTRUCTOR
	public Slugger(TileMap tm) {
		super(tm);
		moveSpeed = 0.05;
		maxSpeed = 0.4;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 2;
		damage = 1;
	

	// load sprites
	try {
		BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream
				("/Sprites/Enemies/slugger.gif")
				);
		sprites = new BufferedImage[3];
		for(int i =0; i < sprites.length; i++) {
			sprites[i] = spritesheet.getSubimage(width * i, 0, width, height);
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	animation = new Animation();
	animation.setFrames(sprites);
	animation.setDelay(300);
	
	right = true;
	facingRight = false;
	
	slugsounds = new HashMap<String, AudioPlayer>();
	slugsounds.put("hit", new AudioPlayer("/SFX/wilhelm.mp3"));
	
	}
	
	// FUNCTION
	private void getNextPosition() {
		
		// walking around
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		
		// faling down
		if(falling) {
			dy += fallSpeed;
		}
		else dy = 0;
		
	}
	public void updatePosition() {
		x += dx;
		y += dy;
	}
	public void update() {
		
		// update position
		
		getNextPosition();
		checkTileMapCollision();
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// hit a wall
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = true;
		}
		else if(left && dx == 0) {
			left = false;
			right = true;
			facingRight = false;
		}
		animation.update();
		updatePosition();
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) {return;}
		
		setMapPosition();
		
		super.draw(g);
	}
	
	public void getHit(int hitDamage) {
		if(dead || flinching) return;
		health -= hitDamage;
		slugsounds.get("hit").play();
		if(health < 0) health =0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	
}
