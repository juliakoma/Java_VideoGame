package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Main.Game;
import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;

public class Level1State extends GameState{

    private TileMap tileMap;
    private Background bg;

    private Player player;
    private Dinosaur pet;

    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;

    private boolean gameOver;
    private boolean gamePaused;
    private long gameOverStartTime;
    private boolean gameOverCountdownStarted;

    private HUD hud;

    private long startTime;
    private long elapsedTime;
    private long targetTime = 120000;

    private ArrayList<Confetti> confetti;
    private boolean playerWon;

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
        //player.setPosition(3000,100);
        populateEnemies();

        pet = new Dinosaur(tileMap);
        pet.setPosition(3170,178);

        explosions = new ArrayList<Explosion>();

        hud = new HUD(player);

        gameOver = false;
        gamePaused = false;
        gameOverCountdownStarted = false;

        startTime = System.currentTimeMillis(); 

        playerWon = false;

        confetti = new ArrayList<>();
        playerWon = false;
    }

    private void populateEnemies(){
        enemies = new ArrayList<Enemy>();
        Slugger s;
        Point[] points = new Point[] {
            new Point(200, 100),
            new Point(250, 100),
            new Point(830,200),
            new Point(1000, 200),
            new Point(1525, 200),
            new Point(1650, 200),
            new Point(1800, 200),
            new Point(2250,200),
            new Point(2400, 200),
            new Point(2700, 200),
            new Point(2710, 200),
            new Point(2730, 200),
            new Point(2750,200),
            new Point(2780, 200),
            new Point(2800, 200),
            new Point(2830, 200),
            new Point(2840, 200),
            new Point(2850, 200),
            new Point(2900, 200),
            new Point(2920, 200),
            new Point(2950, 200),
            new Point(2970, 200),
            new Point(3000, 200),
            new Point(3050, 200),
            new Point(3100, 200)
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
            
        // Piece of grass //
            BufferedImage weed = ImageIO.read(getClass().getResourceAsStream("/Resources/Tilesets/weed.png"));
            tileImages[3] = weed;

        // Green flower //
            BufferedImage flower1 = ImageIO.read(getClass().getResourceAsStream("/Resources/Tilesets/flower1.png"));
            tileImages[4] = flower1;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tileImages;
    }

    public void update(){

        if (!gameOver && !gamePaused){
            // Update player
            player.update();
            pet.update();
            tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
    
            // Set background
            bg.setPosition(tileMap.getx(), tileMap.gety() );

            // Check if player is trying to attack enemies
            player.checkAttack(enemies);

            // Update enemies
            for (int i = 0; i < enemies.size(); i++){
                Enemy e = enemies.get(i);
                e.update();

                if (e.isDead()){
                    enemies.remove(i);
                    i--;
                    explosions.add(new Explosion(e.getx(), e.gety()));
                }
            }

            // Update explosions
            for (int i = 0; i < explosions.size(); i++){
                explosions.get(i).update(); 
                if (explosions.get(i).shouldRemove()){
                    explosions.remove(i);
                    i--;
                }
            }

            if (player.isDead() && !gameOverCountdownStarted) {
                gameOverStartTime = System.currentTimeMillis();
                gameOverCountdownStarted = true;
                gameOver = true;
            }

            // Check if 1 minute has elapsed
            long currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - startTime;

            if (elapsedTime >= targetTime || elapsedTime >= 120000) {
                // If 60 seconds have passed, end the game
                gameOver = true;
                gamePaused = true;
            }

            // Check if player is within 20 pixels of the pet
            if (Math.abs(player.getx() - pet.getx()) <= 20 && Math.abs(player.gety() - pet.gety()) <= 20) {
                playerWon = true;
                gamePaused = true;
            }

            if (playerWon) {
                if (confetti.size() < 100) { 
                    confetti.add(new Confetti((int) (Math.random() * GamePanel.WIDTH), 0));
                }
                for (int i = 0; i < confetti.size(); i++) {
                    confetti.get(i).update();
                    if (confetti.get(i).isOffScreen(GamePanel.HEIGHT)) {
                        confetti.remove(i);
                        i--;
                    }
                }
            }            

        } else if (gameOver && !gamePaused && gameOverCountdownStarted) {
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
            // If the game over countdown has started and the game is not paused,
            long currentTime = System.currentTimeMillis();
            if (currentTime - gameOverStartTime >= 4000) { 
                gamePaused = true;
            }
        } else if (gamePaused && playerWon) {
            // Add new confetti periodically
            if (confetti.size() < 100) { // Limit the number of confetti pieces
                confetti.add(new Confetti((int) (Math.random() * GamePanel.WIDTH), 0));
            }
            for (int i = 0; i < confetti.size(); i++) {
                confetti.get(i).update();
                if (confetti.get(i).isOffScreen(GamePanel.HEIGHT)) {
                    confetti.remove(i);
                    i--;
                }
            }
        }
        else{}
    }
    
    public void draw(Graphics2D g){

        if (!gameOver && !gamePaused){
            // draw background
            bg.draw(g);

            // draw tilemap
            tileMap.draw(g); 

            // draw player
            player.draw(g);

            // draw pet
            pet.draw(g);

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

            // Calculate remaining time
            long remainingTime = targetTime - elapsedTime;

            // Check if remaining time is less than or equal to 10 seconds
            Color timerColor = remainingTime <= 10000 ? Color.RED : Color.WHITE;
            g.setColor(timerColor);

            // Draw timer
            g.setFont(new Font("Arial", Font.BOLD, 18));
            int timerWidth = g.getFontMetrics().stringWidth(formatTime(elapsedTime));
            g.drawString(formatTime(elapsedTime), GamePanel.WIDTH - timerWidth - 10, 25);
        }
        else if (gameOver && !gamePaused){
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
        else if (gamePaused && playerWon) {
            // Draw winning screen
            g.setColor(new Color(255, 182, 193)); // Light pink color
            g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        
            // Draw "You Win!" text
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String winMsg = "YOU WIN!";
            int msgWidth = g.getFontMetrics().stringWidth(winMsg);
            g.drawString(winMsg, (GamePanel.WIDTH - msgWidth) / 2, GamePanel.HEIGHT / 2);
        
            // Draw "Press Enter to Play Again" text
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            String playAgainMsg = "Press Enter to Play Again";
            int playAgainWidth = g.getFontMetrics().stringWidth(playAgainMsg);
            g.drawString(playAgainMsg, (GamePanel.WIDTH - playAgainWidth) / 2, GamePanel.HEIGHT / 2 + 50);
        
            // Draw confetti
            for (Confetti c : confetti) {
                c.draw(g);
            }
        }
        
        else if (gamePaused){
            // Draw game over screen if game is over or paused
            g.setColor(new Color(0, 0, 0, 150)); 
            g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
            
            // Draw "Game Over" text
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String gameOverMsg = "GAME OVER";
            int msgWidth = g.getFontMetrics().stringWidth(gameOverMsg);
            g.drawString(gameOverMsg, (GamePanel.WIDTH - msgWidth) / 2, GamePanel.HEIGHT / 2);
            
            // Draw "Press Enter to Play Again" text
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            String playAgainMsg = "Press Enter to Play Again";
            int playAgainWidth = g.getFontMetrics().stringWidth(playAgainMsg);
            g.drawString(playAgainMsg, (GamePanel.WIDTH - playAgainWidth) / 2, GamePanel.HEIGHT / 2 + 50);
        }
    
    }

    public void keyPressed(int k){
        if (!gameOver && !gamePaused){
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
        else if (gamePaused) {
            if (k == KeyEvent.VK_ENTER) {
                // Reset the game to the homepage
                gsm.setState(GameStateManager.MENUSTATE);
            }
        }
        else{}

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

    private String formatTime(long elapsedTime) {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}
