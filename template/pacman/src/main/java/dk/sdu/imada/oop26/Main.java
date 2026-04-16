package dk.sdu.imada.oop26;

import dk.sdu.imada.oop26.Main.GameState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import javafx.scene.shape.Arc;
//import javafx.scene.shape.ArcType;

public class Main extends Application {

    public enum GameState {
        NORMAL,
        POWER,
        IMMUNE,
        FINISHED
    }

    private Map map;
    private Player player;
    private Ghost ghost;

    @Override
    public void start(Stage stage){
        // Ændre på layout så vi flytter score ud og juster
        BorderPane layout = new BorderPane();
        Pane gamePane = new Pane();

        // UI + Manager
        Label ui = new Label();
        ui.setStyle("-fx-font-size: 16; -fx-padding: 5;");
        GameManager manager = new GameManager(ui);
        
        layout.setTop(ui);
        layout.setCenter(gamePane);

        //objects
        map = new Map(gamePane);
        player = new Player(gamePane, map, manager);
        ghost = new Ghost(gamePane, map, manager);

        player.setGhost(ghost);

        Scene scene = new Scene(layout, 760, 630);

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
                    return;
                }

                player.update();
                ghost.update(player);
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