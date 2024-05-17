package Entity;
import TileMap.TileMap;

public abstract class Pet extends MapObject {

    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    protected boolean flinching;
    protected long flinchTimer;

    public Pet(TileMap tm){
        super(tm);
    }

    public void update() {}
}
