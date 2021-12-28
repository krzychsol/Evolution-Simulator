package agh.ics.darwin.Simulation;

import agh.ics.darwin.Objects.Animal;
import agh.ics.darwin.Objects.Genotype;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class StatTracker implements IAnimalDeathObserver {

    private final SimulationEngine simulationEngine;
    private final HashMap<Genotype, Integer> genotypeCount = new HashMap<>();
    private final HashMap<Genotype, Integer> sumOfGenotypeCount = new HashMap<>();
    private long totalDeaths = 0;
    private long totalLifeTimeOfDead = 0;
    private int dayNumber = 1;
    private Genotype dominantGenotype = null;
    private long sumOfAnimalsEveryDay = 0;
    private long sumOfGrassesEveryDay = 0;
    private double sumOfAverageEnergyEveryday = 0;
    private double sumOfAverageChildrenEveryday = 0;
    private Genotype clickedAnimalGenotype = null;


    public StatTracker(SimulationEngine simulationEngine) {
        this.simulationEngine = simulationEngine;
    }

    public void addNewDayStatsToTotalStats(int numberOfGrasses) {
        this.sumOfAnimalsEveryDay += simulationEngine.getAnimals().size();
        this.sumOfGrassesEveryDay += numberOfGrasses;
        this.sumOfAverageEnergyEveryday += this.getAverageEnergy();
        this.sumOfAverageChildrenEveryday += this.getAverageChildrenNumber();
        increaseSumOfGenotypeCount();
    }

    public void addGeneToCount(Genotype genotype) {
        if (genotypeCount.get(genotype) == null)
            genotypeCount.put(genotype, 1);
        else {
            genotypeCount.merge(genotype, 1, Integer::sum);
        }
    }

    @Override
    public void animalDied(Animal animal) {
        totalDeaths += 1;
        totalLifeTimeOfDead += animal.getLifeTime();

    }

    public void removeGeneFromCount(Genotype genotype) {
        genotypeCount.put(genotype, genotypeCount.get(genotype) - 1);
    }

    public void increaseDay() {
        this.dayNumber += 1;
    }

    public void animalClicked(Animal animal) {
        clickedAnimalGenotype = animal.getGenotype();
    }

    private void increaseSumOfGenotypeCount() {

        for (Animal animal : simulationEngine.getAnimals()) {
            if (sumOfGenotypeCount.get(animal.getGenotype()) == null)
                sumOfGenotypeCount.put(animal.getGenotype(), 1);
            else {
                sumOfGenotypeCount.merge(animal.getGenotype(), 1, Integer::sum);
            }


        }
    }

    public Genotype getDominantGenotype() {
        int highestCount = 0;
        if (dominantGenotype != null)
            highestCount = genotypeCount.get(dominantGenotype);
        for (Map.Entry<Genotype, Integer> entry : genotypeCount.entrySet()) {
            if (entry.getValue() > highestCount) {
                dominantGenotype = entry.getKey();
                highestCount = entry.getValue();

            }
        }
        if (highestCount == 0)
            dominantGenotype = null;
        return dominantGenotype;
    }

    private Genotype getMostFrequentGenotype() {

        Genotype res = null;
        int resFrequency = 0;
        for (Map.Entry<Genotype, Integer> genotypeEntry : sumOfGenotypeCount.entrySet()) {
            if (resFrequency < genotypeEntry.getValue()) {
                res = genotypeEntry.getKey();
                resFrequency = genotypeEntry.getValue();
            }
        }

        // in case of ties dominant genotype is favoured
        if (resFrequency == sumOfGenotypeCount.get(this.getDominantGenotype()))
            res = dominantGenotype;

        return res;
    }

    public double getAverageEnergy() {
        int energy = 0;
        for (Animal animal : simulationEngine.getAnimals()) {
            energy += animal.getEnergy();
        }
        if (simulationEngine.getAnimals().size() == 0)
            return 0;
        return (double) energy / (double) this.simulationEngine.getAnimals().size();
    }

    public double getAverageLifeTime() {
        if (totalDeaths != 0)
            return (double) totalLifeTimeOfDead / (double) totalDeaths;
        else
            return 0;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public double getAverageChildrenNumber() {
        int kids = 0;
        for (Animal animal : simulationEngine.getAnimals()) {
            kids += animal.getChildrenNumber();
        }
        if (simulationEngine.getAnimals().size() == 0)
            return 0;
        return (double) kids / (double) simulationEngine.getAnimals().size();
    }

    public Genotype getClickedAnimalGenotype() {
        return clickedAnimalGenotype;
    }

    public long getSumOfAnimalsEveryDay(){
        return sumOfAnimalsEveryDay;
    }

    public void saveStatsToFile(int mapNumber) throws IOException {
        String fileName = ("Map" + (mapNumber + 1) + "Stats.csv");
        DecimalFormat df = new DecimalFormat("#.##");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            writer.write("Number of animals: " + df.format((double) sumOfAnimalsEveryDay / dayNumber) + "\n");
            writer.write("Number of grasses: " + df.format((double) sumOfGrassesEveryDay / dayNumber) + "\n");
            writer.write("Average Energy: " + df.format(sumOfAverageEnergyEveryday / dayNumber) + "\n");
            writer.write("Average life time of dead animals: " + df.format(getAverageLifeTime()) + "\n");
            writer.write("Average number of children: " + df.format(sumOfAverageChildrenEveryday / dayNumber) + "\n");
        }

    }
}
