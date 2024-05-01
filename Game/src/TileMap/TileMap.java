package TileMap;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import Main.GamePanel;

import java.io.*;
 
public class TileMap {
    
    // position
    private double x;
    private double y;

    // bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    // smooth scrolling
    private double tween;

    // map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;

    // tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;

    // drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    // constructor
    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.07;
    }

    public void loadTiles(BufferedImage[] tileImages) {
        try {
            // Initialize the tiles array
            tiles = new Tile[2][tileImages.length];

            tiles[0][0] = new Tile(tileImages[0], Tile.NORMAL);
            // Assign each image to a tile
            for (int i = 1; i < tileImages.length; i++) {
                tiles[0][i] = new Tile(tileImages[i], Tile.BLOCKED); // Assuming all tiles are of type NORMAL
            }
            
            // Set the number of tiles across
            numTilesAcross = tileImages.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String s){
        try{
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;

            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;
            
            String delims = "\\s+";
            
            for (int row = 0; row < numRows; row++){
                String line = br.readLine();
                String[] tokens = line.split(delims);

                for (int col = 0; col < numCols; col++){
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getTileSize(){
        return tileSize;
    }

    public double getx(){
        return x;
    }

    public double gety(){
        return  y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getType(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return Tile.NORMAL;
        }
    
        int rc = map[row][col];
        if (rc < 0 || rc >= numTilesAcross) {
            return Tile.NORMAL;
        }
    
        if (tiles == null || tiles[0] == null || tiles[0].length <= rc) {
            return Tile.NORMAL;
        }
    
        int r = 0;
        int c = rc;
        return tiles[r][c].getType();
    }

    public void setTween(double d) { tween = d; }

    // helps keep camera moving smoother
    public void setPosition(double x, double y){
        this.x += (x - this.x) * tween;
        this.y += (x - this.y) * tween;

        fixBounds();
        //this.x = x

        colOffset = (int) - this.x / tileSize;
        rowOffset = (int) - this.y / tileSize;
    }

    private void fixBounds(){
        if (x < xmin) x = xmin;
        if (y < ymin) y = ymin;
        if (x > xmax) x = xmax;
        if (y > ymax) y = ymax;

    }

    public void draw(Graphics2D g){
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
            
            if (row >= numRows) break;

            for (int col = colOffset; col < colOffset + numColsToDraw; col++){
                if (col >= numCols) break;

                // first tile is nothing
                if (map[row][col] == 0) continue;

                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                
                g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
            
            }
        }
    }
}
