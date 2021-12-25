package agh.cs.darwinProject.Simulation;

import agh.cs.darwinProject.Map.IAnimalCollection;
import agh.cs.darwinProject.Math.MoveDirection;
import agh.cs.darwinProject.Math.Vector2d;
import agh.cs.darwinProject.Objects.Animal;
import agh.cs.darwinProject.Objects.Grass;
import agh.cs.darwinProject.Setup.WorldSettings;
import agh.cs.darwinProject.Map.JungleMap;
import agh.cs.darwinProject.Visualization.MapPanel;

import java.util.*;

public class SimulationEngine {

    private final WorldSettings worldSettings;
    private final JungleMap engineMap;
    private final LinkedList<Animal> animals;
    private final int mapNumber;
    private final StatTracker statTracker;
    private final AnimalTracker animalTracker;
    public boolean paused = false;
    private final static Random generator = new Random();

    public SimulationEngine(WorldSettings worldSettings, int mapNumber) {
        this.worldSettings = worldSettings;
        this.engineMap = new JungleMap(worldSettings);
        this.statTracker = new StatTracker(this);
        this.animalTracker = new AnimalTracker(statTracker);
        this.mapNumber = mapNumber;

        animals = AnimalGenerator.generateAnimals(worldSettings.numberOfAnimals, worldSettings.startEnergy, engineMap);
        for (Animal animal : animals) {
            statTracker.addGeneToCount(animal.getGenotype());
            animal.addDeathObserver(statTracker);
            animal.addDeathObserver(animalTracker);
        }
    }

    public void nextDay() {

        statTracker.increaseDay();
        this.removeDeadAnimals();
        this.moveAnimals();
        this.feedAnimals();
        this.breedAnimals();
        this.spawnGrass();
        statTracker.addNewDayStatsToTotalStats(engineMap.getGrasses().size());

    }

    private void removeDeadAnimals() {
        LinkedList<Animal> animalsToDel = new LinkedList<>();
        for (Animal animal : animals) {
            animal.changeEnergy(-worldSettings.moveEnergy);
            if (animal.getEnergy() <= 0) {
                animal.kill(statTracker.getDayNumber());
                animalsToDel.add(animal);
            }
        }
        for (Animal animal : animalsToDel) {
            animals.remove(animal);
            statTracker.removeGeneFromCount(animal.getGenotype());
        }

    }

    private void moveAnimals() {

        for (Animal animal : animals) {
            animal.makeMove();
        }

    }

    private void feedAnimals() {
        {
            Map<Vector2d, Grass> grasses = engineMap.getGrasses();
            Map<Vector2d, IAnimalCollection> mapAnimals = engineMap.getAnimals();
            LinkedList<Grass> grassesToRemove = new LinkedList<>();
            for (Grass grass : grasses.values()) {
                IAnimalCollection animalCollection = mapAnimals.get(grass.getPosition());
                if (animalCollection == null)
                    continue;
                Animal stronger = animalCollection.get(0);
                Animal weaker = animalCollection.get(1); // potentially weaker, ties are checked below
                if (stronger == null)
                    continue;
                // make stronger eat
                if (weaker == null || weaker.getEnergy() < stronger.getEnergy()) {
                    stronger.changeEnergy(grass.getEnergyBonus());
                    grassesToRemove.add(grass);
                }
            }
            for (Grass grass : grassesToRemove) {
                engineMap.removeGrass(grass);
            }
        }
    }

    private void breedAnimals() {
        // it is preferred to iterate over collections not single animal as
        // in one collection(on one spot) only one pair can breed
        Map<Vector2d, IAnimalCollection> mapAnimals = engineMap.getAnimals();
        LinkedList<Animal> newAnimals = new LinkedList<>();

        for (IAnimalCollection animalCollection : mapAnimals.values()) {
            if (animalCollection.size() < 2)
                continue;

            LinkedList<Animal> parents = findParents(animalCollection);
            Animal stronger = parents.get(0);
            Animal weaker = parents.get(1);

            if (weaker.getEnergy() < worldSettings.startEnergy * 0.5)
                continue;

            Vector2d childPosition = findChildPosition(stronger);

            Animal child = new Animal(engineMap, childPosition,statTracker.getDayNumber(), stronger, weaker);
            animalTracker.newChildBorn(child,stronger,weaker);
            child.addDeathObserver(statTracker);

            newAnimals.add(child);
        }

        // add new animals to simulation
        for (Animal child : newAnimals) {
            engineMap.placeAnimal(child);
            statTracker.addGeneToCount(child.getGenotype());
        }
        animals.addAll(newAnimals);

    }

