package korywon.com.github.bin;

/**
 * Create and control the game Tetris.
 * 
 *
 *
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.*;

public class Tetris extends JPanel {

    private Game game;
    private Music[] music;

    /**
	 * Sets up the parts for the Tetris game, display and user control
     */
    public Tetris() {
        this.importSongs();
        game = new Game(this, music);
        JFrame f = new JFrame("The Berlin Wall Building Game");
        f.add(this);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setResizable(false);
        f.setSize(500, 500);	// default values (400, 550)
        f.setVisible(true);
        f.setLocationRelativeTo(null);  // centers window
        EventController ec = new EventController(game);
        f.addKeyListener(ec);
        setBackground(Color.BLACK);
    }
    
    /**
     * Imports all songs into program
     */
    public void importSongs() {
    	music = new Music[16];
    	
    	music[0] = new Music("TetrisB1.wav");
    	music[1] = new Music("TetrisB2.wav");
    	music[2] = new Music("TetrisB3.wav");
    	music[3] = new Music("TetrisB4.wav");
    	music[4] = new Music("TetrisB5.wav");
    	music[5] = new Music("TetrisB6.wav");
    	music[6] = new Music("TetrisB7.wav");
    	music[7] = new Music("TetrisA1.wav");
    	music[8] = new Music("TetrisA2.wav");
    	music[9] = new Music("TetrisA3.wav");
    	music[10] = new Music("TetrisA4.wav");
    	music[11] = new Music("TetrisA5.wav");
    	music[12] = new Music("TetrisA6.wav");
    	music[13] = new Music("TetrisA7.wav");
    	music[14] = new Music("TetrisA8.wav");
    	music[15] = new Music("TetrisFinal.wav");
    	
    	for (int i = 0; i < 16; i++) {
    		music[i].setVolume(0);
    	}
    }

    /**
	 * Updates the display
     */
    public void update() {
        repaint();
    }

    /**
	 * Paint the current state of the game
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g);
        
        if (game.isGameOver()) {
            g.setFont(new Font("Palatino", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 80, 250);
        }
    }

    public static void main(String[] args) {
        new Tetris();
    }

}
