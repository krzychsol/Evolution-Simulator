package agh.cs.darwinProject.Objects;

import agh.cs.darwinProject.Map.IEnergyChangeObserver;
import agh.cs.darwinProject.Map.IPositionChangeObserver;
import agh.cs.darwinProject.Math.MoveDirection;
import agh.cs.darwinProject.Math.Vector2d;
import agh.cs.darwinProject.Map.JungleMap;
import agh.cs.darwinProject.Simulation.IAnimalDeathObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Animal implements IMapElement {

    private final JungleMap animalMap;
    private Vector2d position;
    private MoveDirection orientation;
    private int energy;
    private final Genotype genotype;

    private final int birthDay;
    private boolean isAlive = true;
    private int deathDay;

    private final ArrayList<Animal> parents = new ArrayList<>();
    private final HashSet<Animal> children = new HashSet<>();

    private IPositionChangeObserver positionChangeObserver;
    private IEnergyChangeObserver energyChangeObserver;

    private ArrayList<IAnimalDeathObserver> deathObservers = new ArrayList<>();

    private final static Random generator = new Random();


    public Animal(JungleMap jungleMap, Vector2d initialPosition, int energy, int birthDay){

        position = initialPosition;
        this.animalMap = jungleMap;
        this.birthDay = birthDay;
        orientation = MoveDirection.values()[generator.nextInt(8)];
        genotype = new Genotype();
        this.energy = energy;
    }

    // constructor based on parents
    public Animal(JungleMap jungleMap, Vector2d initialPosition, int birthDay, Animal strongerParent, Animal weakerParent){

        position = initialPosition;
        this.animalMap = jungleMap;
        this.birthDay = birthDay;
        orientation = MoveDirection.values()[generator.nextInt(8)];
        genotype = new Genotype(strongerParent.genotype, weakerParent.genotype);
        this.energy = (int) (strongerParent.energy*0.25 + weakerParent.energy*0.25);
        strongerParent.changeEnergy(-strongerParent.energy * 0.25);
        weakerParent.changeEnergy(-weakerParent.energy * 0.25);

        // creating family bonds
        this.parents.add(strongerParent);
        this.parents.add(weakerParent);
        strongerParent.children.add(this);
        weakerParent.children.add(this);

    }


    public void makeMove(){
        orientation = MoveDirection.values()[(orientation.ordinal() + genotype.getRandomGene())%8];
        Vector2d oldPosition = position;
        position = position.add(orientation.toUnitVector());
        position = position.standardizeToMap(animalMap.getWidth(), animalMap.getHeight());
        this.positionChanged(oldPosition, position);
    }

    public void kill(int day){
        animalMap.removeAnimal(this);
        removeEnergyChangeObserver();
        removePositionChangeObserver();
        isAlive = false;
        this.deathDay = day;
        removeFamilyBonds();
        for (IAnimalDeathObserver observer: deathObservers){
            observer.animalDied(this);
        }

    }

    private void removeFamilyBonds() {
            for (Animal parent: parents)
                parent.children.remove(this);
            for (Animal child: children)
                child.parents.remove(this);
    }



    public void changeEnergy(double energy){
        this.energy += energy;
        energyChanged();
    }

    /// getters

    public double getEnergy() {
        return energy;
    }

    public int getLifeTime() {
        return deathDay - birthDay;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public boolean isAlive() {
        return isAlive;

    }
    public int getChildrenNumber() {
        return children.size();
    }

    public int getDeathDay() {
        return deathDay;
    }

    ///

    /// observers related functions
    public void addPositionObserver(IPositionChangeObserver observer) {
        positionChangeObserver = observer;
    }

    public void addEnergyObserver(IEnergyChangeObserver observer) {
        energyChangeObserver = observer;
    }

    public void addDeathObserver(IAnimalDeathObserver observer) {
        deathObservers.add(observer);
    }

    public void removePositionChangeObserver(){
        positionChangeObserver = null;
    }

    public void removeEnergyChangeObserver(){
        energyChangeObserver = null;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        if (positionChangeObserver != null)
            positionChangeObserver.positionChanged(oldPosition, newPosition, this);
    }

    public void energyChanged(){
        if (energyChangeObserver != null)
            energyChangeObserver.energyChanged(this);
    }

    ///

}
