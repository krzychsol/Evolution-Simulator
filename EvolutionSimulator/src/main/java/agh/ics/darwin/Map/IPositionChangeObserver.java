package agh.ics.darwin.Map;

import agh.ics.darwin.Objects.Animal;
import agh.ics.darwin.Math.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
}
