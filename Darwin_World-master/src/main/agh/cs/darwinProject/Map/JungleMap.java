package agh.cs.darwinProject.Map;

import agh.cs.darwinProject.Objects.Animal;
import agh.cs.darwinProject.Objects.Grass;
import agh.cs.darwinProject.Objects.IMapElement;
import agh.cs.darwinProject.Math.Vector2d;
import agh.cs.darwinProject.Setup.WorldSettings;

import java.util.*;
import java.util.Random;

public class JungleMap implements IPositionChangeObserver {

    private final Vector2d jungleUpperRight;
    private final Vector2d jungleLowerLeft;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, IAnimalCollection> animals = new HashMap<>();
    private final int jungleWidth;
    private final int jungleHeight;
    private final int plantEnergy;
    private final int width;
    private final int height;

    private final static Random generator = new Random();
    private static final double criticalLoadFactor = 0.95; // when steppe or jungle has less free positions to total positions than grass generation strategy changes


    public JungleMap(WorldSettings worldSettings) {

        this.plantEnergy = worldSettings.plantEnergy;
        this.width = worldSettings.width;
        this.height = worldSettings.height;

        jungleWidth = (int) (width * worldSettings.jungleRatio);
        jungleHeight = (int)(height * worldSettings.jungleRatio);
        jungleLowerLeft = new Vector2d((width - jungleWidth) / 2, (height - jungleHeight) / 2);
        jungleUpperRight = jungleLowerLeft.add(new Vector2d(jungleWidth - 1, jungleHeight - 1));

    }

    public void placeAnimal(Animal animal) {
        Vector2d animalPosition = animal.getPosition();

        if (animals.get(animalPosition) == null) {
            IAnimalCollection tmp = new AnimalListCollection();
            animals.put(animalPosition, tmp);
        }

        animals.get(animalPosition).add(animal);
        animal.addEnergyObserver(animals.get(animalPosition));
        animal.addPositionObserver(this);

    }
    /// grass generating ///

    public void generateGrass() {
        int takenPositionsInJungle = 0;
        int takenPositionsInSteppe = 0;

        // counting mapElements to determine optimal grass generation strategy
        for (Map.Entry<Vector2d, IAnimalCollection> entry : animals.entrySet()) {
            if (entry.getKey().follows(jungleLowerLeft) && entry.getKey().precedes(jungleUpperRight) && entry.getValue().size() != 0) {
                takenPositionsInJungle += 1;
            } else if (entry.getValue().size() != 0) {
                takenPositionsInSteppe += 1;
            }
        }

        for (Vector2d position : grasses.keySet()) {
            if (position.inSquareArea(jungleLowerLeft, jungleUpperRight) && this.objectAt(position) instanceof Grass) {
                takenPositionsInJungle += 1;
            } else if (this.objectAt(position) instanceof Grass) {
                takenPositionsInSteppe += 1;
            }
        }


        double jungleLoadFactor = (double) takenPositionsInJungle / (double) (jungleWidth * jungleHeight);
        generateGrassInJungle(jungleLoadFactor > criticalLoadFactor);

        // load factor depends on whole map not only steppe map as vector is randomized from entire map in generateGrassInSteppe
        // therefore jungle area is counted as taken positions
        double steppeLoadFactor = (double) (takenPositionsInSteppe + jungleHeight * jungleWidth) / (double) (width * height);
        generateGrassInSteppe(steppeLoadFactor > criticalLoadFactor);


    }

