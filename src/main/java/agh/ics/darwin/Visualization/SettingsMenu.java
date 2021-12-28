package agh.ics.darwin.Visualization;

import agh.ics.darwin.Setup.GameEngine;
import agh.ics.darwin.Setup.WorldSettings;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SettingsMenu extends Application{

    //
    public static final int PANEL_HEIGHT = 600;
    public static final int PANEL_WIDTH = 600;

    WorldSettings worldSettings;
    {
        try {
            worldSettings = new WorldSettings();
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Darwin Project");

        // Create the settings panel from grid pane
        GridPane gridPane = drawGridPane();
        // Add UI controls to the settings panel grid pane
        addUIControls(gridPane);
        // Create a scene with settings panel grid pane as the root node
        Scene scene = new Scene(gridPane, PANEL_WIDTH, PANEL_HEIGHT);
        // Set the scene in primary stage
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private GridPane drawGridPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("SIMULATOR SETTINGS");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Width Label
        Label widthLabel = new Label("Map width : ");
        gridPane.add(widthLabel, 0,1);

        // Add Width Text Field
        TextField width = new TextField();
        width.setPrefHeight(40);
        width.setPromptText("Default: 30");
        gridPane.add(width, 1,1);

        // Add Height Label
        Label heightLabel = new Label("Map height : ");
        gridPane.add(heightLabel, 0, 2);

        // Add Height Text Field
        TextField height = new TextField();
        height.setPrefHeight(40);
        height.setPromptText("Default: 30");
        gridPane.add(height, 1, 2);

        // Add startAnimalEnergy Label
        Label startAnimalEnergyLabel = new Label("Start animal energy : ");
        gridPane.add(startAnimalEnergyLabel, 0, 3);

        // Add startAnimalEnergy Field
        TextField startAnimalEnergy = new TextField();
        startAnimalEnergy.setPrefHeight(40);
        startAnimalEnergy.setPromptText("Default: 200");
        gridPane.add(startAnimalEnergy, 1, 3);

        // Add moveEnergy Label
        Label moveEnergyLabel = new Label("Daily cost of moves : ");
        gridPane.add(moveEnergyLabel, 0, 4);

        // Add moveEnergy Field
        TextField moveEnergy = new TextField();
        moveEnergy.setPrefHeight(40);
        moveEnergy.setPromptText("Default: 2");
        gridPane.add(moveEnergy, 1, 4);

        // Add plantEnergy Label
        Label plantEnergyLabel = new Label("Energy from eating plants : ");
        gridPane.add(plantEnergyLabel, 0, 5);

        // Add plantEnergy Field
        TextField plantEnergy = new TextField();
        plantEnergy.setPrefHeight(40);
        plantEnergy.setPromptText("Default: 40");
        gridPane.add(plantEnergy, 1, 5);

        // Add jungleRatio Label
        Label jungleRatioLabel = new Label("Jungle ratio (0,1) : ");
        gridPane.add(jungleRatioLabel, 0, 6);

        // Add jungleRatio Field
        TextField jungleRatio = new TextField();
        jungleRatio.setPrefHeight(40);
        jungleRatio.setPromptText("Default: 0.2");
        gridPane.add(jungleRatio, 1, 6);

        // Add numberOfAnimals Label
        Label numberOfAnimalsLabel = new Label("Number of animals : ");
        gridPane.add(numberOfAnimalsLabel, 0, 7);

        // Add numberOfAnimals Field
        TextField numberOfAnimals = new TextField();
        numberOfAnimals.setPrefHeight(40);
        numberOfAnimals.setPromptText("Default: 10");
        gridPane.add(numberOfAnimals, 1, 7);

        // Add Start Button
        Button startButton = new Button("Start");
        startButton.setPrefHeight(40);
        startButton.setDefaultButton(true);
        startButton.setPrefWidth(100);
        gridPane.add(startButton, 0, 8, 2, 1);
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setMargin(startButton, new Insets(20, 0,20,0));

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Overwrite default world settings in parameters.json file
                if (width.getText() != null && !(width.getText().trim().isEmpty())) {
                    worldSettings.width = Integer.parseInt((width.getText()));
                }

                if (height.getText() != null && !(height.getText().trim().isEmpty())) {
                    worldSettings.height = Integer.parseInt((height.getText()));
                }

                if (startAnimalEnergy.getText() != null && !(startAnimalEnergy.getText().trim().isEmpty())) {
                    worldSettings.startEnergy = Integer.parseInt(startAnimalEnergy.getText());
                }

                if (moveEnergy.getText() != null && !(moveEnergy.getText().trim().isEmpty())) {
                    worldSettings.moveEnergy = Integer.parseInt((moveEnergy.getText()));
                }

                if (plantEnergy.getText() != null && !(plantEnergy.getText().trim().isEmpty())) {
                    worldSettings.plantEnergy = Integer.parseInt((plantEnergy.getText()));
                }

                if (jungleRatio.getText() != null && !(jungleRatio.getText().trim().isEmpty())) {
                    worldSettings.jungleRatio = Double.parseDouble((jungleRatio.getText()));
                }

                if (numberOfAnimals.getText() != null && !(numberOfAnimals.getText().trim().isEmpty())) {
                    worldSettings.numberOfAnimals = Integer.parseInt((numberOfAnimals.getText()));
                }

                worldSettings.validateArgs();

                GameEngine gameEngine = new GameEngine(worldSettings);
                gameEngine.run();

            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
