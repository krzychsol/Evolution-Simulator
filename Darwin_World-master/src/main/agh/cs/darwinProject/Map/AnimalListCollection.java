package agh.cs.darwinProject.Map;

import agh.cs.darwinProject.Objects.Animal;

import java.util.ArrayList;


public class AnimalListCollection implements IAnimalCollection {

    private final ArrayList<Animal> animals = new ArrayList<>();

    public Animal get(int i) {
        if (i < animals.size())
            return animals.get(i);
        return null;
    }

    public void add(Animal animal) {
        if (animals.size() == 0)
            animals.add(animal);
        else {
            int i = 0;
            while (i < animals.size() && animals.get(i).getEnergy() > animal.getEnergy()) {
                i += 1;
            }
            animals.add(i, animal);
        }
    }

    public void remove(Animal animal) {
        animals.remove(animal);
    }

    @Override
    public void energyChanged(Animal animal) {
        animals.remove(animal);
        animals.add(animal);
    }

    public int size() {
        return animals.size();
    }


}
