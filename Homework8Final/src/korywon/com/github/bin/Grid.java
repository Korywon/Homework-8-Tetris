package korywon.com.github.bin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.plaf.synth.SynthSeparatorUI;

/**
 * This is the Tetris board represented by a (HEIGHT - by - WIDTH) matrix of
 * Squares.
 *
 * The upper left Square is at (0,0). The lower right Square is at (HEIGHT -1,
 * WIDTH -1).
 *
 * Given a Square at (x,y) the square to the left is at (x-1, y) the square
 * below is at (x, y+1)
 *
 * Each Square has a color. A white Square is EMPTY; any other color means that
 * spot is occupied (i.e. a piece cannot move over/to an occupied square). A
 * grid will also remove completely full rows.
 *
 *
 */
public class Grid {

    private Square[][] board;
    
    private Square square;

    private Game game;
    
    // Width and Height of Grid in number of squares
    public static final int HEIGHT = 20;

    public static final int WIDTH = 10;

    private static final int BORDER = 5;

    public static final int LEFT = 15; // pixel position of left of grid

    public static final int TOP = 50; // pixel position of top of grid

    public static final Color EMPTY = Color.BLACK;

    /**
     * Creates the grid
     */
    public Grid(Game game) {
    	this.game = game;
        board = new Square[HEIGHT][WIDTH];
        
        // put squares in the board
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                board[row][col] = new Square(this, row, col, EMPTY, false);

            }
        }

    }

    /**
     * Returns true if the location (row, col) on the grid is occupied
     *
     * @param row the row in the grid
     * @param col the column in the grid
     */
    public boolean isSet(int row, int col) {
        return !board[row][col].getColor().equals(EMPTY);
    }

    /**
     * Changes the color of the Square at the given location
     *
     * @param row the row of the Square in the Grid
     * @param col the column of the Square in the Grid
     * @param c the color to set the Square
     * @throws IndexOutOfBoundsException if row < 0 || row>= WIDTH || col < 0 || col
     * >= HEIGHT
     */
    public void set(int row, int col, Color c) {
        board[row][col].setColor(c);
    }

    /**
     * Checks for and remove all solid rows of squares.
     *
     * If a solid row is found and removed, all rows above it are moved down and
     * the top row set to empty
     */
    private void removeRow(int r) {
        //change color of that row to white
        for (int col = 0; col < WIDTH; col++) {
            set(r, col, EMPTY);
        }

        //move the rest of the thing down
        for (int row = r - 1; row >= 0; row--) {
            for (int col = 0; col < WIDTH; col++) {
                if (isSet(row, col)) {
                    Color c = board[row][col].getColor();
                    board[row][col].setColor(EMPTY);
                    board[row + 1][col].setColor(c);
                }

            }
        }
    }

    public void checkRows() {
        int col, row;
        for (row = 0; row < HEIGHT; row++) {
            for (col = 0; col < WIDTH; col++) {
                if (!isSet(row, col)) {
                    break;
                }
            }
            if (col == WIDTH) // a row is found
            {
                removeRow(row);
                game.lineCleared();
                System.out.println("Lines Cleared: " + game.getLines());
                game.checkLevel();
            }
        }

    }

    /**
     * Draws the grid on the given Graphics context
     */
    public void draw(Graphics g) {

        // draw the edges as rectangles: left, right in blue then bottom in red
        g.setColor(Color.WHITE);    // left, right rectangles
        g.fillRect(LEFT - BORDER - 1, TOP, BORDER, (HEIGHT * Square.HEIGHT) + 2);
        g.fillRect(2 + LEFT + WIDTH * Square.WIDTH, TOP, BORDER, (HEIGHT
                * Square.HEIGHT) + 2);
        g.setColor(Color.WHITE);  // bottom rectangle
        g.fillRect((LEFT - BORDER) - 1, 2 + TOP + HEIGHT * Square.HEIGHT, 3 + (WIDTH
                * Square.WIDTH + 2 * BORDER), BORDER);
        
        // box to contain current level at top of grid
        g.drawRect((LEFT - BORDER - 1), 5, (WIDTH * Square.WIDTH + 2 * BORDER) + 2, BORDER + 30);
        g.drawRect(LEFT - BORDER, 6, (WIDTH * Square.WIDTH + 2 * BORDER), BORDER + 28);
        
        // level tracker
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("LEVEL " + game.getLevel(), 15, 30);
        
        // lines cleared tracker
        g.drawString("LINES: " + game.getLines(), 100, 30);
        
        // tracker for next piece
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("NEXT", 325, 150);
        g.setFont(new Font("Consolas", 0, 10));
        switch (game.getNextPiece()) {
	        case 1:
	        	g.drawString("Z Shape", 330, 300);
	        	g.setColor(Color.RED);
	        	g.fillRect(309, 217, 16, 16);
	        	g.fillRect(325, 217, 16, 16);
	        	g.fillRect(325, 233, 16, 16);
	        	g.fillRect(341, 233, 16, 16);
	        	break;
	        case 2:
	        	g.drawString("O Shape", 330, 300);
	        	g.setColor(Color.GRAY);
	        	g.fillRect(325, 217, 16, 16);
	        	g.fillRect(341, 217, 16, 16);
	        	g.fillRect(325, 233, 16, 16);
	        	g.fillRect(341, 233, 16, 16);
	        	break;
	        case 3:
	        	g.drawString("J Shape", 330, 300);
	        	g.setColor(Color.BLUE);
	        	g.fillRect(341, 217, 16, 16);
	        	g.fillRect(341, 233, 16, 16);
	        	g.fillRect(341, 249, 16, 16);
	        	g.fillRect(325, 249, 16, 16);
	        	break;
	        case 4:
	        	g.drawString("T Shape", 330, 300);
	        	g.setColor(Color.YELLOW);
	        	g.fillRect(309, 217, 16, 16);
	        	g.fillRect(325, 217, 16, 16);
	        	g.fillRect(341, 217, 16, 16);
	        	g.fillRect(325, 233, 16, 16);
	        	break;
	        case 5:
	        	g.drawString("S Shape", 330, 300);
	        	g.setColor(Color.GREEN);
	        	g.fillRect(341, 217, 16, 16);
	        	g.fillRect(325, 217, 16, 16);
	        	g.fillRect(325, 233, 16, 16);
	        	g.fillRect(309, 233, 16, 16);
	        	break;
	        case 6:
	        	g.drawString("I Shape", 330, 300);
	        	g.setColor(Color.CYAN);
	        	g.fillRect(309, 217, 16, 16);
	        	g.fillRect(325, 217, 16, 16);
	        	g.fillRect(341, 217, 16, 16);
	        	g.fillRect(357, 217, 16, 16);
	        	break;
	        case 7:
	        	g.drawString("L Shape", 330, 300);
	        	g.setColor(Color.MAGENTA);
	        	g.fillRect(325, 201, 16, 16);
	        	g.fillRect(325, 217, 16, 16);
	        	g.fillRect(325, 233, 16, 16);
	        	g.fillRect(341, 233, 16, 16);
        }
        
        // draw all the squares in the grid
        // empty ones first (to avoid masking the black lines of the pieces that have already fallen)
        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                if (board[r][c].getColor().equals(EMPTY)) {
                    board[r][c].draw(g);
                }
            }
        }
        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                if (!board[r][c].getColor().equals(EMPTY)) {
                    board[r][c].draw(g);
                }
            }
        }
        if (game.isGamePaused()) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.GREEN);
            g.drawString("GAME PAUSED", 80, 250);
        }
        
    }
}
