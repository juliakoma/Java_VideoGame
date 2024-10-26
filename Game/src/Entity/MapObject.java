package Entity;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;
import Entity.Player;

// this will be the base class for objects of the game like the hero or enemy
public abstract class MapObject {
    // tile stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    // position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;

    // dimensions
    protected int width;
    protected int height;

    // collison box
    protected int cwidth;
    protected int cheight;

    // collision
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;

    // animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;

    // movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;

    // movement attributes
    protected double moveSpeed;
    protected double maxSpeed;
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;
    protected boolean player;

    // constructor
    public MapObject(TileMap tm){
        tileMap = tm;
        tileSize = tm.getTileSize();
    }

    public boolean intersect(MapObject o){
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    public Rectangle getRectangle(){
        return new Rectangle((int)x - cwidth, (int)y - cheight, cwidth, cheight);
    }

    public void calculateCorners(double x, double y) {
        int leftTile = (int)(x - cwidth / 2) / tileSize;
        int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cheight / 2) / tileSize;
        int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;

        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);

        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
    }

    public void checkTileMapCollision(){
        currCol = (int)x / tileSize;
        currRow = (int)y / tileSize;

        xdest = x + dx;
        ydest = y + dy;

        xtemp = x;
        ytemp = y;

        calculateCorners(x, ydest);

        // if going up, hit head off wall 
        if (dy < 0){
            if (topLeft || topRight){
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            }
            else{
                ytemp += dy;
            }
        }

        // land on ground after falling
        if (dy > 0){
            if (bottomLeft || bottomRight){
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            }
            else{
                ytemp += dy;
            }
        }

        calculateCorners(xdest, y);

        // moving to left, check if hit a wall
        if (dx < 0){
            if (topLeft || bottomLeft){
                dx = 0;
                xtemp = currCol * tileSize + cwidth / 2;
            }
            else{
                xtemp += dx;
            }
        }

        // moving to right, check if hit a wall
        if (dx > 0){
            if (topRight || bottomRight){
                dx = 0;
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            }
            else{
                xtemp += dx;
            }
        }

        // check if falling off cliff
        if (!falling){
            calculateCorners(x, ydest + 1);

            // player is falling
            if (!bottomLeft && !bottomRight){
                falling = true;
            }
        }
    }

    public int getx() { return (int)x; }
    public int gety() { return (int)y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getCWidth() { return cwidth; }
    public int getCHeight() { return cheight; }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void setMapPosition(){
        xmap = tileMap.getx();
        ymap = tileMap.gety();
    }

    public void setLeft(boolean b){ 
        player = true;
        left = b;
    }

    public void setRight(boolean b){ right = b;}
    public void setUp(boolean b){ up = b;}
    public void setDown(boolean b){ down = b;}
    public void setJumping(boolean b){ jumping = b;}

    public boolean notOnScreen(){
        return (x + xmap + width < 0) || (x + xmap - width > GamePanel.WIDTH) ||
            (y + ymap + height < 0) || (y + ymap - height > GamePanel.HEIGHT);
    }

    public void draw(Graphics2D g){
        if (facingRight){
            g.drawImage(animation.getImage(), 
                (int) (x + xmap - width / 2), 
                (int) (y + ymap - height / 2), 
                null
            );
        }
        else{
            int xOffset = 0; // Default value for xOffset
            if (player) {
                xOffset = -25; // Adjust the xOffset if the player is moving to the left
            }
            g.drawImage(animation.getImage(), 
                (int) (x + xmap - width / 2 + width), // Adjust the X position based on the xOffset
                (int) (y + ymap - height / 2),
                -width + xOffset,
                height,
                null
            );
        }
    }
}
