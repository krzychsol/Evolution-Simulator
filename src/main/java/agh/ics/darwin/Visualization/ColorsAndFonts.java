package agh.ics.darwin.Visualization;

import agh.ics.darwin.Objects.Animal;
import java.awt.*;


public  class ColorsAndFonts {
    private static final Color steppeColor = new Color(132,180,16,255);
    private static final Color jungleColor =  new Color(58,118,21,255);
    private static final Color grassColor = new Color(17,61,44,255);
    private static final Color dominantAnimalColor = new Color(52,160,186,255);
    private static final Color trackedAnimalColor = new Color(123,86,146,255);
    public static final Image backgroundImage = Toolkit.getDefaultToolkit().createImage("src/main/resources/hip-square.png");
    private static final Font infoFont = new Font("Arial", Font.BOLD, 15);

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

    public static Image getBackgroundImage() {
        return backgroundImage;
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
        return new Color(animalColorNum, 255-animalColorNum, 0);
    }
}
