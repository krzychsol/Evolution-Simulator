package agh.cs.darwinProject.Simulation;

import agh.cs.darwinProject.Objects.Animal;

public interface IAnimalDeathObserver {
    void animalDied(Animal animal);
}
