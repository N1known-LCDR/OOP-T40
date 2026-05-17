package dk.sdu.imada.oop26;

import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

import dk.sdu.imada.oop26.Main.GameState;

public class Ghost {
    
    private int row;
    private int col;

    private int spawnRow;
    private int spawnCol;

    private Group view;
    private ArrayList<Shape> bodyParts = new ArrayList<>();
    private Map map;
    private Random random = new Random();

    private GameManager manager;

    private GhostBehavior behavior;

    private boolean active = true;
    private List<Ghost> allGhosts;

    private Color defaultColor;

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

    public Ghost(Pane root, Map map, GameManager manager, GhostBehavior behavior, int spawnIndex, Color color) {
        this.map = map;
        this.manager = manager;
        this.behavior = behavior;

        List<int[]> spawns = map.getGhostSpawns();
        int[] spawn = spawns.get(spawnIndex % spawns.size());

        spawnRow = spawn[0];
        spawnCol = spawn[1];

        row = spawnRow;
        col = spawnCol;

        this.defaultColor = color;
        view = createGhostView(color);

        updatePosition();
        root.getChildren().add(view);
    }

    public void update(Player player){
        if(!active) return;

        long now = System.nanoTime();

        if (now - lastMoveTime >= MOVE_DELAY){

            if (manager.getState() == GameState.POWER){
                moveAwayFromPlayer(player);
                setGhostColor(Color.GRAY);
            } else if (manager.getState() == GameState.NORMAL){
                behavior.move(this, player, map);
                setGhostColor(defaultColor);
            } else {
                behavior.move(this, player, map);
            }

            updatePosition();
            lastMoveTime = now;
        }
    }

    private Group createGhostView(Color color) {
        Group ghost = new Group();

        Arc head = new Arc(0, 0, 15, 15, 0, 180);
        head.setType(ArcType.ROUND);

        Rectangle body = new Rectangle(-15, 0, 30, 15);

        Circle bump1 = new Circle(-10, 15, 5);
        Circle bump2 = new Circle(0, 15, 5);
        Circle bump3 = new Circle(10, 15, 5);

        bodyParts.add(head);
        bodyParts.add(body);
        bodyParts.add(bump1);
        bodyParts.add(bump2);
        bodyParts.add(bump3);

        Circle leftEye = new Circle(-6, -2, 4);
        leftEye.setFill(Color.WHITE);

        Circle rightEye = new Circle(6, -2, 4);
        rightEye.setFill(Color.WHITE);

        Circle leftPupil = new Circle(-6, -2, 2);
        leftPupil.setFill(Color.BLACK);

        Circle rightPupil = new Circle(6, -2, 2);
        rightPupil.setFill(Color.BLACK);

        setGhostColor(color);

        ghost.getChildren().addAll(
            head, body, bump1, bump2, bump3,
            leftEye, rightEye, leftPupil, rightPupil
        );

        return ghost;
    }

    private void setGhostColor(Color color) {
        for (Shape part : bodyParts) {
            part.setFill(color);
        }
    }
    

    public void moveTowards(int targetRow, int targetCol){

        int[] step = Pathfinder.nextStep(map, row, col, targetRow, targetCol);
        if (step != null && !isOccupiedByOtherGhosts(step[0], step[1])){
            row = step[0];
            col = step[1];
        }else{
            moveRandom();
        }
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
    }

    public void respawn(){

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
        view.setLayoutX(col * map.TILE_SIZE + map.TILE_SIZE / 2);
        view.setLayoutY(row * map.TILE_SIZE + map.TILE_SIZE / 2);
    }

    public Node getView(){
        return view;
    }
}
