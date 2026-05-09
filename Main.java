package dk.sdu.imada.oop26;

import java.util.List;

//import dk.sdu.imada.oop26.Main.GameState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public enum GameState {
        NORMAL,
        POWER,
        IMMUNE,
        LEVEL_COMPLETE,
        FINISHED
    }

    private Map map;
    private Player player;
    private List<Ghost> ghosts;

    @Override
    public void start(Stage stage){
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // UI
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

        Label endMessage = new Label("");
        endMessage.setLayoutX(220);
        endMessage.setLayoutY(20);
        endMessage.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: red;"
        );

        GameManager manager = new GameManager(ui);

        // Objects
        map = new Map(root);
        player = new Player(root, map, manager);

        Ghost hunter = new Ghost(root, map, manager, new HunterBehavior());
        Ghost assassin = new Ghost(root, map, manager, new AssassinBehavior());
        Ghost random = new Ghost(root, map, manager, new RandomBehavior());
        Ghost passive = new Ghost(root, map, manager, new PassiveBehavior());

        ghosts = List.of(hunter, assassin, random, passive);
        player.setGhost(ghosts);

        root.getChildren().addAll(ui, help, endMessage);

        Scene scene = new Scene(root, 760, 600);

        // Input
        scene.setOnKeyPressed(e -> {

            // GAME OVER
            if (manager.getState() == GameState.FINISHED) {
                if (e.getCode() == KeyCode.X) {
                    stage.close();
                    start(new Stage());
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

                if (manager.getState() == GameState.FINISHED){
                    endMessage.setText("Game Over - Press X to restart");
                    return;
                } else if (manager.getState() == GameState.LEVEL_COMPLETE) {
                    endMessage.setText("Level Complete! Press N");
                    return;
                } else {
                    endMessage.setText("");
                }

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

    private void nextLevel(Pane root, GameManager manager, Label ui, Label help, Label endMessage) {

        manager.nextLevel();

        map.loadLevel(manager.getLevel());

        // recreate player
        player = new Player(root, map, manager);

        // recreate ghosts
        Ghost hunter = new Ghost(root, map, manager, new HunterBehavior());
        Ghost assassin = new Ghost(root, map, manager, new AssassinBehavior());
        Ghost random = new Ghost(root, map, manager, new RandomBehavior());
        Ghost passive = new Ghost(root, map, manager, new PassiveBehavior());

        ghosts = List.of(hunter, assassin, random, passive);
        player.setGhost(ghosts);

        // re-add UI (important after redraw)
        root.getChildren().addAll(ui, help, endMessage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}