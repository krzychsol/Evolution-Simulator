package agh.cs.darwinProject.Math;

public enum MoveDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    private final Vector2d[] unitVectors = {
    new Vector2d(0,1),
    new Vector2d(1,1),
    new Vector2d(1,0),
    new Vector2d(1,-1),
    new Vector2d(0,-1),
    new Vector2d(-1,-1),
    new Vector2d(-1,0),
    new Vector2d(-1,1),
    };
    

    public MoveDirection next() {
        return MoveDirection.values()[(this.ordinal() + 1) % 8];
    }

    public MoveDirection previous() {
        int newDirectionID = (this.ordinal() - 1) % 8;
        if (newDirectionID < 0) {
            newDirectionID += 8;
        }
        return MoveDirection.values()[newDirectionID];
    }

    public Vector2d toUnitVector() {
        return unitVectors[this.ordinal()];
    }
}
