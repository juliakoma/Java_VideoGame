package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Entity.GameState;


public class HelpState extends GameState {
    private GameStateManager gsm;

    public HelpState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    public void init() {
    }

    public void update() {
    }

    public void draw(Graphics2D g) {
        // Draw a white box for the instructions
        g.setColor(Color.WHITE);
        g.fillRect(50, 50, 225, 200);
    
        // Draw a black border around the box
        g.setColor(Color.BLACK);
        g.drawRect(50, 50, 225, 200);
    
        // Draw the instructions with black text
        g.setColor(Color.BLACK);
        g.drawString("Instructions:", 60, 80);
        g.drawString("1. Press the arrow keys to move.", 60, 100);
        g.drawString("2. Press the space bar to jump.", 60, 120);
        g.drawString("3. Press 'F' to launch a fireball.", 60, 140);
        g.drawString("4. Press 'R' to attack.", 60, 160);

        g.drawString("Press 'M' to go back to the main menu.", 60, 180);
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_M) {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    public void keyReleased(int k) {
        // Handle any key release events if needed
    }
}