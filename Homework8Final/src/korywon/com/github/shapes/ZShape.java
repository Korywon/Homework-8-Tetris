package korywon.com.github.shapes;

import java.awt.Color;

import korywon.com.github.bin.AbstractPiece;
import korywon.com.github.bin.Grid;
import korywon.com.github.bin.Square;

public class ZShape extends AbstractPiece {
	private static final boolean rotateLegal = true;
	
	public ZShape(int r, int c, Grid g) {
		super(r, c, g, rotateLegal);
		
		square[0] = new Square(g, r-1, c - 1, Color.RED, true);
		square[1] = new Square(g, r-1, c, Color.RED, true);
		square[2] = new Square(g, r, c, Color.RED, true);
		square[3] = new Square (g, r, c + 1, Color.RED, true);
	}
	

}
