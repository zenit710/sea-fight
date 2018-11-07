package model;

public class Range {
    private Coord from;
    private Coord to;

    public Range(Coord from, Coord to) {
        this.from = from;
        this.to = to;
    }

    public Coord getFrom() {
        return from;
    }

    public Coord getTo() {
        return to;
    }
}
