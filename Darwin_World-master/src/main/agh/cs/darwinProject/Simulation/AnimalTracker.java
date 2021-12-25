package agh.cs.darwinProject.Simulation;

import agh.cs.darwinProject.Objects.Animal;

import java.util.LinkedList;

// Used to track 1 chosen animal
public class AnimalTracker implements IAnimalDeathObserver {
    private Animal trackedAnimal = null;
    private LinkedList<Animal> trackedAnimalDescendants = new LinkedList<>();
    private LinkedList<Animal> trackedAnimalChildren = new LinkedList<>();
    private final StatTracker statTracker;
    private int firstDayOfTracking = 0;

    public AnimalTracker(StatTracker statTracker) {
        this.statTracker = statTracker;

    }

    public void newChildBorn(Animal child, Animal parentA, Animal parentB) {

        if (parentA.equals(trackedAnimal) || parentB.equals(trackedAnimal)) {
            trackedAnimalChildren.add(child);
            trackedAnimalDescendants.add(child);
            child.addDeathObserver(this);
        } else if (trackedAnimalDescendants.contains(parentA) || trackedAnimalDescendants.contains(parentB)) {
            trackedAnimalDescendants.add(child);
            child.addDeathObserver(this);
        }
    }

    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public int getTrackedAnimalChildrenNum() {
        return trackedAnimalChildren.size();
    }

    public int getTrackedAnimalDescendantsNum() {
        return trackedAnimalDescendants.size();
    }

    public void trackedAnimalChanged(Animal trackedAnimal) {
        this.trackedAnimal = trackedAnimal;
        trackedAnimalDescendants = new LinkedList<>();
        trackedAnimalChildren = new LinkedList<>();
        firstDayOfTracking = statTracker.getDayNumber();
    }

    public void trackingEnded() {
        trackedAnimalDescendants = new LinkedList<>();
        trackedAnimalChildren = new LinkedList<>();
        trackedAnimal = null;
    }

    @Override
    public void animalDied(Animal animal){
        trackedAnimalDescendants.remove(animal);
        trackedAnimalChildren.remove(animal);
    }

    public int getFirstDayOfTracking() {
        return firstDayOfTracking;
    }

}
