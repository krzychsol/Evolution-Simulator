package agh.cs.darwinProject.Setup;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {

        try {
            WorldSettings worldSettings = new WorldSettings();
            GameEngine gameEngine = new GameEngine(worldSettings);
            gameEngine.run();
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
            System.out.println("Make sure parameters.json exists and is located in the same directory as src");
            System.exit(1);
        }
        catch (IllegalArgumentException | IOException | org.json.simple.parser.ParseException ex) {
            ex.printStackTrace();
            System.exit(1);

        }

    }

}