    private void generateGrassInJungle(boolean isOvercrowded) {
        // no jungle case
        if (jungleHeight == 0 || jungleWidth == 0)
            return;

        // choosing strategy for overcrowded map based on choosing position among free positions
        if (isOvercrowded) {
            HashSet<Vector2d> jungleFreePositions = new HashSet<>();
            for (int x = jungleLowerLeft.x; x <= jungleUpperRight.x; x++) {
                for (int y = jungleLowerLeft.y; y <= jungleUpperRight.y; y++) {
                    if (this.objectAt(new Vector2d(x, y)) == null)
                        jungleFreePositions.add(new Vector2d(x, y));
                }
            }
            generateGrassFromFreePosition(jungleFreePositions);
        }
        // choosing strategy for normally crowded based on choosing random position and checking if it is valid.
        else {
            while (true) {
                Vector2d potentialPosition = new Vector2d(generator.nextInt(jungleWidth), generator.nextInt(jungleHeight)).add(jungleLowerLeft);
                if (objectAt(potentialPosition) == null) {
                    Grass newGrass = new Grass(potentialPosition, plantEnergy);
                    grasses.put(potentialPosition, newGrass);
                    return;
                }
            }

        }
    }


    private void generateGrassInSteppe(boolean isOvercrowded) {
        if (width == jungleWidth || height == jungleHeight)
            return;
        // choosing strategy for overcrowded map based on choosing position among free positions
        if (isOvercrowded) {
            HashSet<Vector2d> steppeFreePositions = new HashSet<>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (this.objectAt(new Vector2d(x, y)) == null && !(new Vector2d(x, y).inSquareArea(jungleLowerLeft, jungleUpperRight)))
                        steppeFreePositions.add(new Vector2d(x, y));
                }
            }
            generateGrassFromFreePosition(steppeFreePositions);
        }

        // choosing strategy for normally crowded based on choosing random position and checking if it is valid.
        else {
            while (true) {
                Vector2d potentialPosition = new Vector2d(generator.nextInt(width), generator.nextInt(height));
                if (objectAt(potentialPosition) == null && !potentialPosition.inSquareArea(jungleLowerLeft, jungleUpperRight)) {
                    Grass newGrass = new Grass(potentialPosition, plantEnergy);
                    grasses.put(potentialPosition, newGrass);
                    return;
                }
            }
        }


    }


    private void generateGrassFromFreePosition(HashSet<Vector2d> freePositions) {
        // strategy for overcrowded map freePositions are already steppe or jungle positions only

        if (freePositions.size() == 0)
            return;
        Vector2d positionTakenByGrass = null;
        int id = generator.nextInt(freePositions.size());
        for (Vector2d position : freePositions) {
            if (id == 0) {
                positionTakenByGrass = position;
                Grass newGrass = new Grass(position, plantEnergy);
                grasses.put(position, newGrass);
                break;
            } else
                id -= 1;
        }
        freePositions.remove(positionTakenByGrass);
    }
    ///
    public Object objectAt(Vector2d position) {
        // returns strongest animal or grass if no animal is present or null if there is no grass nor animal
        IAnimalCollection animalsOnPosition = animals.get(position);
        if (animalsOnPosition != null) {
            IMapElement elem = animalsOnPosition.get(0);
            if (elem != null)
                return elem;
        }
        return grasses.get(position);
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        // moving animal to collection assigned to new position
        if (animals.get(newPosition) == null)
            animals.put(newPosition, new AnimalListCollection());

        animals.get(oldPosition).remove(animal);
        // deleting and creating new collection when needed is faster than keeping empty collection
        // as some functions iterate over collections
        if (animals.get(oldPosition).size() == 0) {
            animals.remove(oldPosition);
        }

        animals.get(newPosition).add(animal);
        animal.removeEnergyChangeObserver();
        animal.addEnergyObserver(animals.get(newPosition));
    }


    public void removeAnimal(Animal animal) {

        Vector2d position = animal.getPosition();
        animals.get(animal.getPosition()).remove(animal);
        // deleting and creating new collection when needed is faster than keeping empty collection
        // as some functions iterate over collections
        if (animals.get(position).size() == 0) {
            animals.remove(position);
        }
    }

    public void removeGrass(Grass grass) {
        grasses.remove(grass.getPosition());

    }

    //getters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<Vector2d, IAnimalCollection> getAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public Vector2d getJungleLowerLeft() {
        return jungleLowerLeft;
    }

}
