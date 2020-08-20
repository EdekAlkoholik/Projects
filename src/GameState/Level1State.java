package GameState;

import java.awt.*;

import java.awt.event.KeyEvent;

import TileMap.*;
import Entity.*;
import Main.GamePanel;
import java.util.ArrayList; 

import Audio.AudioPlayer;

import Entity.Enemies.*;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	// CONSTRUCTOR
	public Level1State(GameStateMenager gsm) {
		this.gsm = gsm;
		init();
	}
	
	// METHODS
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0,0);
		tileMap.setTween(0.07);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1); 
		
		player = new Player(tileMap);
		
		player.setPosition(100, 100);
		
		populateEnemies();
		
		hud = new HUD(player);
		
		explosions = new ArrayList<Explosion>();
		
		bgMusic = new AudioPlayer("/Music/lotro.shire.mp3");
		bgMusic.play();
	}

	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		Slugger s;
		
		Point[] points = new Point[] {
			new Point(400, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200),
		};
		
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s); 
		}
		
	}
	
	
	public void update() {
		
		// update player
		player.update();
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(), 
				GamePanel.HEIGHT / 2 - player.gety()
				);
		
		// update background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// check enemy attack by player
		player.checkAttack(enemies);
		
		
		// update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				explosions.add(new Explosion(e.getx(), e.gety()));
				enemies.remove(i);
				i--;
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			Explosion e = explosions.get(i);
			e.update();
			if(e.shouldRemove()) {
				explosions.remove(i);
			}
		}
	}

	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw hud
		hud.draw(g);
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int) tileMap.getx(), (int) tileMap.gety());
			explosions.get(i).draw(g);
		}
		
	}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_SPACE) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_Q) player.setScratching();
		if(k == KeyEvent.VK_W) player.setFiring();
	}

	public void keyReleasd(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}

}
