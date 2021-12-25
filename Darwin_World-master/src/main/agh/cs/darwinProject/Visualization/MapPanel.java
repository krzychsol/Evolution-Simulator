package agh.cs.darwinProject.Visualization;

import agh.cs.darwinProject.Map.IAnimalCollection;
import agh.cs.darwinProject.Map.JungleMap;
import agh.cs.darwinProject.Objects.Animal;
import agh.cs.darwinProject.Objects.Genotype;
import agh.cs.darwinProject.Objects.Grass;
import agh.cs.darwinProject.Math.Vector2d;
import agh.cs.darwinProject.Simulation.AnimalTracker;
import agh.cs.darwinProject.Simulation.StatTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapPanel extends JPanel {

    private final JungleMap panelMap;
    private Vector2d selectedPosition = null;
    private Genotype dominantGenotype;
    private final int startEnergy;
    private final AnimalTracker animalTracker;
    private boolean paintDominant = false;
    private int positionWidth;
    private int positionHeight;
    private boolean trackingAnimalChangeEnabled = false;


    public MapPanel(JungleMap panelMap, int startEnergy, StatTracker statTracker, AnimalTracker animalTracker) {
        this.animalTracker = animalTracker;
        this.panelMap = panelMap;
        this.startEnergy = startEnergy;

        // on click actions
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me){
                selectedPosition = new Vector2d(me.getPoint().x, me.getPoint().y);
                Object clickedObject =  panelMap.objectAt( new Vector2d(selectedPosition.x/positionWidth,selectedPosition.y/positionHeight));
                if (clickedObject instanceof Animal){
                    if (trackingAnimalChangeEnabled){
                        animalTracker.trackedAnimalChanged((Animal)clickedObject);
                        trackingAnimalChangeEnabled = false; // animal has been chosen successfully
                    }
                    else{
                        statTracker.animalClicked((Animal) clickedObject);
                    }
                }
            }
        });

    }

    public void highlightDominantOn(Genotype dominantGenotype) {
        paintDominant = true;
        this.dominantGenotype = dominantGenotype;
    }

    public void highlightDominantOff() {
        paintDominant = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        positionWidth = this.getWidth() / panelMap.getWidth();
        positionHeight = this.getHeight() / panelMap.getHeight();

        g.setColor(ColorsAndFonts.getSteppeColor());
        g.fillRect(0, 0,  positionWidth * panelMap.getWidth(), positionHeight * panelMap.getHeight());

        g.setColor(ColorsAndFonts.getJungleColor());
        g.fillRect(panelMap.getJungleLowerLeft().x * positionWidth, panelMap.getJungleLowerLeft().y * positionHeight, panelMap.getJungleWidth() * positionWidth,
                panelMap.getJungleHeight() * positionHeight);

        //paint grass
        g.setColor(ColorsAndFonts.getGrassColor());
        for (Grass grass: panelMap.getGrasses().values()){
            // to avoid painting grass in place where animal stands
            if (panelMap.objectAt(grass.getPosition()).equals(grass)){
                g.fillOval(grass.getPosition().x * positionWidth + (int) (0.25 * positionWidth),
                        grass.getPosition().y * positionHeight + (int) (0.25 * positionHeight), positionWidth / 2, positionHeight / 2);
            }
        }
        // paint animals
        for (IAnimalCollection animalCollection: panelMap.getAnimals().values()){
            Animal paintedAnimal = animalCollection.get(0);
            if (paintDominant && (paintedAnimal).getGenotype().equals(dominantGenotype)){
                g.setColor(ColorsAndFonts.getDominantAnimalColor());
            }
            else{
                g.setColor(ColorsAndFonts.getAnimalColor(paintedAnimal, startEnergy));
            }
            g.fillOval(paintedAnimal.getPosition().x * positionWidth, paintedAnimal.getPosition().y * positionHeight, positionWidth, positionHeight);
        }

        // paint tracked animal on top
        Animal trackedAnimal = animalTracker.getTrackedAnimal();
        if (trackedAnimal!= null && trackedAnimal.isAlive()){
            g.setColor(ColorsAndFonts.getTrackedAnimalColor());
            g.fillOval(trackedAnimal.getPosition().x * positionWidth, trackedAnimal.getPosition().y * positionHeight, positionWidth, positionHeight);
        }
    }

    public boolean isPaintDominantOn() {
        return paintDominant;
    }

    public void enableTrackingAnimalChange(){
        trackingAnimalChangeEnabled = true;
    }

    public void disableTrackingAnimalChange(){
        trackingAnimalChangeEnabled = false;
    }


}
