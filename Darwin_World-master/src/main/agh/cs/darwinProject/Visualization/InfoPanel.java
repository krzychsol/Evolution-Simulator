package agh.cs.darwinProject.Visualization;

import agh.cs.darwinProject.Objects.Genotype;
import agh.cs.darwinProject.Simulation.AnimalTracker;
import agh.cs.darwinProject.Simulation.SimulationEngine;
import agh.cs.darwinProject.Simulation.StatTracker;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class InfoPanel extends JPanel {


    private final SimulationEngine simulationEngine;
    private final StatTracker statTracker;
    private final AnimalTracker animalTracker;
    private final Integer mapNumber;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public InfoPanel(SimulationEngine simulationEngine, StatTracker statTracker, AnimalTracker animalTracker){
        this.simulationEngine = simulationEngine;
        this.mapNumber = simulationEngine.getMapNumber();
        this.statTracker = statTracker;
        this.animalTracker = animalTracker;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(ColorsAndFonts.getBackgroundColor());
        g.fillRect(0,0,this.getWidth(), this.getHeight());

        g.setFont(ColorsAndFonts.getInfoFont());
        g.setColor(Color.black);

        if (mapNumber.equals(0))
            g.drawString("Left Map",10,20);
        if (mapNumber.equals(1))
            g.drawString("Right Map",10,20);

        g.drawString(generateDayNumberInfo(),10,40);
        g.drawString(generateDayNumberInfo(),10,40);
        g.drawString("Dominant genotype:",10,60);
        g.drawString(generateDominantGenotypeInfo(),10,80);
        g.drawString(generateAnimalNumberInfo(),10,100);
        g.drawString(generateGrassNumberInfo(),10,120);
        g.drawString(generateAverageEnergyInfo(),10,140);
        g.drawString(generateAverageLifeTimeInfo(),10,160);
        g.drawString(generateAverageChildrenInfo(),10,180);

        if (statTracker.getClickedAnimalGenotype()!=null){
            g.drawString("Clicked animal genotype:",10,220);
            g.drawString(statTracker.getClickedAnimalGenotype().toString(),10,240);
        }

        if (animalTracker.getTrackedAnimal() !=null){
            g.drawString("Tracked animal info:",10,280);
            g.drawString("Genotype:",10,300);
            g.drawString(generateTrackedGenotypeInfo(),10,320);
            if (animalTracker.getTrackedAnimal().isAlive()){
                g.drawString("Status: alive",10,340);
            }
            else{
                g.drawString(generateTrackedStatusInfo(),10,340);

            }
            g.drawString(generateTimeOfTrackingInfo(),10,360);
            g.drawString(generateTrackedDescendantsInfo(),10,380);
            g.drawString(generateTrackedChildrenInfo(),10,400);
        }

    }


    private String generateDominantGenotypeInfo(){
        Genotype dominantGenotype =  statTracker.getDominantGenotype();
        String genotypeText ="None";
        if (dominantGenotype != null){
            genotypeText = dominantGenotype.toString();
        }
        return(genotypeText);

    }

    private String generateAnimalNumberInfo(){
        return ("Number of animals: " + simulationEngine.getNumberOfAnimals());
    }

    private String generateGrassNumberInfo(){
        return ("Number of grasses: " + simulationEngine.getNumberOfGrasses());
    }

    private String generateAverageEnergyInfo(){
        String avgEn = decimalFormat.format(statTracker.getAverageEnergy());
        return ("Average Energy: " + avgEn);
    }
    private String generateAverageLifeTimeInfo(){

        String avgLifeTime = decimalFormat.format( statTracker.getAverageLifeTime());
        if (avgLifeTime.equals("0"))
            avgLifeTime ="No one has died yet :)";
        return ("Average life time of dead animals: " + avgLifeTime);
    }
    private String generateDayNumberInfo(){
        return ("Day: " +  statTracker.getDayNumber());
    }

    private String generateAverageChildrenInfo(){
        String avgKids = decimalFormat.format( statTracker.getAverageChildrenNumber());
        return ("Average number of alive children: " + avgKids);
    }

    private String generateTrackedGenotypeInfo(){
        return animalTracker.getTrackedAnimal().getGenotype().toString();
    }

    private String generateTrackedStatusInfo(){
        return ("Status: dead. Died on day " + animalTracker.getTrackedAnimal().getDeathDay());
    }
    private String generateTrackedDescendantsInfo(){
        return ("Alive descendants born during tracking: " + animalTracker.getTrackedAnimalDescendantsNum());
    }
    private String generateTimeOfTrackingInfo(){
        return ("Tracking animal for: " + (statTracker.getDayNumber() - animalTracker.getFirstDayOfTracking())+ " days");
    }
    private String generateTrackedChildrenInfo(){
        return ("Alive children born during tracking: " + animalTracker.getTrackedAnimalChildrenNum());
    }

}