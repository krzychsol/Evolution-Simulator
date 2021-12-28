package agh.ics.darwin.Setup;
import agh.ics.darwin.Visualization.SettingsMenu;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {

        try {
            Application.launch(SettingsMenu.class,args);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}

