package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

import Main.GamePanel;

public class MenuState extends GameState {
	
	private Background bg;
	//private MediaPlayer mediaPlayer; 
	private int currentChoice = 0;

	private String[] options = {
		"Start",
		"Help",
		"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Clip titleMusic; // Clip for title screen music
    private Clip gameMusic; // Clip for in-game music
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Resources/Backgrounds/welcome.png", 1);

			bg.setVector(-0.1, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
            // Load title screen music
            AudioInputStream titleMusicStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/Resources/Music/TitleScreen.wav"));
            titleMusic = AudioSystem.getClip();
            titleMusic.open(titleMusicStream);
            
            // Load in-game music
            AudioInputStream gameMusicStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/Resources/Music/Music.wav"));
            gameMusic = AudioSystem.getClip();
            gameMusic.open(gameMusicStream);
            
            // Start playing title screen music
            titleMusic.loop(Clip.LOOP_CONTINUOUSLY);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		// draw background
		bg.draw(g);
		bg.draw(g, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Pet Pursuit", 80, 70);
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
		
	}
	
	// Choices of the menu 
	private void select() {
		if(currentChoice == 0) {
            // Stop title screen music
            titleMusic.stop();
            // Start in-game music
            gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
            // start
            gsm.setState((GameStateManager.LEVEL1STATE));
		}
		if(currentChoice == 1) {
			// help
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}

	/*private void playMusic(Song song){
		Media media = new Media(new File(song.getLink()).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();
	}*/
	
}










