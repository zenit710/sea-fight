package model;

public class Ship {
    private int size;
    private int hits = 0;
    private boolean vertical = true;

    Ship(int size)
    {
        this.size = size;
    }

    public void changeOrientation()
    {
        vertical = !vertical;
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
}
