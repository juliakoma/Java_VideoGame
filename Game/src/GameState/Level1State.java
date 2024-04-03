package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.Game;
import Main.GamePanel;
import TileMap.*;
import Entity.*;

public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;

    private Player player;

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

        bg = new Background("/Resources/Backgrounds/Sunset.png", 0.1);
    
        player = new Player(tileMap);
        player.setPosition(100,100);
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
        player.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
    }

    public void draw(Graphics2D g){
        // draw background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g); 

        // draw player
        player.draw(g);
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
