package dk.sdu.imada.oop26;

import java.util.List;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class Player {
    
    private int row = 2;
    private int col = 2;

    private int dx = 0;
    private int dy = 0;

    public int getDx(){ return dx; }
    public int getDy(){ return dy; }

    private long lastMove = 0;

    // for at den åbner munden
    private boolean mouthOpen = true;

    private Arc view;
    private Map map;

    private GameManager manager;
    private List<Ghost> ghosts;

    public void setGhost(List<Ghost> ghosts){
        this.ghosts = ghosts;
    }

    public Player(Pane root, Map map, GameManager manager) {
        this.map = map;
        this.manager = manager;

        view = new Arc(0,0,15,15,45,270);
        view.setType(ArcType.ROUND);
        view.setFill(javafx.scene.paint.Color.YELLOW);
        updatePosition();

        root.getChildren().add(view);
    }

    public void handleInput(KeyCode key) {
        switch(key){
            case UP ->{dx = 0; dy = -1; view.setStartAngle(135);}
            case DOWN ->{dx = 0; dy = 1; view.setStartAngle(315);}
            case LEFT ->{dx = -1; dy = 0; view.setStartAngle(225);}
            case RIGHT ->{dx = 1; dy = 0; view.setStartAngle(45);}
            /*
            case W -> newRow--;
            case S -> newRow++;
            case A -> newCol--;
            case D -> newCol++;
            */
        }
    }

    public void update(){

        if (System.currentTimeMillis() - lastMove < 150) return;

        lastMove = System.currentTimeMillis();

        int newRow = row + dy;
        int newCol = col + dx;
        

        if (!map.isWall(newRow, newCol)) {
            row = newRow;
            col = newCol;
            updatePosition();
            animateMouth();
            checkDot();
            checkTeleporter();
        }

        if(ghosts == null) return;
        for (Ghost ghost : ghosts){
            if (!view.getBoundsInParent().intersects(ghost.getView().getBoundsInParent())){
                continue;
            }

            switch (manager.getState()){
                case NORMAL -> {
                    manager.loseLife();
                return; // stops after hit
            }

                case POWER -> {
                    manager.addScore(200);
                    ghost.respawn();
                }
                default -> {}
            }

        }

        /*if (ghost == null) return;
        if (!view.getBoundsInParent().intersects(ghost.getView().getBoundsInParent())) return;

        switch (manager.getState()) {
            case NORMAL -> manager.loseLife();

            case IMMUNE -> {
                //uhmm ingenting
            }

            case POWER -> {
                manager.addScore(200);
                ghost.respawn();
            }

            case FINISHED -> {
                //blank for nu
            }
                

        }*/
    }

    private void updatePosition(){
        view.setCenterX(col * map.TILE_SIZE + map.TILE_SIZE / 2);
        view.setCenterY(row * map.TILE_SIZE + map.TILE_SIZE / 2);
    }

    private void animateMouth() {
        mouthOpen = !mouthOpen;
        if (mouthOpen) {
            view.setLength(270);
        } else {
            view.setLength(360);
        }
    }

    public Arc getView(){
        return view;
    }

    public void checkDot(){
        int tile = map.getTile(row, col);

        if (tile == Map.DOT){
            manager.addScore(10);
            map.setTile(row, col, Map.EMPTY);
            map.removeDot(row, col);
        }

        if (tile == Map.POWER){
            manager.addScore(50);
            map.setTile(row, col, Map.EMPTY);
            map.removeDot(row, col);
            manager.startPowerMode();
        }
    }

    //Only works if the teleporters are on the same row 
    public void checkTeleporter() {
        int tile = map.getTile(row, col);
        int mapLength = map.getMapWidth() - 1;

        if (tile == Map.TELEPORTER) {
            
            if (col == 0) {
                col = mapLength;

            } else {
                col = 0;
            } 

            updatePosition();
        }
    }

    public int getRow(){return row;}
    public int getCol(){return col;}
}
