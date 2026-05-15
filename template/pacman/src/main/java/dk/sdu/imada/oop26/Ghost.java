package dk.sdu.imada.oop26;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import java.util.Random;
import java.util.List;

import dk.sdu.imada.oop26.Main.GameState;

public class Ghost {
    //private int row = 13;
    //private int col = 17;

    private int row;
    private int col;

    private int spawnRow;
    private int spawnCol;

    private Circle view;
    private Map map;
    private Random random = new Random();

    private GameManager manager;

    private GhostBehavior behavior;

    private boolean active = true;
    private List<Ghost> allGhosts;

    public void setAllGhosts(List<Ghost> ghosts) {
        this.allGhosts = ghosts;
    }

    private boolean isOccupiedByOtherGhosts(int r, int c){
        if (allGhosts == null) return false;
        for (Ghost g : allGhosts){
            if (g == this || ! g.active) continue;
            if (g.getRow() == r && g.getCol() == c) return true;
        }
        return false;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    private long lastMoveTime = 0;
    private final long MOVE_DELAY = 500_000_000; //0.5 seconds in nanoseconds

    public Ghost(Pane root, Map map, GameManager manager, GhostBehavior behavior) {
        this.map = map;
        this.manager = manager;
        this.behavior = behavior;

        int[] spawn = map.getGhostSpawn();

        spawnRow = spawn[0];
        spawnCol = spawn[1];

        row = spawnRow;
        col = spawnCol;

        view = new Circle(15);
        view.setFill(javafx.scene.paint.Color.RED);

        updatePosition();
        root.getChildren().add(view);
    }

    public void update(Player player){
        if(!active) return;

        long now = System.nanoTime();

        if (now - lastMoveTime >= MOVE_DELAY){

            if(manager.getState() == GameState.POWER){
                moveAwayFromPlayer(player);
                view.setFill(javafx.scene.paint.Color.GRAY);
            } else {
                behavior.move(this, player, map);
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

    /*public void moveTowardsPlayer(Player player){
        
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
        }*/



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
    //}

    public void moveTowards(int targetRow, int targetCol){

        int[] step = Pathfinder.nextStep(map, row, col, targetRow, targetCol);
        if (step != null && !isOccupiedByOtherGhosts(step[0], step[1])){
            row = step[0];
            col = step[1];
        }else{
            moveRandom();
        }

        /*int newRow = row;
        int newCol = col;

        if (targetRow < row) newRow--;
        else if (targetRow > row) newRow++;

        if(targetCol < col) newCol--;
        else if (targetCol > col) newCol++;

        if (!map.isWall(newRow, newCol)){
            row = newRow;
            col = newCol;
        } else {
            moveRandom();
        }*/
    }

    public void moveAwayFromPlayer(Player player){
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        int bestDist = -1;
        int bestRow = row, bestCol = col;

        for (int[] d : dirs){
            int nr = row + d[0];
            int nc = col + d[1];
            if (map.isWall(nr, nc)) continue;
            if(isOccupiedByOtherGhosts(nr, nc)) continue;

            int dist = Math.abs(nr - player.getRow()) + Math.abs(nc - player.getCol());
            if (dist > bestDist) {
                bestDist = dist;
                bestRow = nr;
                bestCol = nc;
            }
        }

        row = bestRow;
        col = bestCol;

        /*int bestRow = row;
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
        }*/
    }

    public void moveRandom(){
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int i = 3; i > 0; i--){
            int j = random.nextInt(i + 1);
            int[] tmp = directions[i];
            directions[i] = directions[j];
            directions[j] = tmp;
        }
        for (int[] dir : directions){
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (!map.isWall(newRow, newCol) && !isOccupiedByOtherGhosts(newRow, newCol)){
                row = newRow;
                col = newCol;
                return;
            }
        }
        /*int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int i = 0; i < 4; i++){
            int[] dir = directions[random.nextInt(4)];
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (!map.isWall(newRow, newCol)){
                row = newRow;
                col = newCol;
                break;
            }
        }*/
    }

    public void respawn(){
        /*row = 11;
        col = 9;
        updatePosition();*/

        active = false;

        row = spawnRow;
        col = spawnCol;

        updatePosition();

        view.setVisible(false);

        javafx.animation.PauseTransition pause = 
            new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(5)
            );

            pause.setOnFinished(e -> {
                active = true;
                view.setVisible(true);
            });
            pause.play();
    }

    private void updatePosition(){
        view.setCenterX(col * map.TILE_SIZE + map.TILE_SIZE / 2);
        view.setCenterY(row * map.TILE_SIZE + map.TILE_SIZE / 2);
    }

    public Circle getView(){
        return view;
    }
}
