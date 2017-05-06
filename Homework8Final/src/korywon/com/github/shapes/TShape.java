package korywon.com.github.shapes;

import java.awt.Color;

import korywon.com.github.bin.AbstractPiece;
import korywon.com.github.bin.Grid;
import korywon.com.github.bin.Square;

public class TShape extends AbstractPiece {
	private static final boolean rotateLegal = true;
	
	public TShape(int r, int c, Grid g) {
		super(r, c, g, rotateLegal);
		
		square[0] = new Square(g, r, c - 1, Color.YELLOW, true);
		square[1] = new Square(g, r, c, Color.YELLOW, true);
		square[2] = new Square(g, r, c + 1, Color.YELLOW, true);
		square[3] = new Square(g, r - 1, c, Color.YELLOW, true);
	}

}
