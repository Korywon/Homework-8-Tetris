/*
 * Copyright(c) 2017
 * Alvin V. Huynh <www.github.com/korywon>
 * Created on Apr 20, 2017
 */

package korywon.com.github.bin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * @author Alvin V. Huynh <www.github.com/korywon>
 */

public abstract class AbstractPiece implements Piece {
	protected boolean ableToMove;	// can this piece move?
	
	protected Square[] square;	// squares that make up the piece
	
	protected Grid grid;	// the board the piece is on
	
	protected static final int PIECE_COUNT = 4;	// number of square in the piece
	
	private final boolean canRotate;
	
	public AbstractPiece(int r, int c, Grid g, boolean canRotate) {
		grid = g;
		square = new Square[PIECE_COUNT];
		ableToMove = true;
		this.canRotate = canRotate;
	}
	
	/**
     * Draws the piece on the given Graphics context
     */
    public void draw(Graphics g) {
        for (int i = 0; i < PIECE_COUNT; i++) {
            square[i].draw(g);
        }
    }

    /**
     * Moves the piece if possible Freeze the piece if it cannot move down
     * anymore
     *
     * @param direction the direction to move
     */
    public void move(Direction direction) {
        if (canMove(direction)) {
            for (int i = 0; i < PIECE_COUNT; i++) {
                square[i].move(direction);
            }
        } // if we couldn't move, see if because we're at the bottom
        else if (direction == Direction.DOWN) {
            ableToMove = false;
        }
    }

    /**
     * Returns the (row,col) grid coordinates occupied by this Piece
     *
     * @return an Array of (row,col) Points
     */
    public Point[] getLocations() {
        Point[] points = new Point[PIECE_COUNT];
        for (int i = 0; i < PIECE_COUNT; i++) {
            points[i] = new Point(square[i].getRow(), square[i].getCol());
        }
        return points;
    }

    /**
     * Return the color of this piece
     */
    public Color getColor() {
        // all squares of this piece have the same color
        return square[0].getColor();
    }

    /**
     * Returns if this piece can move in the given direction
     *
     */
    public boolean canMove(Direction direction) {
        if (!ableToMove) {
            return false;
        }

        // Each square must be able to move in that direction
        boolean answer = true;
        for (int i = 0; i < PIECE_COUNT; i++) {
            answer = answer && square[i].canMove(direction);
        }

        return answer;
    }
    
    /**
     * uses linear algebra (rotation matrix)
     * finds vector from original position to translated position
     * first two returned are vectors to new space
     * last two returned are original coordinates
     * @param index - current index of square
     * @return - returns array of vector value
     */
    public int[] calcVector(int index) {
        // finds vector of block from index 1
        int vr_row = square[index].getRow() - square[1].getRow();
        int vr_col = square[index].getCol() - square[1].getCol();
        
        // finds vector of translated block from index 1
        int vt_row = vr_col;
        int vt_col = vr_row * -1;
        
        // finds coordinates of new block
        int vp_row = square[1].getRow() + vt_row;
        int vp_col = square[1].getCol() + vt_col;
        
        // finds vector of new block from original block
        int[] vdArr = vdCalculate(vp_row, vp_col, square[index].getRow(), square[index].getCol());
        
        // vector stored and original coords returned via array
        int[] arr = { vdArr[0], vdArr[1], square[index].getRow(), square[index].getCol() };
        return arr;
    }
    
    /**
     *	Finds vector from old to new
     */
    public int[] vdCalculate(int newRow, int newCol, int oldRow, int oldCol) {
    	int vd_row = newRow - oldRow;
    	int vd_col = newCol - oldCol;
    	int[] arr = { vd_row, vd_col };
    	return arr;
    }
    
    /**
    *   Support function for rotate
    *   Interprets vectors from calcCoords and moves accordingly
    */
    public boolean rotateMove(int index, int[] arr) {
        boolean validPos = true;
        
        if (arr[0] < 0) {
            for (int i = 0; i < Math.abs(arr[0]); i++) {
                if (square[index].canMove(Direction.UP)) {
                    square[index].move(Direction.UP);
                }
                else {
                    validPos = false;
                    break;
                }
            }
        }
        else if (arr[0] > 0) {
            for (int i = 0; i < arr[0]; i++) {
                if (square[index].canMove(Direction.DOWN)) {
                    square[index].move(Direction.DOWN);
                }
                else {
                    validPos = false;
                    break;
                }
            }
        }
        
        if (validPos) {
            if (arr[1] < 0) {
                for (int j = 0; j < Math.abs(arr[1]); j++) {
                    if (square[index].canMove(Direction.LEFT)) {
                        square[index].move(Direction.LEFT);
                    }
                    else {
                        validPos = false;
                        break;
                    }
                }
            }
            else if (arr[1] > 0) {
                for (int j = 0; j < arr[1]; j++) {
                    if (square[index].canMove(Direction.RIGHT)) {
                        square[index].move(Direction.RIGHT);
                    }
                    else {
                        validPos = false;
                        break;
                    }
                }
            }
        }
        
        // returns true if piece rotation is valid or not
        if (!validPos) {
            return true;
        }
        else {
        	return false;
        }
        
    }
    
    /**
     * Rotate the Pieces
     */
    public void rotate() {
    	if (canRotate) {
	    	boolean revert = false;
	    	
	        int[] arr0 = this.calcVector(0);
	        int[] arr2 = this.calcVector(2);
	        int[] arr3 = this.calcVector(3);
	        
	        // attempts to rotate squares
	        if (!revert)
	        	revert = this.rotateMove(0, arr0);
	        if (!revert) 
	        	revert = this.rotateMove(2, arr2);
	        if (!revert) 
	        	revert = this.rotateMove(3, arr3);
	        
	        // reverts squares back to original position
	        if (revert) {
	        	int[] r_arr0 = vdCalculate(arr0[2], arr0[3], square[0].getRow(), square[0].getCol());
	        	int[] r_arr2 = vdCalculate(arr2[2], arr2[3], square[2].getRow(), square[2].getCol());
	        	int[] r_arr3 = vdCalculate(arr3[2], arr3[3], square[3].getRow(), square[3].getCol());
	        	this.rotateMove(0, r_arr0);
	        	this.rotateMove(2, r_arr2);
	        	this.rotateMove(3, r_arr3);
	        }
    	}
        
    }
    
}
