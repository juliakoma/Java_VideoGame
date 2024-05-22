package TileMap;
import Main.GamePanel;

import java.awt.image.BufferedImage;
import java.awt.image.*;
import javax.imageio.*;

import java.awt.*;

public class Background {
    
    private BufferedImage image;

    private double x;
    private double y;
    private double dx;
    private double dy;

    private double moveScale;
    
    public Background(String s, double ms){
        try{
            
            // Read the original image
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream(s));
            
            // Define the desired width and height
            int newWidth = 400; 
            int newHeight = 300; 

            // Create a new resized image
            image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();
            
            moveScale = ms;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // sets position for the background image
    public void setPosition(double x, double y){
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) % GamePanel.HEIGHT;
    }

    // creating paralax effect
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    // as player moves, must update x and y positions
    public void update(){
        x += dx;
        y += dy;
    }

    // drawing image to screen
    public void draw(Graphics2D g){

        g.drawImage(image, (int)x, (int)y, null);

        // if x less than 0, draw more images to the right
        if (x < 0){
            g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
        }

        // drawing more images to the left
        if (x > 0){
            g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
        }
    }

    public void draw(Graphics2D g, int x, int y, int width, int height) {
        g.drawImage(image, x, y, width, height, null);
        // Adjusting for horizontal wraparound
        if (x < 0) {
            g.drawImage(image, x + width, y, width, height, null);
        } else if (x > 0) {
            g.drawImage(image, x - width, y, width, height, null);
        }
    }
    
}
