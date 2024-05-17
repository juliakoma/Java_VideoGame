package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Entity.GameState;
import Main.GamePanel;
import GameState.GameStateManager;

public class IntroState extends GameState {
    private static final String INTRO_TEXT = "Help me save my pet! The thief stole it!";

    private int charIndex = 0;
    private long lastCharTime;
    private long charDelay = 50; // Delay between each character (in milliseconds)
    private boolean introComplete = false;
    private GameStateManager gsm;
    private boolean enterPressed = false;

    private BufferedImage petImage;

    public IntroState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
        // Load the pet image
        try {
            petImage = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Pets/pet1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Check if the intro is complete and Enter key is pressed
        if (introComplete && enterPressed) {
            gsm.setState(GameStateManager.LEVEL1STATE);
            enterPressed = false; // Reset the enterPressed flag
        }

        // Update the intro text display
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCharTime > charDelay) {
            lastCharTime = currentTime;
            if (charIndex < INTRO_TEXT.length()) { 
                charIndex++;
            } else {
                introComplete = true;
            }
        }
    }

    public void draw(Graphics2D g) {
        // Clear the screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
    
        // Calculate the position to draw the images
        int imageX = (GamePanel.WIDTH - petImage.getWidth()) / 2;
        int imageY = GamePanel.HEIGHT / 9;
    
        // Draw the pet image on top of the heart
        if (petImage != null) {
            g.drawImage(petImage, imageX, imageY, null);
        }

        // Draw the "Level 1" text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String levelText = "Level 1";
        int levelTextWidth = g.getFontMetrics().stringWidth(levelText);
        int levelTextX = (GamePanel.WIDTH - levelTextWidth) / 2;
        int levelTextY = GamePanel.HEIGHT / 6;
        g.drawString(levelText, levelTextX, levelTextY);
    
        // Draw the intro text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        int textWidth = g.getFontMetrics().stringWidth(INTRO_TEXT.substring(0, charIndex));
        int textX = (GamePanel.WIDTH - textWidth) / 2;
        int textY = GamePanel.HEIGHT / 3;
        g.drawString(INTRO_TEXT.substring(0, charIndex), textX, textY);
        
        // Draw "Press Enter to continue" if the intro is complete
        if (introComplete) {
            String continueText = "Press Enter to continue";
            int continueTextWidth = g.getFontMetrics().stringWidth(continueText);
            int continueTextX = (GamePanel.WIDTH - continueTextWidth) / 2;
            g.drawString(continueText, continueTextX, textY + 20);
        }
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    
    public void keyReleased(int k) { }
    
}
