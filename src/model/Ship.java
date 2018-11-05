package model;

public class Ship {
    private int size;
    private int hits = 0;
    private boolean vertical = true;
    private int startRow = -1;
    private int startColumn = -1;

    public Ship(int size)
    {
        this.size = size;
    }

    public void changeOrientation()
    {
        vertical = !vertical;
    }

    public int getSize() {
        return size;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void hit()
    {
        hits++;
    }

    public boolean isVertical()
    {
        return vertical;
    }

    public boolean isSunk()
    {
        return hits == size;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }
}
