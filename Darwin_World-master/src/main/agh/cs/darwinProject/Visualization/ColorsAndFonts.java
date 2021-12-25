package agh.cs.darwinProject.Visualization;

import agh.cs.darwinProject.Objects.Animal;

import java.awt.*;

public  class ColorsAndFonts {
    private static final Color steppeColor = new Color(176, 240, 92);
    private static final Color jungleColor =  new Color(29, 200, 40);
    private static final Color grassColor = new Color(9, 80, 12);
    private static final Color backgroundColor = new Color(255, 255, 255);
    private static final Color dominantAnimalColor = new Color(0, 0, 200);
    private static final Color trackedAnimalColor = new Color(148, 7, 177);
    private static final Font infoFont = new Font("Aerial", Font.BOLD, 15);

    public static Font getInfoFont() {
        return infoFont;
    }

    public static Color getSteppeColor() {
        return steppeColor;
    }

    public static Color getJungleColor() {
        return jungleColor;
    }

    public static Color getGrassColor() {
        return grassColor;
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static Color getDominantAnimalColor() {
        return dominantAnimalColor;
    }

    public static Color getTrackedAnimalColor() {
        return trackedAnimalColor;
    }

    public static Color getAnimalColor(Animal animal, int startEnergy) {
        int animalColorNum = (int) ((animal.getEnergy() / (double) startEnergy) * 128.0);
        animalColorNum = Math.min(255, animalColorNum);
        return new Color(animalColorNum, 0, 0);
    }
}
