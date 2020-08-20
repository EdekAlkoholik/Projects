package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject{

	// FIELDS
	protected int health;
	protected int maxHealth;
	protected int damage;
	protected boolean dead;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	// CONSTRUCTOR
	public Enemy(TileMap tm) {
		super(tm);
	}

	// METHODS
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public void getHit(int hitDamage) {
		if(dead || flinching) return;
		health -= hitDamage;
		if(health < 0) health =0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update() {}
	
}
