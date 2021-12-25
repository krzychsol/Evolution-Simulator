package agh.cs.darwinProject.Objects;

import agh.cs.darwinProject.Math.Vector2d;

public class Grass implements IMapElement {

    private final Vector2d position;

    private final double energyBonus;

    public Grass(Vector2d position, int energyBonus) {
        this.position = position;
        this.energyBonus = energyBonus;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public double getEnergyBonus() {
        return energyBonus;
    }

}

