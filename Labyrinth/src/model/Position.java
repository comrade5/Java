package model;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEqual(Position other) {
        return this.x == other.x && this.y == other.y;
    }
    
    public Position translate(Direction d){
        return new Position(x + d.x, y + d.y);
    }
}
