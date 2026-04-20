package dk.sdu.imada.oop26;

import java.util.List;

import dk.sdu.imada.oop26.Main.GameState;
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
        FINISHED
    }

    private Map map;
    private Player player;
    private List<Ghost> ghosts;

    @Override
    public void start(Stage stage){
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        // UI + Manager
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

        //objects
        map = new Map(root);
        player = new Player(root, map, manager);
        Ghost hunter = new Ghost(root, map, manager, new HunterBehavior());
        Ghost assassin = new Ghost(root, map, manager, new AssassinBehavior());
        Ghost random = new Ghost(root, map, manager, new RandomBehavior());
        Ghost passive = new Ghost(root, map, manager, new PassiveBehavior());

        ghosts = List.of(hunter, assassin, random, passive);

        player.setGhost(ghosts);

        // Keep overlays on top of map/characters.
        root.getChildren().addAll(ui, help, endMessage);

        /*ghost = new Ghost(root, map, manager);
        player.setGhost(ghost);*/

        Scene scene = new Scene(root, 760, 600);

        //Input
        scene.setOnKeyPressed(e -> {

        if (manager.getState() == GameState.FINISHED) {
            if (e.getCode() == KeyCode.X) {
                stage.close();             // lukker vinduet
                start(new Stage());        // åbner nyt vindue
            }
            return;
            }

            player.handleInput(e.getCode());
        });

        

        //AnimLoop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //stop
                if (manager.getState() == GameState.FINISHED){
                    endMessage.setText("Game Over - Press X to restart");
                    return;
                } else {
                    endMessage.setText("");
                }

                player.update();

                for (Ghost g : ghosts) {
                    g.update(player);
                }

                //ghost.update(player);
            }
        };

        timer.start();;

        stage.setScene(scene);
        stage.setTitle("Pac-Man");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /* initial code when finding out what was needed


    int pacmanRow = 1;
    int pacmanCol = 1;

    private final int TILE_SIZE = 40;
        // 1 = wall, 0 = path
        private final int[][] map = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
            {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1}, 
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1}, 
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    private int score = 0;

     private void drawMap(Pane root) {
            for (int row = 0; row < map.length; row++){
                for(int col = 0; col < map[row].length; col++){

                    if (map[row][col] == 1){
                        Rectangle wall = new Rectangle(
                            col * TILE_SIZE,
                            row * TILE_SIZE,
                            TILE_SIZE,
                            TILE_SIZE
                        );
                        wall.setFill(javafx.scene.paint.Color.BLUE);
                        root.getChildren().add(wall);
                    }
                    if (map[row][col] == 0){
                        Circle dot = new Circle(
                            col * TILE_SIZE + TILE_SIZE / 2,
                            row * TILE_SIZE + TILE_SIZE / 2,
                            5
                        );
                        dot.setFill(javafx.scene.paint.Color.YELLOW);
                        root.getChildren().add(dot);
                    }
                }
            }
        }

    @Override
    public void start(Stage primaryStage){
        Pane root = new Pane();

        Scene scene = new Scene(root, 760, 600);

        primaryStage.setTitle("Pac-man");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Pacman
        Arc pacman = new Arc(100,100,20,20,45,270);
        pacman.setType(ArcType.ROUND);

        // Ghost
        Circle ghost = new Circle(20);
        ghost.setCenterX(5 * TILE_SIZE);
        ghost.setCenterY(2 * TILE_SIZE);
        ghost.setFill(javafx.scene.paint.Color.RED);*/

        /*Circle pacman = new Circle(20);
        pacman.setCenterX(100);
        pacman.setCenterY(100);*/
/*
        root.getChildren().add(pacman);
        root.getChildren().add(ghost);

        scene.setOnKeyPressed(event ->{
            int newRow = pacmanRow;
            int newCol = pacmanCol;

            switch (event.getCode()){
                case W -> newRow--;
                case S -> newRow++;
                case A -> newCol--;
                case D -> newCol++;
            }
            //Wall Collition
            if (map[newRow][newCol] != 1){
                pacmanRow = newRow;
                pacmanCol = newCol;

                //Removing dots
                if (map[newRow][newCol] == 0){
                    map[newRow][newCol] = 2; //removed or eaten
                    score++;
                    System.out.println("Score: " + score);
                }
                
                //Update pacman position
                pacman.setCenterX(pacmanCol * TILE_SIZE + TILE_SIZE / 2);
                pacman.setCenterY(pacmanRow * TILE_SIZE + TILE_SIZE / 2);

                //Redraw everything
                root.getChildren().clear();
                drawMap(root);
                root.getChildren().add(pacman);
                root.getChildren().add(ghost);
            }
        });*/
        
        /*scene.setOnKeyPressed(event ->{


            switch(event.getCode()){
                case W -> pacman.setCenterY(pacman.getCenterY() - 10);
                case S -> pacman.setCenterY(pacman.getCenterY() + 10);
                case A -> pacman.setCenterX(pacman.getCenterX() - 10);
                case D -> pacman.setCenterX(pacman.getCenterX() + 10);
            }
        });*/
/*
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now){
                if (pacman.getBoundsInParent().intersects(ghost.getBoundsInParent())) {
                    System.out.println("Loser");
                    stop(); // stop the game loop
                }
            }
        };
        timer.start();

        drawMap(root);
        }
        */
    /*public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("hello imada");
        Label label = new Label("Hello IMADA students :)");
        label.setAlignment(Pos.CENTER);
        Scene scene = new Scene(label, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/
   
}