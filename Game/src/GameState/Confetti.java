package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Confetti {
    private double x, y;
    private double dy;
    private Color color;
    private int size;
    
    public Confetti(int startX, int startY) {
        x = startX;
        y = startY;
        dy = 2 + Math.random() * 3; // Random falling speed
        Random rand = new Random();
        color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)); // Random color
        size = 5 + rand.nextInt(6); // Random size between 5 and 10
    }
    
    public void update() {
        y += dy;
    }
    
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, size, size);
    }
    
    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
}
