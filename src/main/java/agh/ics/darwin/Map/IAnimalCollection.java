package agh.ics.darwin.Map;
import agh.ics.darwin.Objects.Animal;

public interface IAnimalCollection extends IEnergyChangeObserver {

    // returns i-th animal with greatest energy.
    // null if i is out of range
    Animal get(int ithAnimalNumber);

    void add(Animal animal);

    void remove(Animal animal);

    @Override
    void energyChanged(Animal animal);

    int size();
}
