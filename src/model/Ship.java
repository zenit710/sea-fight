package model;

public class Ship {
    private int size;
    private int hits = 0;
    private boolean vertical = true;
    private int startPositionX = -1;
    private int startPositionY = -1;

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

    public int getStartPositionX() {
        return startPositionX;
    }

    public int getStartPositionY() {
        return startPositionY;
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

    public void setStartPositionX(int startPositionX) {
        this.startPositionX = startPositionX;
    }

    public void setStartPositionY(int startPositionY) {
        this.startPositionY = startPositionY;
    }
}
