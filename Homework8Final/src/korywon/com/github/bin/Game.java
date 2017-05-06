package korywon.com.github.bin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.concurrent.ThreadLocalRandom;

import korywon.com.github.shapes.IShape;
import korywon.com.github.shapes.JShape;
import korywon.com.github.shapes.LShape;
import korywon.com.github.shapes.OShape;
import korywon.com.github.shapes.SShape;
import korywon.com.github.shapes.TShape;
import korywon.com.github.shapes.ZShape;

/**
 * Manages the game Tetris. Keeps track of the current piece and the grid.
 * Updates the display whenever the state of the game has changed.
 * + Handles music when levels change
 *
 */
public class Game {

    private Grid grid; // the grid that makes up the Tetris board

    private Tetris display; // the visual for the Tetris game
    
    private Music[] music; // handles music of the game
    
    private Piece piece; // the current piece that is dropping

    private boolean isOver; // has the game finished?
    
    private boolean paused_status;	// is the game paused?
    
    private static int currentLevel = 1;	// tracks current level state of the game
    
    private static int linesCleared = 0;	// tracks number of lines that the user has cleared
    
    private static int nextPiece;	// tracks the next piece that will spawn
    
    /**
     * Creates a Tetris game
     *
     * @param Tetris the display
     */
    public Game(Tetris display, Music[] music) {
        grid = new Grid(this);
        this.display = display;
    	this.music = music;
        isOver = false;
        paused_status = false;
        nextPiece = ThreadLocalRandom.current().nextInt(1, 8);
        generatePiece();
        //piece = new IShape(1, Grid.WIDTH / 2 - 1, grid);
        checkMusic();	// begins song
    }
    
    /**
     * Draws the current state of the game
     *
     * @param g the Graphics context on which to draw
     */
    public void draw(Graphics g) {
        grid.draw(g);
        if (piece != null) {
            piece.draw(g);
        }
    }
    /**
     * Moves the piece in the given direction
     *
     * @param the direction to move
     */
    public void movePiece(Direction direction) {
        if (piece != null) {
            piece.move(direction);
        }
        updatePiece();
        grid.checkRows();
        display.update();
        
    }
    
    public void updateDisplay() {
    	display.update();
    }

    /**
     * Returns true if the game is over
     */
    public boolean isGameOver() {
        // game is over if the piece occupies the same space as some non-empty
        // part of the grid. Usually happens when a new piece is made
        if (piece == null) {
            return false;
        }

        // check if game is already over
        if (isOver) {
            closeMusic();
            return true;
        }

        // check every part of the piece
        Point[] p = piece.getLocations();
        for (int i = 0; i < p.length; i++) {
            if (grid.isSet((int) p[i].getX(), (int) p[i].getY())) {
                isOver = true;
                closeMusic();
                return true;
            }
        }
        return false;
    }
    
    /**
    *	Pauses the game.
    *	@param x true - pause game; false - unpause game
    */
    public void gamePause(boolean x) {
        if(x) {
            paused_status = true;
        }
        else {
            paused_status = false;
        }
    }
    
    /**
     * Checks to see if game is paused
     * @return returns condition of pause
     */
    
