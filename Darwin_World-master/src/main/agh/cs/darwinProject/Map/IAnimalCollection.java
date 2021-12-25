package agh.cs.darwinProject.Map;

import agh.cs.darwinProject.Objects.Animal;


public interface IAnimalCollection extends IEnergyChangeObserver {

    // returns i-th animal with greatest energy.
    // null if i is out of range
    Animal get(int i);

    void add(Animal animal);

    void remove(Animal animal);

    @Override
    void energyChanged(Animal animal);

    int size();
}
