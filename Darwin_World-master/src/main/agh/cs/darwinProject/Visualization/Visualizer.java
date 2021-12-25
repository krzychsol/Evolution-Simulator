package agh.cs.darwinProject.Visualization;

import agh.cs.darwinProject.Simulation.SimulationEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


public class Visualizer implements ActionListener {

    private final ArrayList<MapPanel> mapPanels = new ArrayList<>();

    private final ArrayList<SimulationEngine> simulationEngines;
    private JFrame frame;
    private final JPanel mainPanel = new JPanel();
    private final static int maximumVisibleMapSize = 100000;
    private final static int windowWidth = 1920;
    private final static int windowHeight = 1080;
    private final ArrayList<JButton> simulationControlButtons = new ArrayList<>();
    private final ArrayList<JButton> startAnimalTrackingButtons = new ArrayList<>();
    private final ArrayList<JButton> stopAnimalTrackingButtons = new ArrayList<>();
    private final ArrayList<JButton> highlightDominantButtons = new ArrayList<>();
    private final ArrayList<JButton> saveStatButtons = new ArrayList<>();
    private boolean mapsVisible = true; //if maps are too big, they won't be painted



    public Visualizer( ArrayList<SimulationEngine> simulationEngines) {

        this.simulationEngines = simulationEngines;
        frame = new JFrame("Darwin Project");
        frame.setLayout(null);
        frame.setSize(windowWidth, windowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.setLocation(0, 0);
        mainPanel.setSize(windowWidth, windowHeight);
        mainPanel.setLayout(null);

        for (SimulationEngine engine : simulationEngines) {
            int mapNumber = engine.getMapNumber();

            // crating map panel
            MapPanel mapPanel = engine.getMapView(maximumVisibleMapSize); //returns null if map id too big
            if (mapPanel != null) {
                mapPanels.add(mapPanel);
                mapPanel.setSize((int) (frame.getWidth() * 0.35), (int) (frame.getHeight() * 0.7));
                mapPanel.setLocation((int) ((frame.getWidth() * 0.25) + ((frame.getWidth() * 0.35) + 30) * mapNumber), 30);
                mainPanel.add(mapPanel);
            }
            else {
                mapsVisible = false;
            }

            // creating info panel
            InfoPanel infoPanel = new InfoPanel(engine, engine.getStatTracker(), engine.getAnimalTracker());
            infoPanel.setSize((int) (frame.getWidth() * 0.24), (int) (frame.getHeight() * 0.40));
            infoPanel.setLocation(10, 30 + (int) (10 + (frame.getHeight() * 0.40)) * mapNumber);
            mainPanel.add(infoPanel);


            int standardButtonWidth = (int)(frame.getWidth() * 0.1);
            int standardButtonHeight = 30;
            int mostLeftButtonX = (int)(frame.getWidth() * 0.25);
            int topButtonY = (int) (frame.getHeight() * 0.74);
            int mapShift = (int)(0.35 * frame.getWidth() + 30);

            addButton("Stop Simulation", (mostLeftButtonX + mapShift * mapNumber), topButtonY, standardButtonWidth,standardButtonHeight,
                    simulationControlButtons, mainPanel, true);

            addButton("Highlight animals with dominant genotype", mostLeftButtonX + mapShift * mapNumber + standardButtonWidth,
                    topButtonY,  2 * standardButtonWidth,standardButtonHeight, highlightDominantButtons, mainPanel, false);

            addButton("Start tracking new animal", mostLeftButtonX + mapShift * mapNumber,
                    topButtonY + standardButtonHeight,  standardButtonWidth, standardButtonHeight, startAnimalTrackingButtons, mainPanel, false);

            addButton("Stop tracking animal", mostLeftButtonX + mapShift * mapNumber +  standardButtonWidth,
                    topButtonY + standardButtonHeight, standardButtonWidth, standardButtonHeight, stopAnimalTrackingButtons, mainPanel, false);

            addButton("Save map stats to file",mostLeftButtonX + mapShift * mapNumber + standardButtonWidth * 2,
                    topButtonY + standardButtonHeight, standardButtonWidth, standardButtonHeight, saveStatButtons, mainPanel, true);

        }

        frame.add(mainPanel);
        frame.setVisible(true);


    }

    private void addButton(String text, int x, int y, int width, int height, ArrayList<JButton> buttonList, JPanel mainPanel, boolean isEnabled){
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.addActionListener(this);
        button.setEnabled(isEnabled);
        buttonList.add(button);
        mainPanel.add(button);
    }

    public void repaint(){
        mainPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // pause does not stop maps from repainting so that highlighting can be visible
        Object source = e.getSource();

        if (simulationControlButtons.contains(source)) {
            simControlOnClick(source, simulationControlButtons.indexOf(source));
        }
        else if (highlightDominantButtons.contains(source)) {
            highlightDomOnClick(source, highlightDominantButtons.indexOf(source));
        }
        else if (startAnimalTrackingButtons.contains(source)) {
            startAnimalTrackingOnClick(startAnimalTrackingButtons.indexOf(source));
        }
        else if (stopAnimalTrackingButtons.contains(source)) {
            stopAnimalTrackingOnClick(stopAnimalTrackingButtons.indexOf(source));
        }
        else if (saveStatButtons.contains(source)) {
            saveStatOnClick(saveStatButtons.indexOf(source));
        }
    }

    // functions called on buttons click

    private void simControlOnClick(Object btn, int mapNumber) {
        JButton button = (JButton) btn;
        if (simulationEngines.get(mapNumber).paused) {
            button.setText("Stop simulation");
            highlightDominantButtons.get(mapNumber).setEnabled(false);
            startAnimalTrackingButtons.get(mapNumber).setEnabled(false);
            stopAnimalTrackingButtons.get(mapNumber).setEnabled(false);

            // in case user hasn't turn function off
            if (mapsVisible)
                mapPanels.get(mapNumber).highlightDominantOff();
            highlightDominantButtons.get(mapNumber).setText("Highlight animals with dominant genotype");

        } else {
            button.setText("Start simulation");
            if (mapsVisible){
                highlightDominantButtons.get(mapNumber).setEnabled(true);
                startAnimalTrackingButtons.get(mapNumber).setEnabled(true);
                stopAnimalTrackingButtons.get(mapNumber).setEnabled(true);
            }
        }
        simulationEngines.get(mapNumber).paused = !simulationEngines.get(mapNumber).paused;
    }

    private void highlightDomOnClick(Object btn, int mapNumber) {
        JButton button = (JButton) btn;
        if (!mapsVisible)
            return;
        if (!mapPanels.get(mapNumber).isPaintDominantOn()) {
            mapPanels.get(mapNumber).highlightDominantOn(simulationEngines.get(mapNumber).getStatTracker().getDominantGenotype());
            button.setText("Disable highlighting");
        } else {
            mapPanels.get(mapNumber).highlightDominantOff();
            button.setText("Highlight animals with dominant genotype");
        }
    }

    private void startAnimalTrackingOnClick(int mapNumber) {
        if (!mapsVisible)
            return;
        simulationEngines.get(mapNumber).getAnimalTracker().trackingEnded();
        mapPanels.get(mapNumber).enableTrackingAnimalChange();
    }

    private void stopAnimalTrackingOnClick(int mapNumber) {
        if (!mapsVisible)
            return;
        mapPanels.get(mapNumber).disableTrackingAnimalChange();
        simulationEngines.get(mapNumber).getAnimalTracker().trackingEnded();
    }

    private void saveStatOnClick(int mapNumber){
        try {
            simulationEngines.get(mapNumber).getStatTracker().saveStatsToFile(mapNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}