package agh.cs.darwinProject.Simulation;

import agh.cs.darwinProject.Objects.Animal;
import agh.cs.darwinProject.Math.Vector2d;
import agh.cs.darwinProject.Map.JungleMap;

import java.util.LinkedList;
import java.util.Random;

// generates first animals from nowhere. Rest of animals in simulation is born is simulation engine.
public class AnimalGenerator {

    public static  LinkedList<Animal> generateAnimals(int n, int startEnergy, JungleMap spawnMap){
        LinkedList<Animal> newAnimals = new LinkedList<>();
        int width = spawnMap.getWidth();
        int height = spawnMap.getHeight();
        Random generator = new Random();
        for (int i = 0; i < n; i++) {
            int x = generator.nextInt(width);
            int y = generator.nextInt(height);
            Vector2d animalPosition = new Vector2d(x,y);
            if ((spawnMap.objectAt(animalPosition) instanceof Animal)){
                i -= 1; //animal is not created
            }
            else{
                Animal animal = new Animal(spawnMap, animalPosition, startEnergy,1);
                spawnMap.placeAnimal(animal);
                newAnimals.add(animal);
            }
        }

        return newAnimals;
    }

}