    public boolean isGamePaused() {
        if(paused_status) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Updates the piece
     */
    private void updatePiece() {
        if (piece == null) {
        	generatePiece();
        	//piece = new IShape(1, Grid.WIDTH / 2 - 1, grid);
        } 
        // set Grid positions corresponding to frozen piece
        // and then release the piece
        else if (!piece.canMove(Direction.DOWN)) {
            Point[] p = piece.getLocations();
            Color c = piece.getColor();
            for (int i = 0; i < p.length; i++) {
                grid.set((int) p[i].getX(), (int) p[i].getY(), c);
            }
            piece = null;
        }

    }
    
    /**
    * Generates random piece to the board.
    */
    private void generatePiece() {
    	
    	switch (nextPiece) {
	    	case 1:
	    		piece = new ZShape(1, Grid.WIDTH / 2 - 1, grid);
	    		break;
	    	case 2:
	    		piece = new OShape(1, Grid.WIDTH / 2 - 1, grid);
	    		break;
	    	case 3:
	    		piece = new JShape(1, Grid.WIDTH / 2 - 1, grid);
	    		break;
	    	case 4:
	    		piece = new TShape(1, Grid.WIDTH / 2 - 1, grid);
	    		break;
	    	case 5:
	    		piece = new SShape(1, Grid.WIDTH / 2 - 1, grid);
	    		break;
	    	case 6:
	    		piece = new IShape(1, Grid.WIDTH / 2 - 1, grid);
	    		break;
	    	case 7:
	    		piece = new LShape(1, Grid.WIDTH / 2 - 1, grid);
	    		break;
    	}
    	nextPiece = ThreadLocalRandom.current().nextInt(1, 8);
    	System.out.println("Next Piece: " + nextPiece);
    }
    
    public int getNextPiece() {
    	return nextPiece;
    }

    /**
     * Rotate the piece
     */
    public void rotatePiece() {
        if (piece != null) {
            piece.rotate();
        }
        updatePiece();
        grid.checkRows();
        display.update();
    }
    
    public void lineCleared() {
    	linesCleared += 1;
    }
    
    public int getLines() {
    	return linesCleared;
    }
    
    public int getLevel() {
    	return currentLevel;
    }
    
    /**
     * Checks current progress
     * Changes current level and music if changed
     */
    public void checkLevel() {
    	if (linesCleared % 10 == 0) {
    		System.out.println("LEVEL UP!");
    		currentLevel += 1;
    		System.out.println("LEVEL " + currentLevel);
    		checkMusic();
    	}
    }

    /**
     * Returns new speed of game based on current level
     * @return returns piece move time
     */
    public double getTick() {
    	// originally getLevel()
    	switch (getLevel()) {
    		case 1:
	    	case 2:
	    		return .72;
	    	case 3:
	    		return .63;
	    	case 4:
	    		return .55;
	    	case 5:
	    		return .47;
	    	case 6:
	    		return .38;
	    	case 7:
	    		return .3;
	    	case 8:
	    		return .22;
	    	case 9:
	    	case 10:
	    	case 11:
	    		return .13;
	    	case 12:
	    		return .1;
	    	case 13:
	    	case 14:
	    	case 15:
	    		return .08;
	    	case 16:
	    	case 17:
	    	case 18:
	    		return .07;
	    	case 19:
	    	case 20:
	    	case 21:
	    	case 22:
	    	case 23:
	    	case 24:
	    	case 25:
	    	case 26:
	    	case 27:
	    	case 28:
	    		return .05;
	    	case 29:
	    		return .03;
	    	default:
	    		return .02;
    	}
    }
    
    
    /**
     * Changes music based on current level.
     */
    private void checkMusic() {
    	switch (currentLevel) {
	    	case 1:
	            break;
	    	case 2:
	    		music[0].setVolume(1);
	    		music[0].playSong();
	            music[1].playSong();
	            music[2].playSong();
	            music[3].playSong();
	            music[4].playSong();
	            music[5].playSong();
	            music[6].playSong();
	    		break;
	    	case 3:
	    		music[1].setVolume(1);
	    		break;
	    	case 4:
	    		music[2].setVolume(1);
	    		break;
	    	case 5:
	    		music[3].setVolume(1);
	    		break;
	    	case 6:
	    		music[4].setVolume(1);
	    		break;
	    	case 7:
	    		music[5].setVolume(1);
	    		break;
	    	case 8:
	    	case 9:
	    		music[6].setVolume(1);
	    		break;
	    	case 10:
	    		music[0].close();
	            music[1].close();
	            music[2].close();
	            music[3].close();
	            music[4].close();
	            music[5].close();
	            music[6].close();

	            music[7].setVolume(1);
	            music[7].playSong();
	            music[8].playSong();
	            music[9].playSong();
	            music[10].playSong();
	            music[11].playSong();
	            music[12].playSong();
	            music[13].playSong();
	            music[14].playSong();
	            break;
	    	case 11:
	    		music[8].setVolume(1);
	    		break;
	    	case 12:
	    		music[9].setVolume(1);
	    		break;
	    	case 13:
	    		music[10].setVolume(1);
	    		break;
	    	case 14:
	    		music[11].setVolume(1);
	    		break;
	    	case 15:
	    		music[12].setVolume(1);
	    		break;
	    	case 16:
	    		music[13].setVolume(1);
	    		break;
	    	case 17:
	    	case 18:
	    		music[7].close();
	            music[8].close();
	            music[9].close();
	            music[10].close();
	            music[11].close();
	            music[12].close();
	            music[13].close();
	    		music[14].setVolume(1);
	    		break;
	    	default:
	            music[14].close();
	            
	            music[15].setVolume(1);
	            music[15].playSong();
	            break;
    	}
    }
    
    /**
     * Stops and closes all instances of music.
     */
    private void closeMusic() {
    	for (int i = 0; i < music.length; i++) {
    		music[i].close();
    	}
    }
}
