package Entity;
import TileMap.TileMap;

public abstract class Enemy extends MapObject {

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    protected boolean flinching;
    protected boolean flinchTimer;

    public Enemy(TileMap tm){
        super(tm);
    }

    public boolean isDead() { return dead;}

    public int getDamage() { return damage;}

    public void hit(int damage){
        
    }
}
