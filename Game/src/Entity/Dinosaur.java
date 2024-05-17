package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Dinosaur extends Pet {
    private BufferedImage[] sprites;

    public Dinosaur(TileMap tm) {
        super(tm);

        moveSpeed = 0.3;
        maxSpeed = 0.3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        player = false;
        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        // load sprites
        try {
            BufferedImage spriteSheet = ImageIO.read(
                getClass().getResourceAsStream("/Resources/Sprites/Player/playersprites.gif")
            );

            sprites = new BufferedImage[3];

            for (int i = 0; i < sprites.length; i++) {
                BufferedImage sprite = spriteSheet.getSubimage(
                    i * width,
                    0,
                    width,
                    height
                );
                // Resize the sprite to 75% of its original size
                sprites[i] = resizeImage(sprite, (int) (width * 0.75), (int) (height * 0.75));
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage; 
    }

    public void draw(Graphics2D g) {
        setMapPosition();
        super.draw(g);
    }
}
