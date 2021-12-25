package agh.cs.darwinProject.Setup;

import agh.cs.darwinProject.Simulation.SimulationEngine;
import agh.cs.darwinProject.Visualization.Visualizer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GameEngine implements ActionListener {

    private final ArrayList<SimulationEngine> simulationEngines = new ArrayList<>();
    private final Visualizer visualizer;
    private final javax.swing.Timer timer;


    public GameEngine(WorldSettings worldSettings) {
        simulationEngines.add(new SimulationEngine(worldSettings, 0));
        simulationEngines.add(new SimulationEngine(worldSettings, 1));
        this.visualizer = new Visualizer(simulationEngines);

        timer = new javax.swing.Timer(worldSettings.delay, this);
    }

    public void run() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        visualizer.repaint();
        for (SimulationEngine eng : simulationEngines) {
            if (!eng.paused)
                eng.nextDay();
        }

    }
}

