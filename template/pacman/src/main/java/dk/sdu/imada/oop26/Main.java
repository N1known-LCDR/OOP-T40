package dk.sdu.imada.oop26;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    
    // Possible states of the game
    public enum GameState {
        NORMAL,
        POWER,
        IMMUNE,
        LEVEL_COMPLETE,
        FINISHED,
        GAME_WON
    }

    private Map map;
    private Player player;
    private List<Ghost> ghosts;

    // Sets up the UI, game objects, input handling, and the game loop
    @Override
    public void start(Stage stage){
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // UI
        // Displays score, lives, and other game info (top-left)
        Label ui = new Label();
        ui.setLayoutX(40);
        ui.setLayoutY(6);
        ui.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: rgba(0,0,0,0.35);" +
            "-fx-padding: 4px 8px 4px 8px;"
        );
        // Static hint shown to the player (top-right)
        Label help = new Label("Use arrow keys to move");
        help.setLayoutX(502);
        help.setLayoutY(6);
        help.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: rgba(0,0,0,0.35);" +
            "-fx-padding: 4px 8px 4px 8px;"
        );
        // Overlay message shown on game over, level complete, or win (center screen)
        Label endMessage = new Label("");
        endMessage.setLayoutX(0);
        endMessage.setLayoutY(250);
        endMessage.setPrefWidth(760);
        endMessage.setAlignment(javafx.geometry.Pos.CENTER);
        endMessage.setStyle(
            "-fx-font-size: 36px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: rgba(0,0,0,0.65);" +
            "-fx-padding: 16px;" +
            "-fx-border-color: red;" +
            "-fx-border-width: 3px;"
        );
        endMessage.setVisible(false);

        // Initialize game manager (tracks score, lives, level) and start background music
        GameManager manager = new GameManager(ui);
        SoundManager.init();
        SoundManager.playMainMusic();

        // Game objects
        map = new Map(root);
        player = new Player(root, map, manager);

        // Four ghosts, each with a distinct behavior and color
        Ghost hunter = new Ghost(root, map, manager, new HunterBehavior(), 0, Color.RED);
        Ghost assassin = new Ghost(root, map, manager, new AssassinBehavior() ,1, Color.HOTPINK);
        Ghost random = new Ghost(root, map, manager, new RandomBehavior() ,2, Color.YELLOW);
        Ghost passive = new Ghost(root, map, manager, new PassiveBehavior(), 3, Color.CORNFLOWERBLUE);

        ghosts = List.of(hunter, assassin, random, passive);
        // Give the player a reference to all ghosts
        player.setGhost(ghosts);
        // Give each ghost awareness of all other ghosts
        for (Ghost g : ghosts) g.setAllGhosts(ghosts);

        root.getChildren().addAll(ui, help, endMessage);

        Scene scene = new Scene(root, 760, 600);

        // Input handeling
        scene.setOnKeyPressed(e -> {

            // GAME OVER
            if (manager.getState() == GameState.FINISHED || manager.getState() == GameState.GAME_WON) {
                if (e.getCode() == KeyCode.X) {
                    stage.close();
                    start(new Stage()); // Restart by opening a fresh stage
                }
                return;
            }

            // NEXT LEVEL
            if (manager.getState() == GameState.LEVEL_COMPLETE) {
                if (e.getCode() == KeyCode.N) {
                    nextLevel(root, manager, ui, help, endMessage);
                }
                return;
            }
            
            player.handleInput(e.getCode());
        });

        // Game Loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                
                // Show appropriate overlay and pause updates if the game is no longer active
                if (manager.getState() == GameState.FINISHED){
                    endMessage.setText("Game Over - Press X to restart");
                    endMessage.setStyle(
                        "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: rgba(0,0,0,0.65);" +
                        "-fx-padding: 16px;" +
                        "-fx-border-color: red;" +
                        "-fx-border-width: 3px;"
                    );
                    endMessage.setVisible(true);
                    return;
                } else if (manager.getState() == GameState.LEVEL_COMPLETE) {
                    endMessage.setText("Level Complete! Press N");
                    endMessage.setVisible(true);
                    return;
                } else if (manager.getState() == GameState.GAME_WON) {
                    endMessage.setText("YOU WIN! Press X to restart");
                    endMessage.setStyle(
                        "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: gold;" +
                        "-fx-background-color: rgba(0,0,0,0.75);" +
                        "-fx-padding: 16px;" +
                        "-fx-border-color: gold;" +
                        "-fx-border-width: 3px;"
                    );
                    endMessage.setVisible(true);
                    return;
                } else {
                    // Hide the overlay during normal gameplay
                    endMessage.setText("");
                    endMessage.setVisible(false);
                }
                // Tick the player and all ghosts each frame
                player.update();
                for (Ghost g : ghosts) {
                    g.update(player);
                }
            }
        };

        timer.start();

        stage.setScene(scene);
        stage.setTitle("Pac-Man");
        stage.show();
    }
    // Advances Game (Clear current scene, reload map, remake Player + Ghosts)
    private void nextLevel(Pane root, GameManager manager, Label ui, Label help, Label endMessage) {

        manager.nextLevel();
        // If advancing triggered a win condition, stop here
        if (manager.getState() == GameState.GAME_WON) return;

        // Remove the old player and ghost visuals from the scene
        root.getChildren().remove(player.getView());
        for (Ghost g : ghosts) root.getChildren().remove(g.getView());

        // Load the new level's map layout
        map.loadLevel(manager.getLevel());

        // recreate player
        player = new Player(root, map, manager);

        // recreate ghosts
        Ghost hunter = new Ghost(root, map, manager, new HunterBehavior(), 0, Color.RED);
        Ghost assassin = new Ghost(root, map, manager, new AssassinBehavior(), 1, Color.HOTPINK);
        Ghost random = new Ghost(root, map, manager, new RandomBehavior(), 2, Color.YELLOW);
        Ghost passive = new Ghost(root, map, manager, new PassiveBehavior(), 3, Color.CORNFLOWERBLUE);

        ghosts = List.of(hunter, assassin, random, passive);
        player.setGhost(ghosts);

        // re-add UI (important after redraw)
        root.getChildren().addAll(ui, help, endMessage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
