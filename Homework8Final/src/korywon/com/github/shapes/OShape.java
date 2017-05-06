package korywon.com.github.shapes;

import java.awt.Color;

import korywon.com.github.bin.AbstractPiece;
import korywon.com.github.bin.Grid;
import korywon.com.github.bin.Square;

public class OShape extends AbstractPiece {
	private static final boolean rotateLegal = false;
	
	public OShape(int r, int c, Grid g) {
		super(r, c, g, rotateLegal);
		
		square[0] = new Square(g, r-1, c, Color.GRAY, true);
		square[1] = new Square(g, r-1, c + 1, Color.GRAY, true);
		square[2] = new Square(g, r, c, Color.GRAY, true);
		square[3] = new Square(g, r, c +1, Color.GRAY, true);
	}
	
}
