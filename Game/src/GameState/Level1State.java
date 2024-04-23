package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.Game;
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;

public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;

    private Player player;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private HUD hud;

    // constructor
    public Level1State(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }

    public void init() {
        tileMap = new TileMap(30);
        
        // Load images using the loadImages() method
        BufferedImage[] tileImages = loadImages();
        
        // Pass the loaded images to the tileMap
        tileMap.loadTiles(tileImages);
        tileMap.loadMap("/Resources/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Resources/Backgrounds/Sunset.png", 0.1);
    
        player = new Player(tileMap);
        player.setPosition(100,100);

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);
    }

    private void populateEnemies(){
        enemies = new ArrayList<Enemy>();
        Slugger s;
        Point[] points = new Point[] {
            new Point(200, 100),
            new Point(860,200),
            new Point(1525, 200),
            new Point(1680, 200),
            new Point(1800, 200),
        };

        for (int i = 0; i < points.length; i++){
            s = new Slugger(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
        
    }

    public BufferedImage[] loadImages(){
        BufferedImage[] tileImages = new BufferedImage[30];
        
        try {
        // GRASS //
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream("/Resources/Tilesets/Grass.png"));
            int newWidth = originalImage.getWidth() / (int)4.9;  
            int newHeight = originalImage.getHeight() / (int)4.9;  
            
            // Create a new buffered image with the new dimensions
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
            
            // Draw the original image onto the new image with the desired dimensions
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();
            
            // Assign the resized image to the tileImages array
            tileImages[1] = resizedImage;        

        // DIRT //
            BufferedImage ogDIRT = ImageIO.read(getClass().getResourceAsStream("/Resources/Tilesets/Dirt.png"));
            int dirtWidth = originalImage.getWidth() / (int)4.9;  
            int dirtHeight = originalImage.getHeight() / (int)4.9;  
            
            // Create a new buffered image with the new dimensions
            BufferedImage resizedDirt = new BufferedImage(dirtWidth, dirtHeight, ogDIRT.getType());
            
            // Draw the original image onto the new image with the desired dimensions
            Graphics2D dirt2 = resizedDirt.createGraphics();
            dirt2.drawImage(ogDIRT, 0, 0, dirtWidth, dirtHeight, null);
            dirt2.dispose();
            
            // Assign the resized image to the tileImages array
            tileImages[2] = resizedDirt;   


        } catch (IOException e) {
            e.printStackTrace();
        }
        //tileImages[1] = ImageIO.read(getClass().getResourceAsStream("/path/to/tile2.png"));
        return tileImages;
    }

    public void update(){
        // update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
   
        // set background
        bg.setPosition(tileMap.getx(), tileMap.gety() );

        // check if player is trying to attack enemies
        player.checkAttack(enemies);

        // update enemies
        for (int i = 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            e.update();

            if (e.isDead()){
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getx(), e.gety()));
            }
        }

        // update explosions
        for (int i = 0; i < explosions.size(); i++){
            explosions.get(i).update(); 
            if (explosions.get(i).shouldRemove()){
                explosions.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics2D g){
        // draw background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g); 

        // draw player
        player.draw(g);

        // draw enemies
        for (int i = 0; i < enemies.size(); i++){
            enemies.get(i).draw(g);
        }

        // draw explosions
        for (int i = 0; i < explosions.size(); i++){
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
        }

        // draw hud
        hud.draw(g);
    }

    public void keyPressed(int k){
        if(k == KeyEvent.VK_LEFT){
            player.setLeft(true);
        }
        if (k == KeyEvent.VK_RIGHT){
            player.setRight(true);
        }
        if (k == KeyEvent.VK_UP){
            player.setUp(true);
        }       
        if (k == KeyEvent.VK_DOWN){
            player.setDown(true);
        }    
        if (k == KeyEvent.VK_SPACE){
            player.setJumping(true);
        }
        if (k == KeyEvent.VK_E){
            player.setGliding(true);
        }
        if (k == KeyEvent.VK_R){
            player.setScratching();
        }
        if (k == KeyEvent.VK_F){
            player.setFiring();
        }
    }

    public void keyReleased(int k){
        if(k == KeyEvent.VK_LEFT){
            player.setLeft(false);
        }
        if (k == KeyEvent.VK_RIGHT){
            player.setRight(false);
        }
        if (k == KeyEvent.VK_UP){
            player.setUp(false);
        }       
        if (k == KeyEvent.VK_DOWN){
            player.setDown(false);
        }    
        if (k == KeyEvent.VK_SPACE){
            player.setJumping(false);
        }
        if (k == KeyEvent.VK_E){
            player.setGliding(false);
        }
    }

}
