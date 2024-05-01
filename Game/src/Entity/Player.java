package Entity;
import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends MapObject{
    // Desired dimensions for each sprite
    int desiredWidth = 69;
    int desiredHeight = 44;
    
    // player attributes
    private int health;
    private int maxHealth;
    protected boolean player;

    // CHANGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEeEE
    private int fire;
    private int maxFire;
    
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    // attacks
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;

    private ArrayList<FireBall> fireBalls;

    // scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    // gliding
    private boolean gliding;

    // animations
    private ArrayList<BufferedImage[]> sprites;

    private final int[] numFrames = {
        6, // Idle 0
        8, // Walking 1
        8, // Attack 2
        8, // Death 3
        8, // Jump 4
        6 // Fireball 5
    };

    // animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 4;
    private static final int FALLING = 4;
    private static final int GLIDING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 2;
    private static final int DEATH = 3;

    public Player(TileMap tm){
        super(tm);

        // change this
        //width = 30;
        width = 45;
        height = 45;
        cwidth = 35;
        cheight = 35;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        fire = maxFire = 2500;
        fireCost = 200;
        fireBallDamage = 5;

        fireBalls = new ArrayList<FireBall>();
        scratchDamage = 8;
        scratchRange = 40;
        player = false;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Player/PlayerSprites.png"));
        
            sprites = new ArrayList<>();
        
            int numRows = numFrames.length;
        
            for (int i = 0; i < numRows; i++) {
                int frames = numFrames[i];
        
                BufferedImage[] bi = new BufferedImage[frames];
        
                for (int j = 0; j < frames; j++) {
                    // Calculate the coordinates based on the desired sprite dimensions
                    int startX = j * desiredWidth;
                    int startY = i * desiredHeight;
                    bi[j] = spritesheet.getSubimage(startX, startY, desiredWidth, desiredHeight);
                }
        
                sprites.add(bi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getFire() { return fire; }
    public int getMaxFire() { return maxFire; }

    public void setFiring(){
        firing = true;
    }

    public void setScratching(){
        scratching = true;
    }

    public void setGliding(boolean b){
        gliding = b;
    }

    public boolean isDead(){
        return dead;
    }

    public void update(){
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        // check attack has stopped
        if (currentAction == SCRATCHING){
            if (animation.hasPlayedOnce()) scratching = false;
        }

        if (currentAction == FIREBALL){
            if (animation.hasPlayedOnce()) firing = false; 
        }

        if (currentAction == DEATH){
            if (animation.hasPlayedOnce()) {
                dead = true;
            }
        }

        if (dead){
            BufferedImage[] deathFrames = sprites.get(DEATH);

            // Create an array to hold only the last frame
            BufferedImage[] lastFrame = new BufferedImage[1];
            lastFrame[0] = deathFrames[deathFrames.length - 1]; 

            // Set the frame to only the last frame
            animation.setFrames(lastFrame);       
        }

        // fireball attack
        fire += 1;
        if (fire > maxFire) fire = maxFire;
        if (firing && currentAction != FIREBALL){
            if (fire > fireCost){
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);
                fireBalls.add(fb);
            }
        }

        // update fireballs
        for (int i = 0; i < fireBalls.size(); i++){
            fireBalls.get(i).update();
            if (fireBalls.get(i).shouldRemove()){
                fireBalls.remove(i);
                i--;
            }
        }

        // check if player done flinching
        if (flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;

            if (elapsed > 1000){
                flinching = false;
            }
        }

        // set animations
        if(scratching){
            if (currentAction != SCRATCHING){
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 45;
            }
        }
        else if (firing){
            if (currentAction != FIREBALL){
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 45;
            }
        }
        else if (dy > 0){
            if(gliding){
                if (currentAction != GLIDING){
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 40;
                }
            }
            else if(currentAction != FALLING){
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 40;
            }
        }
        else if (dy < 0){
            if (currentAction != JUMPING){
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 40;
            }
        }
        else if (left || right){
            if (currentAction != WALKING){
                currentAction = WALKING;

                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 45;
            }
        }
        else if (health == 0) {
            if (currentAction != DEATH) {
                currentAction = DEATH;
                animation.setFrames(sprites.get(DEATH));
                animation.setDelay(70);
                width = 45;
            }
        }
        else{
            if (currentAction != IDLE){
                currentAction = IDLE;

                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 40;
            }
        }

        animation.update();

        // set direction
        if (currentAction != SCRATCHING && currentAction != FIREBALL){
            if (right) {
                facingRight = true;
                player = false;
            }
            if (left) {
                facingRight = false;
                player = true;
            }
        }
    }

    public void draw(Graphics2D g){
        setMapPosition();

        // draw fireballs
        for (int i = 0; i < fireBalls.size(); i++){
            fireBalls.get(i).draw(g);
        }

        // draw player
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            
            if (elapsed / 100 % 2 == 0){
                return;
            }
        }

        super.draw(g);
    }

    private void getNextPosition(){
        // movement
        if (left){
            dx -= moveSpeed;
            if (dx < -maxSpeed){
                dx = -maxSpeed;
            }
        }
        else if(right){
            dx += moveSpeed;
            if (dx > maxSpeed){
                dx = maxSpeed;
            }
        }
        else{
            if (dx > 0){
                dx -= stopSpeed;
                if (dx < 0){
                    dx = 0;
                }
            }
            else if (dx < 0){ 
                dx += stopSpeed;
                if (dx > 0) {
                    dx = 0;
                }
            }
        }

        // cannot move while attacking
        if ((currentAction == SCRATCHING || currentAction == FIREBALL) && !(jumping || falling)) {
            dx = 0;
        }

        // jumping
        if (jumping && !falling){
            dy = jumpStart;
            falling = true;
        }

        // falling
        if (falling){
            if (dy > 0 && gliding) dy += fallSpeed * 0.1;
            else dy += fallSpeed;

            if (dy > 0) jumping = false;

            if (dy < 0 && !jumping) dy += stopJumpSpeed;

            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }
        
    }

    public void checkAttack(ArrayList<Enemy> enemies){
        // loop through enemies
        for (int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);

            // scratch attack
            if (scratching){
                if (facingRight){
                    if (e.getx() > x &&
                        e.getx() < x + scratchRange &&
                        e.gety() > y - height / 2 &&
                        e.gety() < y + height / 2
                    ){
                        e.hit(scratchDamage);
                    }
                }
                else{
                    if(
                        e.getx() < x &&
                        e.getx() > x - scratchRange &&
                        e.gety() > y - height / 2 &&
                        e.gety() < y + height / 2
                    ){
                        e.hit(scratchDamage);
                    }
                }
            }

            // check for fire
            for (int j = 0; j < fireBalls.size(); j ++){
                if (fireBalls.get(j).intersect(e)){
                    e.hit(fireBallDamage);
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            // check for enemy attack
            if (intersect(e)){
                hit(e.getDamage());
            }
        }
    }

    public void hit(int damage){

        if (flinching) return;

        health -= damage;
        if (health < 0){
            health = 0;
        }

        flinching = true;
        flinchTimer = System.nanoTime();  
    }

}