    private LinkedList<Animal> findParents(IAnimalCollection animalCollection) {

        LinkedList<Animal> potentialParents = new LinkedList<>();
        potentialParents.add(animalCollection.get(0));
        potentialParents.add(animalCollection.get(1));

        int nextAnimalID = 2;
        // animals with 2nd highest energy on field with ties
        while (nextAnimalID < animalCollection.size()) {
            Animal curr = animalCollection.get(nextAnimalID);
            if ( curr.getEnergy() < potentialParents.get(1).getEnergy())
                break;
            potentialParents.add(curr);
            nextAnimalID += 1;
        }
        Animal stronger;
        Animal weaker;
        // case when stronger has greater energy than weaker animals.
        if (potentialParents.get(0).getEnergy() > potentialParents.get(1).getEnergy()) {
            stronger = potentialParents.get(0);
            int weakerID = generator.nextInt(potentialParents.size() - 1);
            weaker = potentialParents.get(1 + weakerID); // +1 as 0 is stronger
        }
        else {
            // rare case when stronger is as strong as 'weaker' animals.
            // choose 2 among stronger animal + weaker animals
            int strongerID = generator.nextInt(potentialParents.size());
            int weakerID = generator.nextInt(potentialParents.size());
            while (weakerID == strongerID) // avoid situation when one animal is two parents
                weakerID = generator.nextInt(potentialParents.size());
            stronger = potentialParents.get(strongerID);
            weaker = potentialParents.get(weakerID);
        }

        LinkedList<Animal> parents = new LinkedList<>();
        parents.add(stronger);
        parents.add(weaker);
        return parents;

    }

    private Vector2d findChildPosition(Animal parent) {

        Vector2d childPosition = null;
        int validPositions = 0;

        // count free positions around parent using Move Direction as offset
        for (int i = 0; i < 8; i++) {
            Vector2d potentialPosition = parent.getPosition().add(MoveDirection.values()[i].toUnitVector());
            potentialPosition = potentialPosition.standardizeToMap(engineMap.getWidth(), engineMap.getHeight());

            if ((engineMap.objectAt(potentialPosition) == null)) {
                validPositions += 1;
            }
        }
        // if there is a free position
        if (validPositions > 0) {
            int randPositionNum = generator.nextInt(validPositions); //  applies only to free positions
            for (int i = 0; i < 8; i++) {
                childPosition = parent.getPosition().add(MoveDirection.values()[i].toUnitVector());
                childPosition = childPosition.standardizeToMap(engineMap.getWidth(), engineMap.getHeight());

                if ((engineMap.objectAt(childPosition) == null)) {
                    randPositionNum -= 1;
                }
                if (randPositionNum < 0) {
                    break;
                }
            }
        }
        else {
            MoveDirection randDirection = MoveDirection.values()[generator.nextInt(8)];
            childPosition = parent.getPosition().add(randDirection.toUnitVector()).standardizeToMap(engineMap.getWidth(), engineMap.getHeight());
            childPosition = childPosition.standardizeToMap(engineMap.getWidth(), engineMap.getHeight());
        }
        return childPosition;
    }


    private void spawnGrass() {
        engineMap.generateGrass();

    }

    public MapPanel getMapView(int maxSize) {
        if (engineMap.getWidth() * engineMap.getHeight() <= maxSize)
            return new MapPanel(engineMap, worldSettings.startEnergy, statTracker, animalTracker);
        return null;
    }

    public int getMapNumber() {
        return mapNumber;
    }

    public int getNumberOfAnimals() {
        return animals.size();
    }

    public int getNumberOfGrasses() {
        return engineMap.getGrasses().size();
    }

    public LinkedList<Animal> getAnimals() {
        return animals;
    }

    public StatTracker getStatTracker() {
        return statTracker;
    }

    public AnimalTracker getAnimalTracker() {
        return animalTracker;
    }

}
