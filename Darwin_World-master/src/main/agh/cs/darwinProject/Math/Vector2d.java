package agh.cs.darwinProject.Math;

import java.util.Objects;

public class Vector2d {

    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other) {

        if (this == other)
            return true;

        if (!(other instanceof Vector2d))
            return false;

        Vector2d that = (Vector2d) other;
        return that.x == this.x && that.y == this.y;
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    // shifts vector positions if they are out of boundaries. Important:
    // currently performs 1 shift (if statement instead of while) as vectors are supposed to be off by maximum 1 unit
    public Vector2d standardizeToMap( int width, int height){
        int x = this.x;
        if (x >= width){
            x-= width;
        }
        if (x < 0){
            x+= width;
        }

        int y = this.y;
        if (y >= height){
           y -= height;
        }
        if (y < 0){
            y+= height;
        }
        return new Vector2d(x,y);
    }

    public Boolean inSquareArea(Vector2d lowerLeft, Vector2d upperRight){
        return (this.follows(lowerLeft) && this.precedes(upperRight));
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
