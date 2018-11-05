package model;

import java.util.ArrayList;

public class ShipListFactory {
    private int shipSize1Count;
    private int shipSize2Count;
    private int shipSize3Count;
    private int shipSize4Count;

    public ShipListFactory(int size1Count, int size2Count, int size3Count, int size4Count)
    {
        shipSize1Count = size1Count;
        shipSize2Count = size2Count;
        shipSize3Count = size3Count;
        shipSize4Count = size4Count;
    }

    public ArrayList<Ship> create()
    {
        ArrayList<Ship> ships = new ArrayList<>();

        for (int i = 0; i < shipSize4Count; i++) {
            ships.add(new Ship(4));
        }
        for (int i = 0; i < shipSize3Count; i++) {
            ships.add(new Ship(3));
        }
        for (int i = 0; i < shipSize2Count; i++) {
            ships.add(new Ship(2));
        }
        for (int i = 0; i < shipSize1Count; i++) {
            ships.add(new Ship(1));
        }

        return ships;
    }
}
