package agh.ics.darwin.Simulation;

import agh.ics.darwin.Objects.Animal;

public interface IAnimalDeathObserver {
    void animalDied(Animal animal);
}
