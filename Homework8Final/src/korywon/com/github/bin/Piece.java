package korywon.com.github.bin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

interface Piece {
    void draw(Graphics g);
    void move(Direction direction);
    public Point[] getLocations();
    public Color getColor();
    public boolean canMove(Direction direction);
    public int[] calcVector(int index);
    public int[] vdCalculate(int newRow, int newCol, int oldRow, int oldCol);
    public boolean rotateMove(int index, int[] arr);
    public void rotate();
}