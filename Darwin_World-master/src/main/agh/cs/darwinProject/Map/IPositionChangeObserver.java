package agh.cs.darwinProject.Map;

import agh.cs.darwinProject.Objects.Animal;
import agh.cs.darwinProject.Math.Vector2d;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal);
}
