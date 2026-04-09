package dk.sdu.imada.oop26;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import java.util.Random;

import dk.sdu.imada.oop26.Main.GameState;

public class Ghost {
    private int row = 13;
    private int col = 17;

    private Circle view;
    private Map map;
    private Random random = new Random();

    private GameManager manager;

    private long lastMoveTime = 0;
    private final long MOVE_DELAY = 500_000_000; //0.5 seconds in nanoseconds

    public Ghost(Pane root, Map map, GameManager manager) {
        this.map = map;
        this.manager = manager;

        view = new Circle(15);
        view.setFill(javafx.scene.paint.Color.RED);

        updatePosition();
        root.getChildren().add(view);
    }

    public void update(Player player){

        long now = System.nanoTime();

        if (now - lastMoveTime >= MOVE_DELAY){

            if(manager.getState() == GameState.POWER){
                moveAwayFromPlayer(player);
                view.setFill(javafx.scene.paint.Color.GRAY);
            } else {
                moveTowardsPlayer(player);
                view.setFill(javafx.scene.paint.Color.RED);
            }

            updatePosition();
            lastMoveTime = now;
        }

        /*
        long now = System.nanoTime();

        if (now - lastMoveTime >= MOVE_DELAY){
            moveTowardsPlayer(player);
            updatePosition();
            lastMoveTime = now;
        }*/
    }

    public void moveTowardsPlayer(Player player){
        
        int newRow = row;
        int newCol = col;

        boolean moveVertically = random.nextBoolean();

        if(moveVertically){
            if(player.getRow() < row) newRow--;
            else if (player.getRow() > row) newRow++;
        } else {
            if (player.getCol() < col) newCol--;
            else if (player.getCol() > col) newCol++;
        }

        //Try move
        if (!map.isWall(newRow, newCol)){
            row = newRow;
            col = newCol;
        } else {
            moveRandom();
        }



        /*
        int bestRow = row;
        int bestCol = col;

        //try to go towards player
        if (player.getRow() < row) bestRow--;
        else if (player.getRow() > row) bestRow++;

        if (player.getCol() < col) bestCol--;
        else if (player.getCol() > col) bestCol++;

        //Try move
        if (!map.isWall(bestRow, bestCol)){
            row = bestRow;
            col = bestCol;
        } else {
            moveRandom();
        } */
    }

    public void moveAwayFromPlayer(Player player){
            int bestRow = row;
            int bestCol = col;

            if(player.getRow() < row) bestRow++;
            else if (player.getRow() > row) bestRow--;

            if (player.getCol() < col) bestCol++;
            else if (player.getCol() > col) bestCol--;

            if (!map.isWall(bestRow, bestCol)){
                row = bestRow;
                col = bestCol;
            } else {
                moveRandom();
            }
        }

    private void moveRandom(){
        int[][] directions = {
            {-1,0},{1,0},{0,-1},{0,1}
        };
        for (int i = 0; i < 4; i++){
            int[] dir = directions[random.nextInt(4)];
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (!map.isWall(newRow, newCol)){
                row = newRow;
                col = newCol;
                break;
            }
        }
    }

    public void respawn(){
        row = 11;
        col = 9;
        updatePosition();
    }

    private void updatePosition(){
        view.setCenterX(col * map.TILE_SIZE + map.TILE_SIZE / 2);
        view.setCenterY(row * map.TILE_SIZE + map.TILE_SIZE / 2);
    }

    public Circle getView(){
        return view;
    }
}
